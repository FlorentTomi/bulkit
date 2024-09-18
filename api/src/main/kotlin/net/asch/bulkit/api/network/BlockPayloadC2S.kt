package net.asch.bulkit.api.network

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.Level
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.network.registration.PayloadRegistrar

abstract class BlockPayloadC2S(val bPos: BlockPos) :
    CustomPacketPayload {
    fun send() = PacketDistributor.sendToServer(this)

    private fun <T : BlockPayloadC2S> handlePayload(payload: T, context: IPayloadContext) {
        if (payload.type() != type()) {
            return
        }

        context.enqueueWork {
            val level = context.player().level()
            handleBlock(level, context.player().direction.opposite)
        }
    }

    protected abstract fun handleBlock(level: Level, direction: Direction)

    companion object {
        fun <T : BlockPayloadC2S> register(
            registrar: PayloadRegistrar,
            type: CustomPacketPayload.Type<T>,
            streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
        ) {
            registrar.playToServer(type, streamCodec, ::handle)
        }

        private fun <T : CustomPacketPayload> handle(payload: T, context: IPayloadContext) {
            if (payload is BlockPayloadC2S) {
                payload.handlePayload(payload, context)
            }
        }
    }
}