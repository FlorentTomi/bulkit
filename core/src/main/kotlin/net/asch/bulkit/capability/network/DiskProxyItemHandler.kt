package net.asch.bulkit.capability.network

import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler

class DiskProxyItemHandler(
    size: Int, diskHandler: IItemHandler, slotTransform: ISlotTransform
) : DiskProxyResourceHandler<IItemHandler>(size, diskHandler, slotTransform, Capabilities.ItemHandler.ITEM),
    IItemHandler {
    constructor(size: Int, diskHandler: IItemHandler) : this(size, diskHandler, UNIT_SLOT_TRANSFORM)

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