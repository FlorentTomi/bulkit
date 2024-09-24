package net.asch.bulkit.api.setup

import com.mojang.serialization.Codec
import net.asch.bulkit.api.BulkItApi
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {
    val REGISTER: DeferredRegister.DataComponents = DeferredRegister.createDataComponents("${BulkItApi.ID}.mod")

    val MULTIPLIER: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        REGISTER.registerComponentType("multiplier") {
            DataComponentType.builder<Int>().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
        }

    val CREATIVE: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        REGISTER.registerComponentType("creative") {
            DataComponentType.builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        }

    fun register(modBus: IEventBus) = DiskDataComponents.REGISTER.register(modBus)
}