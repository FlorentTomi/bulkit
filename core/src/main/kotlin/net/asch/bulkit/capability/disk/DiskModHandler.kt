package net.asch.bulkit.capability.disk

import net.asch.bulkit.api.item.BaseMod
import net.asch.bulkit.api.setup.DiskDataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ComponentItemHandler

class DiskModHandler(disk: ItemStack, ctx: Unit) : ComponentItemHandler(disk, DiskDataComponents.MODS.get(), 4) {
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean =
        super.isItemValid(slot, stack) && (stack.isEmpty || stack.item is BaseMod)
}