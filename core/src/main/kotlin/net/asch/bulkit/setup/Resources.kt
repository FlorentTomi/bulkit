package net.asch.bulkit.setup

import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.Resources
import net.asch.bulkit.capability.disk.DiskFluidHandler
import net.asch.bulkit.capability.disk.DiskItemHandler
import net.asch.bulkit.capability.network.DiskProxyFluidHandler
import net.asch.bulkit.capability.network.DiskProxyItemHandler
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.capabilities.Capabilities

class Resources {
    val item = Resources.REGISTER.registerResourceType(
        ResourceType.Builder<Item>("item").registry(BuiltInRegistries.ITEM)
            .registerDiskCapabilities(Capabilities.ItemHandler.ITEM, ::DiskItemHandler)
            .registerDiskDriveCapabilities(Capabilities.ItemHandler.BLOCK, ::DiskProxyItemHandler)
            .registerNetworkViewCapabilities(Capabilities.ItemHandler.BLOCK, ::DiskProxyItemHandler)
    )

    val fluid = Resources.REGISTER.registerResourceType(
        ResourceType.Builder<Fluid>("fluid").registry(BuiltInRegistries.FLUID)
            .registerDiskCapabilities(Capabilities.FluidHandler.ITEM, ::DiskFluidHandler)
            .registerDiskDriveCapabilities(Capabilities.FluidHandler.BLOCK, ::DiskProxyFluidHandler)
            .registerNetworkViewCapabilities(Capabilities.FluidHandler.BLOCK, ::DiskProxyFluidHandler)
    )
}