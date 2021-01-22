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

    @Test
    fun `add when empty goes in first slot`() {
        val container = Container()
        container.add(ItemStack(testItem))

        val firstSlot = container.get(0)
        assertThat(firstSlot.item).isEqualTo(testItem)
    }

    @Test
    fun `add goes to multiple slots`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 15))

        val firstSlot = container.get(0)
        assertThat(firstSlot.item).isEqualTo(testItem)
        assertThat(firstSlot.size).isEqualTo(testItem.maxStackSize())

        val secondSlot = container.get(1)
        assertThat(secondSlot.item).isEqualTo(testItem)
        assertThat(secondSlot.size).isEqualTo(5)
    }

    @Test
    fun `add modifies input stack`() {
        val container = Container(1)

        val refStack = ItemStack(testItem, 5)
        container.add(refStack)

        assertThat(refStack.isEmpty()).isTrue()
        assertThat(refStack.size).isEqualTo(0)
    }

    @Test
    fun `add leaves remainder in input stack`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 5))

        val refStack = ItemStack(testItem, 10)
        container.add(refStack)

        assertThat(refStack.isEmpty()).isFalse()
        assertThat(refStack.size).isEqualTo(5)
    }

    // TODO(max): add set tests

    @Test
    fun `get returns correct ItemStack`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 5))
        container.add(ItemStack(testItemMaxStack, 10))

        val secondSlot = container.get(1)
        assertThat(secondSlot).isEqualTo(ItemStack(testItemMaxStack, 10))
    }

    @Test
    fun `remove returns the correct ItemStack`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 5))
        container.add(ItemStack(testItemMaxStack, 10))

        val secondSlot = container.remove(1)
        assertThat(secondSlot).isEqualTo(ItemStack(testItemMaxStack, 10))

        assertThat(container.get(1)).isEqualTo(ItemStack.EMPTY)
    }

    @Test
    fun `count returns three without overflow`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 3))

        val result = container.count(testItem)
        assertThat(result.amount).isEqualTo(3)
        assertThat(result.hasOverflow).isFalse()
    }

    @Test
    fun `count returns MAX_VALUE with overflow`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 100))
        container.add(ItemStack(testItem, Long.MAX_VALUE))

        val result = container.count(testItem)
        assertThat(result.amount).isEqualTo(Long.MAX_VALUE)
        assertThat(result.hasOverflow).isTrue()
    }

    @Test
    fun `countLarge returns under MAX_VALUE`() {
        val container = Container(2)
        container.add(ItemStack(testItem, 5))

        val result = container.countLarge(testItem)
        assertThat(result).isEqualTo(BigInteger.valueOf(5))
    }

    @Test
    fun `countLarge returns two times MAX_VALUE`() {
        val container = Container(2)
        container.add(ItemStack(testItem, Long.MAX_VALUE))
        container.add(ItemStack(testItem, Long.MAX_VALUE))

        val result = container.countLarge(testItem)
        val max = BigInteger.valueOf(Long.MAX_VALUE)
        assertThat(result).isEqualTo(max.add(max))
    }

    @Test
    fun `removeAmount returns 3`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 5))

        val result = container.removeAmount(testItem, 3)
        assertThat(result).isEqualTo(ItemStack(testItem, 3))

        val slot = container.get(0)
        assertThat(slot).isEqualTo(ItemStack(testItem, 2))
    }

    @Test
    fun `removeAmount returns all`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 5))

        val result = container.removeAmount(testItem, 5)
        assertThat(result).isEqualTo(ItemStack(testItem, 5))

        val slot = container.get(0)
        assertThat(slot.isEmpty()).isTrue()
    }

    @Test
    fun `removeAmountLarge returns less than MAX_VALUE`() {
        val container = Container(1)
        container.add(ItemStack(testItem, 50))

        val remove = BigInteger.valueOf(50)
        val result = container.removeAmountLarge(testItem, remove)
        assertThat(result).isEqualTo(BigItemStack(testItem, remove))
    }

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
        override fun id() = "test id"

        override fun maxStackSize() = Long.MAX_VALUE
    }
}