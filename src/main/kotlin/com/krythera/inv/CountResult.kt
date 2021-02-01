package com.krythera.inv

/**
 * Contains the [amount] from counting and whether there were more than [Long.MAX_VALUE].
 */
data class CountResult(val amount: Long, val hasOverflow: Boolean)
