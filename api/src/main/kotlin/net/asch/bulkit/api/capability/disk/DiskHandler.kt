package net.asch.bulkit.api.capability.disk

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.kotlin.delegate.ComponentDelegate
import net.asch.bulkit.api.setup.DiskDataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.capabilities.ItemCapability

class DiskHandler(disk: ItemStack) {
    var amountL: Long by ComponentDelegate(disk, DiskDataComponents.AMOUNT.get())
    var isLocked: Boolean by ComponentDelegate(disk, DiskDataComponents.LOCKED.get())
    var isVoidExcess: Boolean by ComponentDelegate(disk, DiskDataComponents.VOID_EXCESS.get())
    private val mods: ItemContainerContents by ComponentDelegate(disk, DiskDataComponents.MODS.get())

    var amountI: Int
        get() = amountL.toInt()
        set(value) {
            amountL = value.toLong()
        }

    fun getMultiplier(defaultMultiplier: Int): Int {
        var multiplier = 1
        var hasDowngrade = false

        val mods = mods ?: return multiplier
        for (slot in 0 until mods.slots) {
            val mod = mods.getStackInSlot(slot)
            val modItem = mod.item
//            if (modItem is CapacityUpgradeMod) {
//                multiplier *= modItem.multiplier
//            } else if (modItem is CapacityDowngradeMod) {
//                hasDowngrade = true
//            }
        }

        if (!hasDowngrade) {
            multiplier *= defaultMultiplier
        }

        return multiplier
    }

    companion object {
        val CAPABILITY: ItemCapability<DiskHandler, Void?> = ItemCapability.createVoid(
            ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "disk_handler"), DiskHandler::class.java
        )

        fun create(stack: ItemStack, ctx: Void?): DiskHandler = DiskHandler(stack)
    }
}