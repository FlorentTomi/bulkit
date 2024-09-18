package net.asch.bulkit.api.capability.network

import net.minecraft.core.BlockPos
import net.neoforged.neoforge.items.IItemHandler

interface INetworkLink {
    val root: IItemHandler?

    fun linkTo(bPos: BlockPos?)
    fun map(viewSlot: Int, rootSlot: Int?)
    fun getRootSlot(viewSlot: Int): Int
}