package net.asch.bulkit.api.block

import net.minecraft.world.level.block.state.properties.IntegerProperty

object BlockStates {
    val NETWORK_VIEW_SIZES: List<Int> = listOf(1, 2, 4)
    val NETWORK_VIEW_SIZE: IntegerProperty =
        IntegerProperty.create("size", NETWORK_VIEW_SIZES.min(), NETWORK_VIEW_SIZES.max())

}