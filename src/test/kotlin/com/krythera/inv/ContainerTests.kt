package com.krythera.inv

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger

class ContainerTests {
    private val testItem = ItemTest()
    private val testItemMaxStack = ItemTestMaxStack()

    @Test
    fun `size returns correct length`() {
        val container = Container(5)
        assertThat(container.size()).isEqualTo(5)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add does nothing when full`() {
        val container = Container(1)
        container.add(ItemStack(testItem, Long.MAX_VALUE))

        val refStack = ItemStack(testItem, Long.MAX_VALUE)
        container.add(refStack)

        assertThat(refStack.isFull())
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add when empty goes in first slot`() {
        val container = Container(2)
        container.add(ItemStack(testItem))

        val firstSlot = container.get(0)
        assertThat(firstSlot.item).isEqualTo(testItem)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add modifies input stack`() {
        val container = Container(1)

        val refStack = ItemStack(testItem, 5)
        container.add(refStack)

        assertThat(refStack.isEmpty()).isTrue()
        assertThat(refStack.size).isEqualTo(0)
    }

    @Test
    fun `set stores stack into specific slot`() {
        val container = Container(2)
        container.set(1, ItemStack(testItem, 5))
        val stack = container.get(1)

        assertThat(stack).isEqualTo(ItemStack(testItem, 5))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `set returns last stack from specific slot`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 5))
        container.add(ItemStack(testItemMaxStack, 5))
        val lastStack = container.set(1, ItemStack(testItem, 5))

        assertThat(lastStack).isEqualTo(ItemStack(testItemMaxStack, 5))
    }

    @Test
    fun `set returns the input stack if out of bounds`() {
        val container = Container(2)
        val input = ItemStack(testItem, 5)

        var got = container.set(-1, input)
        assertThat(got).isEqualTo(input)

        got = container.set(container.size(), input)
        assertThat(got).isEqualTo(input)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `get returns correct ItemStack`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 5))
        container.add(ItemStack(testItemMaxStack, 10))

        val secondSlot = container.get(1)
        assertThat(secondSlot).isEqualTo(ItemStack(testItemMaxStack, 10))
    }

    @Test
    fun `get returns empty ItemStack when out of bounds`() {
        val container = Container(2)

        var got = container.get(-1)
        assertThat(got).isEqualTo(ItemStack.EMPTY)

        got = container.get(container.size())
        assertThat(got).isEqualTo(ItemStack.EMPTY)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `remove returns the correct ItemStack`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 5))
        container.add(ItemStack(testItemMaxStack, 10))

        val secondSlot = container.remove(1)
        assertThat(secondSlot).isEqualTo(ItemStack(testItemMaxStack, 10))

        assertThat(container.get(1)).isEqualTo(ItemStack.EMPTY)
    }

    @Test
    fun `remove returns empty ItemStack when out of bounds`() {
        val container = Container(2)

        var got = container.remove(-1)
        assertThat(got).isEqualTo(ItemStack.EMPTY)

        got = container.remove(container.size())
        assertThat(got).isEqualTo(ItemStack.EMPTY)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `count returns three without overflow`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 3))

        val result = container.count(testItem)
        assertThat(result.amount).isEqualTo(3)
        assertThat(result.hasOverflow).isFalse()
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `count returns MAX_VALUE with overflow`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 100))
        container.add(ItemStack(testItem, Long.MAX_VALUE))

        val result = container.count(testItem)
        assertThat(result.amount).isEqualTo(Long.MAX_VALUE)
        assertThat(result.hasOverflow).isTrue()
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `countLarge returns under MAX_VALUE`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 5))

        val result = container.countLarge(testItem)
        assertThat(result).isEqualTo(BigInteger.valueOf(5))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `countLarge returns two times MAX_VALUE`() {
        val container = Container(2)
        container.add(ItemStack(testItem, Long.MAX_VALUE))
        container.add(ItemStack(testItem, Long.MAX_VALUE))

        val result = container.countLarge(testItem)
        val max = BigInteger.valueOf(Long.MAX_VALUE)
        assertThat(result).isEqualTo(max.add(max))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmount returns 3`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 5))

        val result = container.removeAmount(testItem, 3)
        assertThat(result).isEqualTo(ItemStack(testItem, 3))

        val slot = container.get(0)
        assertThat(slot).isEqualTo(ItemStack(testItem, 2))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmount returns all`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 5))

        val result = container.removeAmount(testItem, 5)
        assertThat(result).isEqualTo(ItemStack(testItem, 5))

        val slot = container.get(0)
        assertThat(slot.isEmpty()).isTrue()
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmountLarge returns less than MAX_VALUE`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 50))

        val remove = BigInteger.valueOf(50)
        val result = container.removeAmountLarge(testItem, remove)
        assertThat(result).isEqualTo(BigItemStack(testItem, remove))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmountLarge returns more than MAX_VALUE`() {
        val container = Container(2)
        container.add(ItemStack(testItem, Long.MAX_VALUE))
        container.add(ItemStack(testItem, Long.MAX_VALUE))

        val max = BigInteger.valueOf(Long.MAX_VALUE)
        val remove = max.add(max)
        val result = container.removeAmountLarge(testItem, remove)
        assertThat(result).isEqualTo(BigItemStack(testItem, remove))
    }

    private class ItemTest : Item() {
        override fun id() = "test id"

        override fun maxStackSize() = 10L
    }

    private class ItemTestMaxStack : Item() {
        override fun id() = "test id max stack"

        override fun maxStackSize() = Long.MAX_VALUE
    }
}