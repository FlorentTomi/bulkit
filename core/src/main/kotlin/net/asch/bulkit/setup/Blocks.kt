package net.asch.bulkit.setup

import net.asch.bulkit.api.block.BlockStates
import net.asch.bulkit.api.setup.Blocks
import net.asch.bulkit.block.DiskDrive
import net.asch.bulkit.block.NetworkView
import net.neoforged.neoforge.registries.DeferredBlock

class Blocks {
    val diskDrive: DeferredBlock<DiskDrive> = Blocks.registerBlock("disk_drive", DiskDrive.PROPERTIES, ::DiskDrive)
    val networkViews: Collection<DeferredBlock<NetworkView>> = BlockStates.NETWORK_VIEW_SIZES.map { size ->
        Blocks.registerBlock("network_view_$size", NetworkView.PROPERTIES) { props -> NetworkView(size, props) }
    }
}