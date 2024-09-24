package net.asch.bulkit.api.setup

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.capability.disk.DiskModHandler
import net.asch.bulkit.api.capability.disk.IDiskHandler
import net.asch.bulkit.api.capability.network.INetworkLink
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.items.IItemHandler

object BulkItCapabilities {
    object Disk {
        val RESOURCE: ItemCapability<IDiskHandler, Unit> = ItemCapability.create(
            BulkItApi.location("disk_handler"), IDiskHandler::class.java, Unit::class.java
        )

        val MODS: ItemCapability<DiskModHandler, Unit> = ItemCapability.create(
            BulkItApi.location("disk_mod_handler"), DiskModHandler::class.java, Unit::class.java
        )
    }

    object Network {
        val LINK: BlockCapability<INetworkLink, Unit> =
            BlockCapability.create(BulkItApi.location("network_link"), INetworkLink::class.java, Unit::class.java)

        val DRIVE_DISK_HANDLER: BlockCapability<IItemHandler, Unit> =
            BlockCapability.create(BulkItApi.location("drive_disk_handler"), IItemHandler::class.java, Unit::class.java)
    }
}