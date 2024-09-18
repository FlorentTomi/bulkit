package net.asch.builkit.mekanism.capability.network

import mekanism.api.Action
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.gas.IGasHandler
import mekanism.common.capabilities.Capabilities
import net.asch.bulkit.api.capability.network.DiskProxyResourceHandler
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.items.IItemHandler

class DiskProxyGasHandler(size: Int, diskHandler: IItemHandler, slotTransform: ISlotTransform) :
    DiskProxyResourceHandler<IGasHandler>(
        size, diskHandler, slotTransform, Capabilities.GAS.item
    ), IGasHandler {
    constructor(size: Int, diskHandler: IItemHandler) : this(size, diskHandler, UNIT_SLOT_TRANSFORM)

    override fun getTanks(): Int = size

    override fun getChemicalInTank(tank: Int): GasStack =
        invokeDisk(tank, 0, GasStack.EMPTY, IGasHandler::getChemicalInTank)

    override fun setChemicalInTank(tank: Int, stack: GasStack) =
        invokeDisk(tank, 0, stack, Unit, IGasHandler::setChemicalInTank)

    override fun getTankCapacity(tank: Int): Long =
        invokeDisk(tank, 0, FluidType.BUCKET_VOLUME.toLong(), IGasHandler::getTankCapacity)

    override fun isValid(tank: Int, stack: GasStack): Boolean = invokeDisk(tank, 0, stack, false, IGasHandler::isValid)

    override fun insertChemical(tank: Int, stack: GasStack, action: Action): GasStack =
        invokeDisk(tank, 0, stack, action, GasStack.EMPTY, IGasHandler::insertChemical)

    override fun extractChemical(tank: Int, amount: Long, action: Action): GasStack =
        invokeDisk(tank, 0, amount, action, GasStack.EMPTY, IGasHandler::extractChemical)
}