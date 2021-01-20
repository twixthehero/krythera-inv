package com.krythera.inv

import java.math.BigInteger
import java.util.Objects

/** Tracks the number of items in a stack. Can handle infinitely big stacks. */
class BigItemStack(item: IItem, amount: BigInteger = BigInteger.ONE) :
    ItemStack(item, amount.mod(BigInteger.valueOf(Long.MAX_VALUE)).longValueExact()) {
    private var stacks = amount.div(BigInteger.valueOf(Long.MAX_VALUE))

    fun fullSize(): BigInteger =
        stacks.multiply(BigInteger.valueOf(Long.MAX_VALUE)).add(BigInteger.valueOf(size))

    /** Increases the stack size by [amount]. */
    @ExperimentalUnsignedTypes
    override fun grow(amount: Long) {
        if (amount < 0) {
            return
        }

        val total = size.toULong() + amount.toULong()
        if (total > Long.MAX_VALUE.toULong()) {
            stacks = stacks.plus(BigInteger.ONE)
            size = (total - Long.MAX_VALUE.toULong()).toLong()
        } else {
            size += amount
        }
    }

    /** Decreases the stack size by [amount]. */
    override fun shrink(amount: Long) {
        if (amount < 0) {
            return
        }

        size -= amount

        if (size < 0) {
            if (stacks != BigInteger.ZERO) {
                stacks = stacks.subtract(BigInteger.ONE)
                size += Long.MAX_VALUE
            } else {
                size = 0
            }
        }

        checkEmpty()
    }

    /** Changes [item] to be [Item.NONE] if the stack is empty. */
    private fun checkEmpty() {
        if (size == 0L && stacks == BigInteger.ZERO) {
            item = Item.NONE
        }
    }

    /** Whether this stack is full. */
    override fun isFull() = false

    override fun equals(other: Any?): Boolean {
        if (other !is BigItemStack) {
            return false
        }

        return item == other.item && size == other.size && stacks == other.stacks
    }

    override fun hashCode(): Int {
        return Objects.hash(item, size, stacks)
    }

    companion object {
        val EMPTY = BigItemStack(Item.NONE)
    }
}