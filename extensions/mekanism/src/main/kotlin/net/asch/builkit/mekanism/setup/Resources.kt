package net.asch.builkit.mekanism.setup

import mekanism.api.MekanismAPI
import mekanism.api.chemical.gas.Gas
import mekanism.common.capabilities.Capabilities
import net.asch.builkit.mekanism.capability.disk.DiskGasHandler
import net.asch.builkit.mekanism.capability.network.DiskProxyGasHandler
import net.asch.bulkit.api.block.BlockStates
import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.asch.bulkit.api.setup.Resources

class Resources {
    val gas = Resources.REGISTER.registerResourceType(ResourceType.Builder<Gas>("gas_radioactive")
        .registry(MekanismAPI.GAS_REGISTRY).registerCapabilities { event, resourceType, diskDrive, networkView ->
            event.registerItem(Capabilities.GAS.item, DiskGasHandler::create, resourceType.disk)

            event.registerBlockEntity(Capabilities.GAS.block, diskDrive) { bEntity, ctx ->
                BulkItCapabilities.Network.getDriveDiskHandler(bEntity, ctx)?.let {
                    DiskProxyGasHandler(it.slots, it)
                }
            }

            event.registerBlockEntity(Capabilities.GAS.block, networkView) { bEntity, _ ->
                val link = BulkItCapabilities.Network.getLink(bEntity)
                link?.let {
                    val root = it.root
                    if (root != null) {
                        val size = bEntity.blockState.getValue(BlockStates.NETWORK_VIEW_SIZE)
                        DiskProxyGasHandler(size, root, link::getRootSlot)
                    } else {
                        null
                    }
                }
            }
        })

    val gasNonRadioactive = Resources.REGISTER.registerResourceType(ResourceType.Builder<Gas>("gas_non_radioactive")
        .registry(MekanismAPI.GAS_REGISTRY).registerCapabilities { event, resourceType, diskDrive, networkView ->
            event.registerItem(Capabilities.GAS.item, DiskGasHandler::createNonRadioactive, resourceType.disk)

            event.registerBlockEntity(Capabilities.GAS.block, diskDrive) { bEntity, ctx ->
                BulkItCapabilities.Network.getDriveDiskHandler(bEntity, ctx)?.let {
                    DiskProxyGasHandler(it.slots, it)
                }
            }

            event.registerBlockEntity(Capabilities.GAS.block, networkView) { bEntity, _ ->
                val link = BulkItCapabilities.Network.getLink(bEntity)
                link?.let {
                    val root = it.root
                    if (root != null) {
                        val size = bEntity.blockState.getValue(BlockStates.NETWORK_VIEW_SIZE)
                        DiskProxyGasHandler(size, root, link::getRootSlot)
                    } else {
                        null
                    }
                }
            }
        })
}