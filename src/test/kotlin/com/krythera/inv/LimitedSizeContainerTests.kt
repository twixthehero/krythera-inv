package com.krythera.inv

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger

class LimitedSizeContainerTests {
    private val testItem = ItemTest()
    private val testItemMaxStack = ItemTestMaxStack()

    @Test
    fun `size returns correct length`() {
        val container = LimitedSizeContainer(5)
        assertThat(container.size()).isEqualTo(5)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add does nothing when full`() {
        val container = LimitedSizeContainer(1)
        container.add(LimitedSizeItemStack(testItem, testItem.maxStackSize()))

        val refStack = LimitedSizeItemStack(testItem, testItem.maxStackSize())
        container.add(refStack)

        assertThat(refStack.isFull())
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add when empty goes in first slot`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItem))

        val firstSlot = container.get(0)
        assertThat(firstSlot.item).isEqualTo(testItem)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add returns the modified input stack`() {
        val container = LimitedSizeContainer(1)

        val resultStack = container.add(LimitedSizeItemStack(testItem, 5))

        assertThat(resultStack.isEmpty()).isTrue()
        assertThat(resultStack.size).isEqualTo(0)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add goes to multiple slots`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItem, 15))

        val firstSlot = container.get(0)
        assertThat(firstSlot.item).isEqualTo(testItem)
        assertThat(firstSlot.size).isEqualTo(testItem.maxStackSize())

        val secondSlot = container.get(1)
        assertThat(secondSlot.item).isEqualTo(testItem)
        assertThat(secondSlot.size).isEqualTo(5)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `add leaves remainder in input stack`() {
        val container = LimitedSizeContainer(1)
        container.add(LimitedSizeItemStack(testItem, 5))

        val refStack = LimitedSizeItemStack(testItem, 10)
        container.add(refStack)

        assertThat(refStack.isEmpty()).isFalse()
        assertThat(refStack.size).isEqualTo(5)
    }

    @Test
    fun `set stores stack into specific slot`() {
        val container = LimitedSizeContainer(2)
        container.set(1, LimitedSizeItemStack(testItem, 5))
        val stack = container.get(1)

        assertThat(stack).isEqualTo(LimitedSizeItemStack(testItem, 5))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `set returns last stack from specific slot`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItem, 5))
        container.add(LimitedSizeItemStack(testItemMaxStack, 5))
        val lastStack = container.set(1, LimitedSizeItemStack(testItem, 5))

        assertThat(lastStack).isEqualTo(LimitedSizeItemStack(testItemMaxStack, 5))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `get returns correct ItemStack`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItem, 5))
        container.add(LimitedSizeItemStack(testItemMaxStack, 10))

        val secondSlot = container.get(1)
        assertThat(secondSlot).isEqualTo(LimitedSizeItemStack(testItemMaxStack, 10))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `remove returns the correct ItemStack`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItem, 5))
        container.add(LimitedSizeItemStack(testItemMaxStack, 10))

        val secondSlot = container.remove(1)
        assertThat(secondSlot).isEqualTo(LimitedSizeItemStack(testItemMaxStack, 10))

        assertThat(container.get(1)).isEqualTo(LimitedSizeItemStack.EMPTY)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `count returns three without overflow`() {
        val container = LimitedSizeContainer(1)
        container.add(LimitedSizeItemStack(testItem, 3))

        val result = container.count(testItem)
        assertThat(result.amount).isEqualTo(3)
        assertThat(result.hasOverflow).isFalse()
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `count returns MAX_VALUE with overflow`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItemMaxStack, 100))
        container.add(LimitedSizeItemStack(testItemMaxStack, Long.MAX_VALUE))

        val result = container.count(testItemMaxStack)
        assertThat(result.amount).isEqualTo(Long.MAX_VALUE)
        assertThat(result.hasOverflow).isTrue()
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `countLarge returns under MAX_VALUE`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItem, 5))

        val result = container.countLarge(testItem)
        assertThat(result).isEqualTo(BigInteger.valueOf(5))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `countLarge returns two times MAX_VALUE`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItemMaxStack, Long.MAX_VALUE))
        container.add(LimitedSizeItemStack(testItemMaxStack, Long.MAX_VALUE))

        val result = container.countLarge(testItemMaxStack)
        val max = BigInteger.valueOf(Long.MAX_VALUE)
        assertThat(result).isEqualTo(max.add(max))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmount returns 3`() {
        val container = LimitedSizeContainer(1)
        container.add(LimitedSizeItemStack(testItem, 5))

        val result = container.removeAmount(testItem, 3)
        assertThat(result).isEqualTo(LimitedSizeItemStack(testItem, 3))

        val slot = container.get(0)
        assertThat(slot).isEqualTo(LimitedSizeItemStack(testItem, 2))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmount returns all`() {
        val container = LimitedSizeContainer(1)
        container.add(LimitedSizeItemStack(testItem, 5))

        val result = container.removeAmount(testItem, 5)
        assertThat(result).isEqualTo(LimitedSizeItemStack(testItem, 5))

        val slot = container.get(0)
        assertThat(slot.isEmpty()).isTrue()
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmountLarge returns less than MAX_VALUE`() {
        val container = LimitedSizeContainer(1)
        container.add(LimitedSizeItemStack(testItem, 7))

        val remove = BigInteger.valueOf(5)
        val result = container.removeAmountLarge(testItem, remove)
        assertThat(result).isEqualTo(BigItemStack(testItem, remove))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `removeAmountLarge returns more than MAX_VALUE`() {
        val container = LimitedSizeContainer(2)
        container.add(LimitedSizeItemStack(testItemMaxStack, Long.MAX_VALUE))
        container.add(LimitedSizeItemStack(testItemMaxStack, Long.MAX_VALUE))

        val max = BigInteger.valueOf(Long.MAX_VALUE)
        val remove = max.add(max)
        val result = container.removeAmountLarge(testItemMaxStack, remove)
        assertThat(result).isEqualTo(BigItemStack(testItemMaxStack, remove))
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