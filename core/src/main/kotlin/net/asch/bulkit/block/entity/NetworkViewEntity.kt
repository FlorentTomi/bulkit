package net.asch.bulkit.block.entity

import net.asch.bulkit.setup.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class NetworkViewEntity(bPos: BlockPos, bState: BlockState) :
    BlockEntity(BlockEntities.NETWORK_VIEW.get(), bPos, bState)