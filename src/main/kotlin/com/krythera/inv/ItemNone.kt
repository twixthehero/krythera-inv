package com.krythera.inv

/** Represents "no" item. */
class ItemNone : Item() {
    /** ID reserved for [ItemNone]. */
    override fun id() = "\\empty\\"

    /** Max stack size is always zero. */
    override fun maxStackSize(): Long = 0

    override fun toString() = "None"
}