package net.asch.bulkit.capability.network

import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler

class DiskProxyItemHandler(bEntity: BlockEntity, ctx: Direction) :
    DiskProxyResourceHandler<IItemHandler>(bEntity, Capabilities.ItemHandler.ITEM), IItemHandler {

    override fun getSlots(): Int = size

    override fun getStackInSlot(slot: Int): ItemStack =
        invokeDisk(slot, 0, ItemStack.EMPTY, IItemHandler::getStackInSlot)

    override fun insertItem(slot: Int, toInsert: ItemStack, simulate: Boolean): ItemStack =
        invokeDisk(slot, 0, toInsert, simulate, toInsert, IItemHandler::insertItem)

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack =
        invokeDisk(slot, 0, amount, simulate, ItemStack.EMPTY, IItemHandler::extractItem)

    override fun getSlotLimit(slot: Int): Int = invokeDisk(slot, 0, 64, IItemHandler::getSlotLimit)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean =
        invokeDisk(slot, 0, stack, false, IItemHandler::isItemValid)
}