package com.krythera.inv

import java.util.Objects

/** Tracks the number of items in a stack while limiting the stack size. */
class LimitedSizeItemStack(item: IItem, size: Long = 1) : ItemStack(item, size) {
    /** Increases the stack size by [amount]. */
    @ExperimentalUnsignedTypes
    override fun grow(amount: Long) {
        super.grow(amount)

        correctStackSize()
    }

    /** Decreases the stack size by [amount]. */
    override fun shrink(amount: Long) {
        super.shrink(amount)

        correctStackSize()
    }

    /** Clamp stack size to between zero and [IItem.maxStackSize]. */
    private fun correctStackSize() {
        if (item.maxStackSize() >= 0) {
            if (size > item.maxStackSize()) {
                size = item.maxStackSize()
            }
        }

        if (size == 0L) {
            item = Item.NONE
        }
    }

    /** Create and return a copy of this [LimitedSizeItemStack]. */
    override fun copy(): ItemStack = LimitedSizeItemStack(item, size)

    /** Whether this stack is full. */
    override fun isFull() = size == item.maxStackSize()

    override fun equals(other: Any?): Boolean {
        if (other !is LimitedSizeItemStack) {
            return false
        }

        return item == other.item && size == other.size
    }

    override fun hashCode(): Int {
        return Objects.hash(item, size)
    }
}