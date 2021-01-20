Krythera Inv - an [infinite] inventory system

---

# Concepts

`Item` - A single instance which contains shared data about an item.

`ItemStack` - Holds an amount of `Item`.

# Usage

Any class implementing `com.krythera.inv.IItem` is compatible with this library.

Example:

```kotlin
import com.krythera.inv.IItem

class ItemSand : IItem() {
    override fun id() = "id-sand"
    
    override fun maxStackSize() = 2500L
    
    override fun toString() = "Sand[maxStackSize=${maxStackSize()}]"
}

...

fun main() {
    val itemSand = ItemSand()
    val stack = ItemStack(itemSand, 2000)
    println(stack.item) // Sand[maxStackSize=2500]
    
    stack.grow(500)
    println(stack.size) // 2500
    
    stack.grow(5000) // caps at Long.MAX_VALUE. Use LimitedSizeItemStack for maxStackSize()
    println(stack.size) // 7500
    
    stack.shrink(6500)
    println(stack.size) // 1000
    println(stack.isEmpty()) // false
    
    stack.shrink(2000) // can't go below 0
    println(stack.size) // 0
    println(stack.isEmpty()) // true
    
    // NOTE: stack.item becomes Item.NONE when it reaches 0
    println(stack.item) // None
}
```

|Amount of Items|Class to Use|
|---|---|
|0 < x < `IItem.maxStackSize()`|`LimitedSizeItemStack`|
|0 < x < `Long.MAX_VALUE`|`ItemStack`|

# License

MIT