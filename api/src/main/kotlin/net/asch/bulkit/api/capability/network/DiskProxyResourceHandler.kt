package net.asch.bulkit.api.capability.network

import net.asch.bulkit.api.BlockStates
import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler.ISlotTransform
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.items.IItemHandler

abstract class DiskProxyResourceHandler<T>(
    private val bEntity: BlockEntity, private val cap: ItemCapability<T, Void?>
) {
    private val link: INetworkLink?
        get() = bEntity.level?.getCapability(
            BulkItCapabilities.Network.LINK, bEntity.blockPos, bEntity.blockState, bEntity, Unit
        )

    private val diskHandler: IItemHandler?
        get() = bEntity.level?.getCapability(
            BulkItCapabilities.Network.DRIVE_DISK_HANDLER, link?.rootPos ?: bEntity.blockPos, Unit
        )

    val size: Int = bEntity.blockState.getValue(BlockStates.Network.VIEW_SIZE)

    private fun <R> transformAndInvoke(slot: Int, errorVal: R, fn: (T?) -> R?): R {
        val transformedSlot = link?.getRootSlot(slot) ?: slot
        if (transformedSlot == INVALID_SLOT) {
            return errorVal
        }

        return if (transformedSlot in 0 until size) {
            fn(diskHandler?.getStackInSlot(transformedSlot)?.getCapability(cap)) ?: errorVal
        } else {
            errorVal
        }
    }

    fun <R> invokeDisk(slot: Int, errorVal: R, fn: T.() -> R): R = transformAndInvoke(slot, errorVal) { it?.fn() }

    fun <R, P0> invokeDisk(slot: Int, p0: P0, errorVal: R, fn: T.(P0) -> R): R =
        transformAndInvoke(slot, errorVal) { it?.fn(p0) }

    fun <R, P0, P1> invokeDisk(slot: Int, p0: P0, p1: P1, errorVal: R, fn: T.(P0, P1) -> R): R =
        transformAndInvoke(slot, errorVal) { it?.fn(p0, p1) }

    fun <R, P0, P1, P2> invokeDisk(slot: Int, p0: P0, p1: P1, p2: P2, errorVal: R, fn: T.(P0, P1, P2) -> R): R =
        transformAndInvoke(slot, errorVal) { it?.fn(p0, p1, p2) }

    fun interface ISlotTransform {
        fun transform(slot: Int): Int
    }

    fun interface IDiskProxyResourceHandlerProvider<T : DiskProxyResourceHandler<*>> {
        fun create(diskHandler: IItemHandler): T
    }

    companion object {
        const val INVALID_SLOT: Int = -1
        val UNIT_SLOT_TRANSFORM: ISlotTransform = ISlotTransform { slot: Int -> slot }

        fun <T : DiskProxyResourceHandler<*>> create(
            bEntity: BlockEntity, provider: IDiskProxyResourceHandlerProvider<T>
        ) = bEntity.level?.getCapability(
            BulkItCapabilities.Network.DRIVE_DISK_HANDLER, bEntity.blockPos, bEntity.blockState, bEntity, Unit
        )?.let { provider.create(it) }
    }
}