package net.asch.bulkit.api

import net.minecraft.world.level.block.state.properties.IntegerProperty

object BlockStates {
    object Network {
        val VIEW_SIZES: List<Int> = listOf(1, 2, 4)
        val VIEW_SIZE: IntegerProperty = IntegerProperty.create("size", 1, Int.MAX_VALUE)
    }
}