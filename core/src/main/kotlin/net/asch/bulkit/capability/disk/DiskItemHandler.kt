package net.asch.bulkit.capability.disk

import net.asch.bulkit.BulkIt
import net.asch.bulkit.api.capability.disk.IDiskHandler
import net.asch.bulkit.api.data.ResourceIdentifier
import net.asch.bulkit.api.kotlin.delegate.NullableComponentDelegate
import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.asch.bulkit.kotlin.extension.identifier
import net.asch.bulkit.kotlin.extension.stack
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class DiskItemHandler(disk: ItemStack, ctx: Void?) : IItemHandler {
    private val resourceType: ResourceType<Item> = BulkIt.RESOURCES.item.get()
    private var id: ResourceIdentifier<Item>? by NullableComponentDelegate(disk, resourceType.id)
    private val diskHandler: IDiskHandler = disk.getCapability(BulkItCapabilities.Disk.RESOURCE, Unit)!!

    private val maxStackSize: Int
        get() = id?.holder?.value()?.defaultMaxStackSize ?: 0
    private val capacity: Int
        get() = capacity(maxStackSize, diskHandler)

    override fun getSlots(): Int = 1
    override fun getStackInSlot(slot: Int): ItemStack = toStack()
    override fun getSlotLimit(slot: Int): Int = capacity
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = (id == null) || (id == stack.identifier())

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) {
            return ItemStack.EMPTY
        }

        if (!isItemValid(slot, stack)) {
            return stack
        }

        val capacity = if (id == null) capacity(stack, diskHandler) else this.capacity
        val remainingCapacity = capacity - diskHandler.amountI
        val amountToInsert = if (!diskHandler.isVoidExcess) minOf(remainingCapacity, stack.count) else stack.count
        if (amountToInsert == 0) {
            return stack
        }

        if (!simulate) {
            if (id == null) {
                id = stack.identifier()
                diskHandler.amountI = 0
            }

            diskHandler.amountI = minOf(capacity, diskHandler.amountI + amountToInsert)
        }

        return if (amountToInsert == stack.count) ItemStack.EMPTY else stack.copyWithCount(amountToInsert)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (diskHandler.amountI == 0) {
            return ItemStack.EMPTY
        }

        if (id == null || amount == 0) {
            return ItemStack.EMPTY
        }

        val toExtract = minOf(amount, maxStackSize)
        if (diskHandler.amountI <= toExtract) {
            val existing = toStack()
            if (!simulate) {
                if (!diskHandler.isLocked) {
                    id = null
                }

                diskHandler.amountI = 0
            }

            return existing
        }

        if (!simulate) {
            diskHandler.amountI -= toExtract
        }

        return toStack(toExtract)
    }

    private fun toStack(amount: Int): ItemStack = id?.stack(amount) ?: ItemStack.EMPTY
    private fun toStack(): ItemStack = toStack(diskHandler.amountI)

    companion object {
        private const val DEFAULT_CAPACITY_MULTIPLIER: Int = 8

        fun capacity(maxStackSize: Int, diskHandler: IDiskHandler): Int =
            maxStackSize * diskHandler.mods.getMultiplier(DEFAULT_CAPACITY_MULTIPLIER)

        fun capacity(stack: ItemStack, diskHandler: IDiskHandler): Int = capacity(stack.maxStackSize, diskHandler)
    }
}