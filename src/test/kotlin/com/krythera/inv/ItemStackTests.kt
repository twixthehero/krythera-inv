package com.krythera.inv

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ItemStackTests {
    private val testItem = ItemTest()

    @Test
    fun `stack created with zero initial size converts to NONE`() {
        val stack = ItemStack(testItem, 0)
        assertThat(stack.item).isEqualTo(Item.NONE)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `grow increases stack size`() {
        val stack = ItemStack(testItem, 1)
        stack.grow(2)
        assertThat(stack.size).isEqualTo(3)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `grow does not increase stack size when negative`() {
        val stack = ItemStack(testItem, 1)
        stack.grow(-2)
        assertThat(stack.size).isEqualTo(1)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `grow does not increase stack size over Long MAX_VALUE`() {
        val stack = ItemStack(testItem, Long.MAX_VALUE - 1)
        stack.grow(2)
        assertThat(stack.size).isEqualTo(Long.MAX_VALUE)
    }

    @Test
    fun `shrink decreases stack size`() {
        val stack = ItemStack(testItem, 3)
        stack.shrink(2)
        assertThat(stack.size).isEqualTo(1)
    }

    @Test
    fun `shrink does not decrease stack size when negative`() {
        val stack = ItemStack(testItem, 3)
        stack.shrink(-2)
        assertThat(stack.size).isEqualTo(3)
    }

    @Test
    fun `shrink does not decrease stack below zero`() {
        val stack = ItemStack(testItem, 1)
        stack.shrink(3)
        assertThat(stack.size).isEqualTo(0)
    }

    @Test
    fun `shrink to zero becomes Item NONE`() {
        val stack = ItemStack(testItem, 1)
        stack.shrink(3)
        assertThat(stack.item).isEqualTo(Item.NONE)
    }

    @Test
    fun `copy returns new instance`() {
        val stack = ItemStack(testItem)
        assertThat(stack.copy() !== stack).isTrue()
    }

    @Test
    fun `isFull true when stack is full`() {
        val stack = ItemStack(testItem, Long.MAX_VALUE)
        assertThat(stack.isFull()).isTrue()
    }

    @Test
    fun `isFull false when item is not full`() {
        val stack = ItemStack(testItem)
        assertThat(stack.isFull()).isFalse()
    }

    @Test
    fun `isEmpty true when stack is empty`() {
        val stack = ItemStack(Item.NONE)
        assertThat(stack.isEmpty()).isTrue()
    }

    @Test
    fun `isEmpty false when item is not empty`() {
        val stack = ItemStack(testItem)
        assertThat(stack.isEmpty()).isFalse()
    }

    private class ItemTest : Item() {
        override fun id() = "test id"

        override fun maxStackSize() = 10L
    }
}