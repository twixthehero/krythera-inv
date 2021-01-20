package com.krythera.inv

import com.google.common.truth.Truth.assertThat
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
    fun `is never full`() {
        val stack = BigItemStack(testItem, BigInteger.ONE)
        assertThat(stack.isFull()).isFalse()
    }

    private class ItemTest : Item() {
        override fun id() = "test id"

        override fun maxStackSize() = 10L
    }
}