package net.asch.bulkit.capability.disk

import net.asch.bulkit.api.capability.disk.IDiskHandler
import net.asch.bulkit.api.item.BaseMod
import net.asch.bulkit.api.kotlin.delegate.ComponentDelegate
import net.asch.bulkit.api.setup.DiskDataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

class DiskHandler(disk: ItemStack, ctx: Unit) : IDiskHandler {
    override var amountL: Long by ComponentDelegate(disk, DiskDataComponents.AMOUNT.get())
    override var isLocked: Boolean by ComponentDelegate(disk, DiskDataComponents.LOCKED.get())
    override var isVoidExcess: Boolean by ComponentDelegate(disk, DiskDataComponents.VOID_EXCESS.get())
    private val mods: ItemContainerContents by ComponentDelegate(disk, DiskDataComponents.MODS.get())

    override var amountI: Int
        get() = amountL.toInt()
        set(value) {
            amountL = value.toLong()
        }

    override fun getMultiplier(defaultMultiplier: Int): Int {
        var multiplier = 1
        var hasDowngrade = false

        val mods = mods
        for (slot in 0 until mods.slots) {
            val mod = mods.getStackInSlot(slot)
            val modItem = mod.item
            if (modItem !is BaseMod) {
                continue
            }

            when (modItem.type) {
                BaseMod.Types.Upgrade -> {} // multiplier *= modItem
                BaseMod.Types.Downgrade -> hasDowngrade = true
            }
        }

        if (!hasDowngrade) {
            multiplier *= defaultMultiplier
        }

        return multiplier
    }
}