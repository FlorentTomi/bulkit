package net.asch.builkit.mekanism.capability.disk

import mekanism.api.Action
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.gas.IGasHandler
import net.asch.builkit.mekanism.BulkIt
import net.asch.builkit.mekanism.kotlin.extension.identifier
import net.asch.builkit.mekanism.kotlin.extension.stack
import net.asch.bulkit.api.capability.disk.DiskHandler
import net.asch.bulkit.api.data.ResourceIdentifier
import net.asch.bulkit.api.kotlin.delegate.NullableComponentDelegate
import net.asch.bulkit.api.resource.ResourceType
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidType

class DiskGasHandler(disk: ItemStack, private val allowRadioactive: Boolean) : IGasHandler {
    private val resourceType: ResourceType<Gas> =
        if (allowRadioactive) BulkIt.RESOURCES.gas.get() else BulkIt.RESOURCES.gasNonRadioactive.get()
    private var id: ResourceIdentifier<Gas>? by NullableComponentDelegate(disk, resourceType.id)
    private val diskHandler: DiskHandler = disk.getCapability(DiskHandler.CAPABILITY)!!

    private val capacity: Long
        get() = capacity(diskHandler)

    override fun getTanks(): Int = 1
    override fun getChemicalInTank(tank: Int): GasStack = toStack()
    override fun getTankCapacity(tank: Int): Long = minOf(FluidType.BUCKET_VOLUME.toLong(), capacity)

    override fun insertChemical(tank: Int, stack: GasStack, action: Action): GasStack {
        if (stack.isEmpty) {
            return emptyStack
        }

        if (!isValid(tank, stack)) {
            return stack
        }

        val remainingCapacity = capacity - diskHandler.amountL
        val amountToInsert = if (!diskHandler.isVoidExcess) minOf(remainingCapacity, stack.amount) else stack.amount
        if (amountToInsert == 0L) {
            return stack
        }

        if (action.execute()) {
            if (id == null) {
                id = stack.identifier()
            }

            diskHandler.amountL = (minOf(capacity, diskHandler.amountL + amountToInsert))
        }

        return if (amountToInsert == stack.amount) emptyStack else (stack.copyWithAmount(amountToInsert))
    }

    override fun extractChemical(tank: Int, amount: Long, action: Action): GasStack {
        if (diskHandler.amountL == 0L) {
            return emptyStack
        }

        if (id == null || amount == 0L) {
            return emptyStack
        }

        val toExtract = minOf(amount, FluidType.BUCKET_VOLUME.toLong())
        if (diskHandler.amountL <= toExtract) {
            val existing = toStack()
            if (action.execute()) {
                if (!diskHandler.isLocked) {
                    id = null
                }

                diskHandler.amountL = 0L
            }

            return existing
        }

        if (action.execute()) {
            diskHandler.amountL -= toExtract
        }

        return toStack(toExtract)
    }

    override fun setChemicalInTank(tank: Int, stack: GasStack) {
        if (id == null) {
            id = stack.identifier()
        }
    }

    override fun isValid(tank: Int, stack: GasStack): Boolean = allowRadioactive || !stack.isRadioactive
    private fun toStack(amount: Long): GasStack = id?.stack(amount) ?: GasStack.EMPTY
    private fun toStack(): GasStack = toStack(diskHandler.amountL)

    companion object {
        private const val DEFAULT_CAPACITY_MULTIPLIER = 32

        fun capacity(resource: DiskHandler): Long =
            (FluidType.BUCKET_VOLUME * resource.getMultiplier(DEFAULT_CAPACITY_MULTIPLIER)).toLong()

        fun create(disk: ItemStack, ctx: Void?): IGasHandler = DiskGasHandler(disk, true)
        fun createNonRadioactive(disk: ItemStack, ctx: Void?): IGasHandler = DiskGasHandler(disk, false)
    }
}