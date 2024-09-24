package net.asch.bulkit.capability.disk

import net.asch.bulkit.api.capability.disk.DiskModHandler
import net.asch.bulkit.api.capability.disk.IDiskHandler
import net.asch.bulkit.api.kotlin.delegate.ComponentDelegate
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.asch.bulkit.api.setup.DiskDataComponents
import net.minecraft.world.item.ItemStack

class DiskHandler(private val disk: ItemStack, ctx: Unit) : IDiskHandler {
    override var amountL: Long
        get() = if (mods.hasCreative()) Long.MAX_VALUE else disk.get(DiskDataComponents.AMOUNT)!!
        set(value) {
            if (!mods.hasCreative()) {
                disk.set(DiskDataComponents.AMOUNT, value)
            }
        }
    override var isLocked: Boolean by ComponentDelegate(disk, DiskDataComponents.LOCKED.get())
    override var isVoidExcess: Boolean by ComponentDelegate(disk, DiskDataComponents.VOID_EXCESS.get())
    override val mods: DiskModHandler = disk.getCapability(BulkItCapabilities.Disk.MODS, Unit)!!

    override var amountI: Int
        get() = amountL.toInt()
        set(value) {
            amountL = value.toLong()
        }
}