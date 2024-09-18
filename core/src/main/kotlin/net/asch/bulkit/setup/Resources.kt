package net.asch.bulkit.setup

import net.asch.bulkit.api.block.BlockStates
import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.BulkItCapabilities
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
    val item =
        Resources.REGISTER.registerResourceType(ResourceType.Builder<Item>("item").registry(BuiltInRegistries.ITEM)
            .registerCapabilities { event, resourceType, diskDrive, networkView ->
                event.registerItem(Capabilities.ItemHandler.ITEM, DiskItemHandler::create, resourceType.disk)

                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, diskDrive) { bEntity, ctx ->
                    BulkItCapabilities.Network.getDriveDiskHandler(bEntity, ctx)?.let {
                        DiskProxyItemHandler(it.slots, it)
                    }
                }

                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, networkView) { bEntity, _ ->
                    val link = BulkItCapabilities.Network.getLink(bEntity)
                    link?.let {
                        val root = it.root
                        if (root != null) {
                            val size = bEntity.blockState.getValue(BlockStates.NETWORK_VIEW_SIZE)
                            DiskProxyItemHandler(size, root, link::getRootSlot)
                        } else {
                            null
                        }
                    }
                }
            })

    val fluid =
        Resources.REGISTER.registerResourceType(ResourceType.Builder<Fluid>("fluid").registry(BuiltInRegistries.FLUID)
            .registerCapabilities { event, resourceType, diskDrive, networkView ->
                event.registerItem(Capabilities.FluidHandler.ITEM, DiskFluidHandler::create, resourceType.disk)

                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, diskDrive) { bEntity, ctx ->
                    BulkItCapabilities.Network.getDriveDiskHandler(bEntity, ctx)?.let {
                        DiskProxyFluidHandler(it.slots, it)
                    }
                }

                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, networkView) { bEntity, _ ->
                    val link = BulkItCapabilities.Network.getLink(bEntity)
                    link?.let {
                        val root = it.root
                        if (root != null) {
                            val size = bEntity.blockState.getValue(BlockStates.NETWORK_VIEW_SIZE)
                            DiskProxyFluidHandler(size, root, link::getRootSlot)
                        } else {
                            null
                        }
                    }
                }
            })
}