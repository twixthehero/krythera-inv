package com.krythera.inv

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class LimitedSizeItemStackTests {
    private val testItem = ItemTest()

    @ExperimentalUnsignedTypes
    @Test
    fun `grow does not exceed max stack size`() {
        val stack = LimitedSizeItemStack(testItem, 5)
        stack.grow(10)
        assertThat(stack.size).isAtMost(stack.item.maxStackSize())
    }

    @Test
    fun `shrink limits the stack size if reducing above max stack size`() {
        val stack = LimitedSizeItemStack(testItem, 50)
        stack.shrink(10)
        assertThat(stack.size).isAtMost(stack.item.maxStackSize())
    }

    @Test
    fun `sizeLeft returns correct value`() {
        val stack = LimitedSizeItemStack(testItem, 3)
        assertThat(stack.sizeLeft()).isEqualTo(testItem.maxStackSize() - stack.size)
    }

    @Test
    fun `fill maxes the stack`() {
        val stack = LimitedSizeItemStack(testItem, 1)
        stack.fill()
        assertThat(stack.size).isEqualTo(testItem.maxStackSize())
    }

    @Test
    fun `copy returns new instance`() {
        val stack = LimitedSizeItemStack(testItem)
        assertThat(stack.copy() !== stack).isTrue()
    }

    @Test
    fun `isFull true when stack is full`() {
        val stack = LimitedSizeItemStack(testItem, testItem.maxStackSize())
        assertThat(stack.isFull()).isTrue()
    }

    @Test
    fun `isFull false when item is not full`() {
        val stack = LimitedSizeItemStack(testItem)
        assertThat(stack.isFull()).isFalse()
    }

    private class ItemTest : Item() {
        override fun id() = "test id"

        override fun maxStackSize() = 10L
    }
}