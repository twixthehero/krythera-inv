package com.krythera.inv

import java.math.BigInteger

/** A holder for [ItemStack]s. */
class Container(size: Int = 10) {
    private val stacks = Array(size) { ItemStack.EMPTY }

    /** The number of slots for [ItemStack]s in this [Container]. */
    fun size(): Int = stacks.size

    /**
     * Adds [stack] to an existing stack of the same [IItem],
     * adding to the first empty stack if none exists or there is extra.
     *
     * The [stack] is modified as its contents are added to the container.
     * If this [Container] runs out of space, [stack] will not be empty.
     */
    fun add(stack: ItemStack) {

    }

    /**
     * Create and return a copy of the [ItemStack] at slot [index].
     */
    fun get(index: Int): ItemStack {
        return ItemStack.EMPTY
    }

    /**
     * Store [stack] into slot [index] and return the replaced [ItemStack].
     */
    fun set(index: Int, stack: ItemStack): ItemStack {
        return ItemStack.EMPTY
    }

    /**
     * Remove from this [Container] and return the [ItemStack] at slot [index].
     */
    fun remove(index: Int): ItemStack {
        return ItemStack.EMPTY
    }

    /**
     * Counts the amount of [item] in this [Container].
     */
    fun count(item: IItem): CountResult {
        return CountResult(0, false)
    }

    /**
     * Contains the [amount] from counting and whether there were more than [Long.MAX_VALUE].
     */
    data class CountResult(val amount: Long, val hasOverflow: Boolean)

    /**
     * Counts the amount of [item] in this [Container].
     * Use to count amounts bigger than Long.MAX_VALUE.
     */
    fun countLarge(item: IItem): BigInteger {
        return BigInteger.ZERO
    }

    /**
     * Removes [amount] of [item] from this [Container].
     */
    fun removeAmount(item: IItem, amount: Long): ItemStack {
        return ItemStack.EMPTY
    }

    /**
     * Removes more than [Long.MAX_VALUE] [amount] of [item] from this [Container].
     */
    fun removeAmountLarge(item: IItem, amount: BigInteger): BigItemStack {
        return BigItemStack.EMPTY
    }
}