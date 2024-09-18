package net.asch.bulkit.capability.network

import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler
import net.asch.bulkit.api.capability.network.INetworkLink
import net.asch.bulkit.api.kotlin.delegate.AttachmentDelegate
import net.asch.bulkit.api.kotlin.delegate.NullableAttachmentDelegate
import net.asch.bulkit.api.setup.NetworkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler

class NetworkLink(
    private val level: Level, bPos: BlockPos, bState: BlockState, bEntity: BlockEntity, ctx: Unit
) : INetworkLink {
    private var rootPos: BlockPos? by NullableAttachmentDelegate(bEntity, NetworkAttachments.VIEW_ROOT_POS.get())
    private var slotMap: Map<Int, Int> by AttachmentDelegate(bEntity, NetworkAttachments.VIEW_SLOT_MAP.get())

    override val root: IItemHandler?
        get() = rootPos?.let { level.getCapability(Capabilities.ItemHandler.BLOCK, it, Direction.NORTH) }

    override fun linkTo(bPos: BlockPos?) {
        rootPos = if (bPos == rootPos) null else bPos
    }

    override fun map(viewSlot: Int, rootSlot: Int?) {
        val mutableSlotMap = slotMap.toMutableMap()
        if (rootSlot == null) {
            mutableSlotMap.remove(viewSlot)
        } else {
            mutableSlotMap[viewSlot] = rootSlot
        }
        slotMap = mutableSlotMap
    }

    override fun getRootSlot(viewSlot: Int): Int = slotMap.getOrDefault(viewSlot, DiskProxyResourceHandler.INVALID_SLOT)

    companion object {
        fun create(
            level: Level, bPos: BlockPos, bState: BlockState, bEntity: BlockEntity?, ctx: Unit
        ): NetworkLink? = bEntity?.let { NetworkLink(level, bPos, bState, it, ctx) }
    }
}