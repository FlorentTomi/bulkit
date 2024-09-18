package net.asch.bulkit.api.capability.network

import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler.ISlotTransform
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.items.IItemHandler

abstract class DiskProxyResourceHandler<T>(
    val size: Int,
    private val diskHandler: IItemHandler,
    private val slotTransform: ISlotTransform,
    private val cap: ItemCapability<T, Void?>
) {
    private val internalSize: Int = diskHandler.slots

    private fun <R> transformAndInvoke(slot: Int, errorVal: R, fn: (T?) -> R?): R {
        val transformedSlot = slotTransform.transform(slot)
        if (transformedSlot == INVALID_SLOT) {
            return errorVal
        }

        return if (transformedSlot in 0 until internalSize) {
            fn(diskHandler.getStackInSlot(transformedSlot).getCapability(cap)) ?: errorVal
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

    companion object {
        const val INVALID_SLOT: Int = -1
        val UNIT_SLOT_TRANSFORM: ISlotTransform = ISlotTransform { slot: Int -> slot }
    }
}