package com.krythera.inv

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ItemNoneTests {
    @Test
    fun `id is always none`() {
        val none = ItemNone()
        assertThat(none.id()).isEqualTo(Item.NONE.id())
    }

    @Test
    fun `max stack size is always zero`() {
        val none = ItemNone()
        assertThat(none.maxStackSize()).isEqualTo(0)
    }
}