package com.krythera.inv

import java.math.BigInteger

/** A holder for [LimitedSizeItemStack]s. */
class LimitedSizeContainer(size: Int = 10) {
    private val stacks = Array(size) { LimitedSizeItemStack.EMPTY }

    /** The number of slots for [LimitedSizeItemStack]s in this [Container]. */
    fun size(): Int = stacks.size

    /**
     * Adds [stack] to an existing stack of the same [IItem],
     * adding to the first empty stack if none exists or there is extra.
     *
     * Returns the remaining items if this [Container] runs out of space.
     */
    @ExperimentalUnsignedTypes
    fun add(stack: LimitedSizeItemStack): LimitedSizeItemStack {
        return LimitedSizeItemStack.EMPTY
    }

    private fun findFirstNotFilled(item: IItem): Int {
        return -1
    }

    private fun findFirstEmpty(): Int {
        return -1
    }

    /**
     * Create and return a copy of the [LimitedSizeItemStack] at slot [index].
     *
     * If [index] is out of bounds, [LimitedSizeItemStack.EMPTY] is returned.
     */
    fun get(index: Int): ItemStack {
        return LimitedSizeItemStack.EMPTY
    }

    /**
     * Store [stack] into slot [index] and return the replaced [ItemStack].
     *
     * If [index] is out of bounds, [LimitedSizeItemStack.EMPTY] is returned.
     */
    fun set(index: Int, stack: LimitedSizeItemStack): LimitedSizeItemStack {
        return LimitedSizeItemStack.EMPTY
    }

    /**
     * Remove from this [Container] and return the [LimitedSizeItemStack] at slot [index].
     *
     * If [index] is out of bounds, [LimitedSizeItemStack.EMPTY] is returned.
     */
    fun remove(index: Int): ItemStack {
        return LimitedSizeItemStack.EMPTY
    }

    /**
     * Counts the amount of [item] in this [Container].
     */
    @ExperimentalUnsignedTypes
    fun count(item: IItem): CountResult {
        return CountResult(0, false)
    }

    /**
     * Counts the amount of [item] in this [Container].
     * Use to count amounts bigger than [Long.MAX_VALUE].
     */
    fun countLarge(item: IItem): BigInteger {
        return BigInteger.ZERO
    }

    /**
     * Removes [amount] of [item] from this [Container].
     */
    @ExperimentalUnsignedTypes
    fun removeAmount(item: IItem, amount: Long): ItemStack {
        return LimitedSizeItemStack.EMPTY
    }

    /**
     * Removes more than [Long.MAX_VALUE] [amount] of [item] from this [Container].
     */
    @ExperimentalUnsignedTypes
    fun removeAmountLarge(item: IItem, amount: BigInteger): BigItemStack {
        return BigItemStack.EMPTY
    }
}