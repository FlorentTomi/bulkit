package net.asch.bulkit.api.setup

import com.mojang.serialization.Codec
import net.asch.bulkit.api.BulkItApi
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object DiskDataComponents {
    val REGISTER: DeferredRegister.DataComponents = DeferredRegister.createDataComponents("${BulkItApi.ID}.disk")

    val AMOUNT: DeferredHolder<DataComponentType<*>, DataComponentType<Long>> =
        REGISTER.registerComponentType("amount") {
            DataComponentType.builder<Long>().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
        }

    val LOCKED: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        REGISTER.registerComponentType("locked") {
            DataComponentType.builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        }

    val VOID_EXCESS: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        REGISTER.registerComponentType("void_excess") {
            DataComponentType.builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        }

    val MODS: DeferredHolder<DataComponentType<*>, DataComponentType<ItemContainerContents>> =
        REGISTER.registerComponentType("mods") {
            DataComponentType.builder<ItemContainerContents>().persistent(ItemContainerContents.CODEC)
                .networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding()
        }

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
}