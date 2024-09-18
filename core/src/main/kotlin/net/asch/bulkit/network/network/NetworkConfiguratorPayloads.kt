package net.asch.bulkit.network.network

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.network.ItemPayloadC2S
import net.asch.bulkit.api.setup.NetworkConfiguratorDataComponents
import net.asch.bulkit.item.NetworkConfigurator
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object NetworkConfiguratorPayloads {
    fun register(registrar: PayloadRegistrar) {
        ItemPayloadC2S.register(registrar, SetRootPosition.TYPE, SetRootPosition.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, ClearRootPosition.TYPE, ClearRootPosition.STREAM_CODEC)
    }

    fun setRootPosition(usedHand: InteractionHand, bPos: BlockPos) = SetRootPosition(usedHand, bPos).send()
    fun clearRootPosition(usedHand: InteractionHand) = ClearRootPosition(usedHand).send()

    private class SetRootPosition(usedHand: InteractionHand, val bPos: BlockPos) :
        ItemPayloadC2S<NetworkConfigurator>(NetworkConfigurator::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            stack.set(NetworkConfiguratorDataComponents.ROOT_POS, bPos)
        }

        companion object {
            val TYPE = CustomPacketPayload.Type<SetRootPosition>(
                ResourceLocation.fromNamespaceAndPath(
                    BulkItApi.ID, "network_configurator_set_root_pos"
                )
            )
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetRootPosition> = StreamCodec.composite(
                INTERACTION_HAND_CODEC,
                SetRootPosition::usedHand,
                BlockPos.STREAM_CODEC,
                SetRootPosition::bPos,
                NetworkConfiguratorPayloads::SetRootPosition
            )
        }
    }

    private class ClearRootPosition(usedHand: InteractionHand) :
        ItemPayloadC2S<NetworkConfigurator>(NetworkConfigurator::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            stack.remove(NetworkConfiguratorDataComponents.ROOT_POS)
        }

        companion object {
            val TYPE = CustomPacketPayload.Type<ClearRootPosition>(
                ResourceLocation.fromNamespaceAndPath(
                    BulkItApi.ID, "network_configurator_clear_root_pos"
                )
            )
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ClearRootPosition> = StreamCodec.composite(
                INTERACTION_HAND_CODEC, ClearRootPosition::usedHand, NetworkConfiguratorPayloads::ClearRootPosition
            )
        }
    }
}