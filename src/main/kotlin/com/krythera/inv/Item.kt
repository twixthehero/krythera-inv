package com.krythera.inv

/** Bare information holder. */
abstract class Item : IItem {
    /** Retrieves this [Item]'s ID. */
    abstract override fun id(): String

    /**
     * The maximum number of items that can fit in a single [LimitedSizeItemStack].
     */
    override fun maxStackSize() = Long.MAX_VALUE

    companion object {
        val NONE = ItemNone()
    }
}