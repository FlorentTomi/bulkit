package net.asch.bulkit.capability.network

import net.asch.bulkit.kotlin.delegate.NullableAttachmentDelegate
import net.asch.bulkit.api.setup.NetworkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity

class NetworkHandler(blockEntity: BlockEntity, direction: Direction) {
    private val diskDrivePos: BlockPos? by NullableAttachmentDelegate(blockEntity, NetworkAttachments.VIEW_ROOT_POS)
}