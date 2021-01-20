package com.krythera.inv

/** Any class implementing [IItem] will work with this library. */
interface IItem {
    fun id(): String
    fun maxStackSize(): Long
}