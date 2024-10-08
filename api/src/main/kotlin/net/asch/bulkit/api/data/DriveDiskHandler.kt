package net.asch.bulkit.api.data

import net.asch.bulkit.api.item.Disk
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler

class DriveDiskHandler : ItemStackHandler(SIZE) {
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean =
        super.isItemValid(slot, stack) && (stack.item is Disk)

    companion object {
        const val SIZE: Int = 8
    }
}