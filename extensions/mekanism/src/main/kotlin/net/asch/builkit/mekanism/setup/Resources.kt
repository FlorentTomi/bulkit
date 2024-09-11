package net.asch.builkit.mekanism.setup

import mekanism.api.MekanismAPI
import mekanism.api.chemical.gas.Gas
import mekanism.common.capabilities.Capabilities
import net.asch.builkit.mekanism.capability.disk.DiskGasHandler
import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.Resources

class Resources {
    val gas = Resources.REGISTER.registerResourceType(ResourceType.Builder<Gas>("gas_radioactive")
        .registry(MekanismAPI.GAS_REGISTRY).registerDiskCapabilities { event, disk ->
            event.registerItem(Capabilities.GAS.item, DiskGasHandler::create, disk)
        })

    val gasNonRadioactive = Resources.REGISTER.registerResourceType(ResourceType.Builder<Gas>("gas_non_radioactive")
        .registry(MekanismAPI.GAS_REGISTRY).registerDiskCapabilities { event, disk ->
            event.registerItem(Capabilities.GAS.item, DiskGasHandler::createNonRadioactive, disk)
        })
}