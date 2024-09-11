package net.asch.bulkit.setup

import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.Resources
import net.asch.bulkit.capability.disk.DiskFluidHandler
import net.asch.bulkit.capability.disk.DiskItemHandler
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.capabilities.Capabilities

class Resources {
    val item =
        Resources.REGISTER.registerResourceType(ResourceType.Builder<Item>("item").registry(BuiltInRegistries.ITEM)
            .registerDiskCapabilities { event, disk ->
                event.registerItem(Capabilities.ItemHandler.ITEM, DiskItemHandler::create, disk)
            })

    val fluid =
        Resources.REGISTER.registerResourceType(ResourceType.Builder<Fluid>("fluid").registry(BuiltInRegistries.FLUID)
            .registerDiskCapabilities { event, disk ->
                event.registerItem(Capabilities.FluidHandler.ITEM, DiskFluidHandler::create, disk)
            })
}