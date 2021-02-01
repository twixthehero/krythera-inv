package com.krythera.inv

import java.util.Objects

/** Tracks the number of items in a stack ignoring maximum stack size. */
open class ItemStack(initialItem: IItem, initialSize: Long = 1) {
    var item: IItem = initialItem
        protected set
    var size: Long = initialSize
        protected set

    init {
        checkEmpty()
    }

    /** Increases the stack size by [amount]. */
    @ExperimentalUnsignedTypes
    open fun grow(amount: Long) {
        if (amount < 0) {
            return
        }

        // overflow would wrap around. convert to unsigned to avoid.
        if (size.toULong() + amount.toULong() > Long.MAX_VALUE.toULong()) {
            size = Long.MAX_VALUE
        } else {
            size += amount
        }
    }

    /** Decreases the stack size by [amount]. */
    open fun shrink(amount: Long) {
        if (amount < 0) {
            return
        }

        size -= amount

        if (size < 0) {
            size = 0
        }

        checkEmpty()
    }

    /** Changes [item] to be [Item.NONE] if the stack is empty. */
    private fun checkEmpty() {
        if (size == 0L) {
            item = Item.NONE
        }
    }

    /** Fills this [ItemStack] to max capacity. */
    open fun fill() {
        size = Long.MAX_VALUE
    }

    /** Sets this [ItemStack] to [EMPTY]. */
    open fun empty() {
        size = 0L

        checkEmpty()
    }

    /** How much more this [ItemStack] can store. */
    open fun sizeLeft(): Long = Long.MAX_VALUE - size

    /** Create and return a copy of this [ItemStack]. */
    open fun copy() = ItemStack(item, size)

    /** Whether this stack is full. */
    open fun isFull() = size == Long.MAX_VALUE

    /** Whether there are no [IItem]s in this stack. */
    fun isEmpty() = item == Item.NONE

    override fun equals(other: Any?): Boolean {
        if (other !is ItemStack) {
            return false
        }

        return item == other.item && size == other.size
    }

    override fun hashCode(): Int {
        return Objects.hash(item, size)
    }

    companion object {
        val EMPTY = ItemStack(Item.NONE, 0)
    }
}