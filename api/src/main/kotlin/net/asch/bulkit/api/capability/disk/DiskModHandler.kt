package net.asch.bulkit.api.capability.disk

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.item.BaseMod
import net.asch.bulkit.api.setup.DiskDataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.items.ComponentItemHandler

class DiskModHandler(disk: ItemStack) : ComponentItemHandler(disk, DiskDataComponents.MODS.get(), SIZE) {
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean =
        super.isItemValid(slot, stack) && (stack.isEmpty || stack.item is BaseMod)

    companion object {
        private const val SIZE: Int = 4

        val CAPABILITY: ItemCapability<DiskModHandler, Void?> = ItemCapability.createVoid(
            ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "disk_mod_handler"), DiskModHandler::class.java
        )

        fun create(stack: ItemStack, ctx: Void?): DiskModHandler = DiskModHandler(stack)
    }
}