package net.asch.bulkit.capability.network

import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler
import net.asch.bulkit.api.capability.network.INetworkLink
import net.asch.bulkit.api.kotlin.delegate.AttachmentDelegate
import net.asch.bulkit.api.kotlin.delegate.NullableAttachmentDelegate
import net.asch.bulkit.api.setup.NetworkAttachments
import net.asch.bulkit.setup.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import kotlin.jvm.optionals.getOrNull

class NetworkLink(private val bEntity: BlockEntity, ctx: Unit) : INetworkLink {
    override var rootPos: BlockPos? by NullableAttachmentDelegate(bEntity, NetworkAttachments.VIEW_ROOT_POS.get())
    private var slotMap: Map<Int, Int> by AttachmentDelegate(bEntity, NetworkAttachments.VIEW_SLOT_MAP.get())

    override val root: BlockEntity?
        get() = rootPos?.let { bEntity.level?.getBlockEntity(it, BlockEntities.DISK_DRIVE.get())?.getOrNull() }

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
}