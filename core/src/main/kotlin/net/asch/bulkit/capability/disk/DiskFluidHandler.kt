package net.asch.bulkit.capability.disk

import net.asch.bulkit.BulkIt
import net.asch.bulkit.api.capability.disk.IDiskHandler
import net.asch.bulkit.api.data.ResourceIdentifier
import net.asch.bulkit.api.kotlin.delegate.NullableComponentDelegate
import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.asch.bulkit.kotlin.extension.identifier
import net.asch.bulkit.kotlin.extension.stack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class DiskFluidHandler(private val disk: ItemStack, ctx: Void?) : IFluidHandlerItem {
    private val resourceType: ResourceType<Fluid> = BulkIt.RESOURCES.fluid.get()
    private var id: ResourceIdentifier<Fluid>? by NullableComponentDelegate(disk, resourceType.id)
    private val diskHandler: IDiskHandler = disk.getCapability(BulkItCapabilities.Disk.RESOURCE, Unit)!!

    private val capacity: Int
        get() = capacity(diskHandler)

    override fun getTanks(): Int = 1
    override fun getFluidInTank(tank: Int): FluidStack = toStack()
    override fun getTankCapacity(tank: Int): Int = minOf(FluidType.BUCKET_VOLUME, capacity)
    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = (id == null) || (id == stack.identifier())

    override fun getContainer(): ItemStack = disk

    override fun fill(stack: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (stack.isEmpty) {
            return 0
        }

        if (!isFluidValid(0, stack)) {
            return 0
        }

        val remainingCapacity = capacity - diskHandler.amountI
        val amountToInsert = if (!diskHandler.isVoidExcess) minOf(remainingCapacity, stack.amount) else stack.amount

        if (action.execute()) {
            if (id == null) {
                id = stack.identifier()
            }

            diskHandler.amountI = minOf(capacity, diskHandler.amountI + amountToInsert)
        }

        return amountToInsert
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        return if (id == resource.identifier()) {
            drain(resource.amount, action)
        } else {
            FluidStack.EMPTY
        }
    }

    override fun drain(amount: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (amount == 0) {
            return FluidStack.EMPTY
        }

        if (id == null || diskHandler.amountI == 0) {
            return FluidStack.EMPTY
        }

        val toExtract = minOf(amount, FluidType.BUCKET_VOLUME)
        if (diskHandler.amountI <= toExtract) {
            val existing = toStack()
            if (action.execute()) {
                if (!diskHandler.isLocked) {
                    id = null
                }

                diskHandler.amountI = 0
            }

            return existing
        }

        if (action.execute()) {
            diskHandler.amountI -= toExtract
        }

        return toStack(toExtract)
    }

    private fun toStack(amount: Int): FluidStack = id?.stack(amount) ?: FluidStack.EMPTY
    private fun toStack(): FluidStack = toStack(diskHandler.amountI)

    companion object {
        private const val DEFAULT_CAPACITY_MULTIPLIER: Int = 32

        fun capacity(diskHandler: IDiskHandler): Int =
            (FluidType.BUCKET_VOLUME * diskHandler.mods.getMultiplier(DEFAULT_CAPACITY_MULTIPLIER))
    }
}