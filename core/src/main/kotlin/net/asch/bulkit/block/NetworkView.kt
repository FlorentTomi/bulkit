package net.asch.bulkit.block

import net.asch.bulkit.api.block.BlockStates
import net.asch.bulkit.api.setup.NetworkConfiguratorDataComponents
import net.asch.bulkit.item.NetworkConfigurator
import net.asch.bulkit.network.network.NetworkPayloads
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult

class NetworkView(size: Int, properties: Properties) : Block(properties) {
    init {
        registerDefaultState(stateDefinition.any().setValue(BlockStates.NETWORK_VIEW_SIZE, size))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStates.NETWORK_VIEW_SIZE)
        super.createBlockStateDefinition(builder)
    }

    override fun useItemOn(
        pStack: ItemStack,
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHand: InteractionHand,
        pHitResult: BlockHitResult
    ): ItemInteractionResult {
        if (pStack.item is NetworkConfigurator) {
            return useConfiguratorOn(pStack, pPos)
        }

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult)
    }

    private fun useConfiguratorOn(configurator: ItemStack, bPos: BlockPos): ItemInteractionResult {
        val rootPos = configurator.get(NetworkConfiguratorDataComponents.ROOT_POS)
        if (rootPos == null) {
            NetworkPayloads.unlink(bPos)
        } else {
            NetworkPayloads.link(bPos, rootPos)
        }

        return ItemInteractionResult.CONSUME
    }

    companion object {
        val PROPERTIES: Properties =
            Properties.of().destroyTime(0.5f).explosionResistance(1200.0f).sound(SoundType.METAL)
                .pushReaction(PushReaction.BLOCK)
    }
}