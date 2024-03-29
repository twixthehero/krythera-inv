package com.krythera.inv

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigInteger

class BigItemStackTests {
    private val testItem = ItemTest()

    @ExperimentalUnsignedTypes
    @Test
    fun `grow can increase more than MAX_VALUE`() {
        val stack = BigItemStack(testItem, BigInteger.ZERO)
        stack.grow(Long.MAX_VALUE)
        stack.grow(Long.MAX_VALUE)

        val max = BigInteger.valueOf(Long.MAX_VALUE)
        assertThat(stack.fullSize()).isEqualTo(max.add(max))
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `grow does not increase stack size when negative`() {
        val stack = BigItemStack(testItem, BigInteger.ONE)
        stack.grow(-2)
        assertThat(stack.fullSize()).isEqualTo(BigInteger.ONE)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `shrink can decrease more than MAX_VALUE`() {
        val stack = BigItemStack(testItem, BigInteger.ZERO)
        stack.grow(Long.MAX_VALUE)
        stack.grow(1)

        stack.shrink(Long.MAX_VALUE)
        stack.shrink(1)
        assertThat(stack.fullSize()).isEqualTo(BigInteger.ZERO)
    }

    @Test
    fun `shrink does not decrease stack size when negative`() {
        val stack = BigItemStack(testItem, BigInteger.valueOf(3))
        stack.shrink(-2)
        assertThat(stack.fullSize()).isEqualTo(BigInteger.valueOf(3))
    }

    @Test
    fun `shrink does not decrease stack below zero`() {
        val stack = BigItemStack(testItem, BigInteger.ONE)
        stack.shrink(3)
        assertThat(stack.fullSize()).isEqualTo(BigInteger.ZERO)
    }

    @Test
    fun `fill throws`() {
        assertThrows(Exception::class.java) {
            BigItemStack(testItem).fill()
        }
    }

    @Test
    fun `empty clears the stack`() {
        val stack = BigItemStack(testItem, BigInteger.ONE)
        stack.empty()
        assertThat(stack).isEqualTo(BigItemStack.EMPTY)
    }

    @Test
    fun `sizeLeft throws`() {
        assertThrows(Exception::class.java) {
            BigItemStack(testItem).sizeLeft()
        }
    }

    @Test
    fun `is never full`() {
        val stack = BigItemStack(testItem, BigInteger.ONE)
        assertThat(stack.isFull()).isFalse()
    }

    @Test
    fun `isEmpty with no stacks`() {
        val stack = BigItemStack(testItem, BigInteger.ZERO)
        assertThat(stack.isEmpty()).isTrue()
    }

    @Test
    fun `is not empty with stacks`() {
        val stack = BigItemStack(
            testItem,
            BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(Long.MAX_VALUE))
        )
        assertThat(stack.isEmpty()).isFalse()
    }

    private class ItemTest : Item() {
        override fun id() = "test id"

        override fun maxStackSize() = 10L
    }
}