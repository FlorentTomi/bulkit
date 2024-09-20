package net.asch.bulkit.api.capability.network

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity

interface INetworkLink {
    val rootPos: BlockPos?
    val root: BlockEntity?

    fun linkTo(bPos: BlockPos?)
    fun map(viewSlot: Int, rootSlot: Int?)
    fun getRootSlot(viewSlot: Int): Int
}