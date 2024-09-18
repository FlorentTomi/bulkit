package net.asch.bulkit.api.network

import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import kotlin.reflect.KClass

abstract class ItemPayloadC2S<I : Item>(private val clazz: KClass<*>, val usedHand: InteractionHand) :
    CustomPacketPayload {
    fun send() = PacketDistributor.sendToServer(this)

    private fun <T : ItemPayloadC2S<*>> handlePayload(payload: T, context: IPayloadContext) {
        if (payload.type() != type()) {
            return
        }

        context.enqueueWork {
            val player = context.player()
            val stack = player.getItemInHand(usedHand)
            if (stack.item.javaClass == clazz.java) {
                handleItem(stack)
            }
        }
    }

    protected abstract fun handleItem(stack: ItemStack)

    companion object {
        val INTERACTION_HAND_CODEC: StreamCodec<ByteBuf, InteractionHand> =
            ByteBufCodecs.INT.map({ i: Int -> InteractionHand.entries[i] }) { h: InteractionHand -> h.ordinal }

        fun <I : Item, T : ItemPayloadC2S<I>> register(
            registrar: PayloadRegistrar,
            type: CustomPacketPayload.Type<T>,
            streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
        ) {
            registrar.playToServer(type, streamCodec, ::handle)
        }

        private fun <T : CustomPacketPayload> handle(payload: T, context: IPayloadContext) {
            if (payload is ItemPayloadC2S<*>) {
                payload.handlePayload(payload, context)
            }
        }
    }
}