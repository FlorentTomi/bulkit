package net.asch.bulkit

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.resource.DeferredResources
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.asch.bulkit.api.setup.NetworkAttachments
import net.asch.bulkit.capability.disk.DiskHandler
import net.asch.bulkit.api.capability.disk.DiskModHandler
import net.asch.bulkit.capability.network.NetworkLink
import net.asch.bulkit.network.disk.DiskUpdatePayloads
import net.asch.bulkit.network.network.NetworkConfiguratorPayloads
import net.asch.bulkit.network.network.NetworkPayloads
import net.asch.bulkit.setup.*
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(BulkIt.ID)
object BulkIt {
    const val ID = BulkItApi.ID

    val ATTACHMENTS = Attachments()
    val BLOCKS = Blocks()
    val ITEMS = Items()
    val RESOURCES = Resources()

    init {
        val modBus = MOD_BUS
        register(modBus)
    }

    private fun register(modBus: IEventBus) {
        BulkItApi.register(modBus)
        modBus.addListener(RegisterPayloadHandlersEvent::class.java, ::onRegisterPayloads)
        modBus.addListener(RegisterCapabilitiesEvent::class.java, ::onRegisterCapability)
    }

    private fun onRegisterCapability(event: RegisterCapabilitiesEvent) {
        DeferredResources.registeredResourceTypes().forEach { resourceType ->
            resourceType.registerCapabilities(event, BlockEntities.DISK_DRIVE.get(), BlockEntities.NETWORK_VIEW.get())

            event.registerItem(
                BulkItCapabilities.Disk.RESOURCE, ::DiskHandler, resourceType.disk
            )

            event.registerItem(
                BulkItCapabilities.Disk.MODS, ::DiskModHandler, resourceType.disk
            )

            event.registerBlockEntity(
                BulkItCapabilities.Network.DRIVE_DISK_HANDLER, BlockEntities.DISK_DRIVE.get()
            ) { bEntity, _ -> bEntity.getData(NetworkAttachments.DISK_HANDLER) }

            event.registerBlockEntity(
                BulkItCapabilities.Network.LINK, BlockEntities.NETWORK_VIEW.get(), ::NetworkLink
            )
        }
    }

    private fun onRegisterPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar(ID).versioned("1")
        DiskUpdatePayloads.register(registrar)
        NetworkConfiguratorPayloads.register(registrar)
        NetworkPayloads.register(registrar)
    }
}