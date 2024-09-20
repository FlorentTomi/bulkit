package net.asch.bulkit.capability.network

import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler

class DiskProxyFluidHandler(bEntity: BlockEntity, ctx: Direction) : DiskProxyResourceHandler<IFluidHandlerItem>(
    bEntity, Capabilities.FluidHandler.ITEM
), IFluidHandler {
    override fun getTanks(): Int = size

    override fun getFluidInTank(tank: Int): FluidStack =
        invokeDisk(tank, 0, FluidStack.EMPTY, IFluidHandlerItem::getFluidInTank)

    override fun getTankCapacity(tank: Int): Int =
        invokeDisk(tank, 0, FluidType.BUCKET_VOLUME, IFluidHandlerItem::getTankCapacity)

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean =
        invokeDisk(tank, 0, stack, false, IFluidHandlerItem::isFluidValid)

    override fun fill(stack: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (stack.isEmpty) {
            return 0
        }

        val toFill = stack.copy()
        var filled = 0
        for (tank in 0 until tanks) {
            val currentFilled = invokeDisk(tank, toFill, action, 0, IFluidHandlerItem::fill)
            if (currentFilled > 0) {
                toFill.shrink(currentFilled)
                filled += currentFilled
                if (toFill.isEmpty) {
                    break
                }
            }
        }

        return filled
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (resource.isEmpty) {
            return FluidStack.EMPTY
        }

        val toDrain = resource.copy()
        var drained = 0
        for (tank in 0 until tanks) {
            val currentDrained = invokeDisk(tank, toDrain, action, FluidStack.EMPTY, IFluidHandlerItem::drain)
            if (!currentDrained.isEmpty) {
                toDrain.shrink(currentDrained.amount)
                drained += currentDrained.amount
                if (toDrain.isEmpty) {
                    break
                }
            }
        }

        return if (drained == 0) FluidStack.EMPTY else resource.copyWithAmount(drained)
    }

    override fun drain(amount: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (amount == 0) {
            return FluidStack.EMPTY
        }

        for (tank in 0 until tanks) {
            val resource = invokeDisk(tank, 0, FluidStack.EMPTY, IFluidHandlerItem::getFluidInTank)
            if (!resource.isEmpty) {
                return drain(resource.copyWithAmount(amount), action)
            }
        }

        return FluidStack.EMPTY
    }
}