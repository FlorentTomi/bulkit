package net.asch.bulkit.network.network

import net.asch.bulkit.BulkIt
import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.network.BlockPayloadC2S
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object NetworkPayloads {
    fun register(registrar: PayloadRegistrar) {
        BlockPayloadC2S.register(registrar, Link.TYPE, Link.STREAM_CODEC)
        BlockPayloadC2S.register(registrar, Unlink.TYPE, Unlink.STREAM_CODEC)
    }

    fun link(bPos: BlockPos, rootPos: BlockPos) = Link(bPos, rootPos).send()
    fun unlink(bPos: BlockPos) = Unlink(bPos).send()

    private class Link(bPos: BlockPos, val rootPos: BlockPos) : BlockPayloadC2S(bPos) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleBlock(level: Level, direction: Direction) {
            if (!level.getBlockState(rootPos).`is`(BulkIt.BLOCKS.diskDrive)) {
                return
            }

            val cap = level.getCapability(BulkItCapabilities.Network.LINK, bPos, Unit)
            cap?.linkTo(rootPos)
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<Link>(ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "network_link"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Link> = StreamCodec.composite(
                BlockPos.STREAM_CODEC, Link::bPos, BlockPos.STREAM_CODEC, Link::rootPos, NetworkPayloads::Link
            )
        }
    }

    private class Unlink(bPos: BlockPos) : BlockPayloadC2S(bPos) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleBlock(level: Level, direction: Direction) {
            val cap = level.getCapability(BulkItCapabilities.Network.LINK, bPos, Unit)
            cap?.linkTo(null)
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<Unlink>(ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "network_unlink"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Unlink> = StreamCodec.composite(
                BlockPos.STREAM_CODEC, Unlink::bPos, NetworkPayloads::Unlink
            )
        }
    }
}