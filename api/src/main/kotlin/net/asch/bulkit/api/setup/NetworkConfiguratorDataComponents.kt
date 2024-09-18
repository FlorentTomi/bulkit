package net.asch.bulkit.api.setup

import net.asch.bulkit.api.BulkItApi
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object NetworkConfiguratorDataComponents {
    val REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents("${BulkItApi.ID}.network_configurator")

    val ROOT_POS: DeferredHolder<DataComponentType<*>, DataComponentType<BlockPos>> =
        REGISTER.registerComponentType("root_pos") {
            DataComponentType.builder<BlockPos>().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC)
        }

    fun register(modBus: IEventBus) = DiskDataComponents.REGISTER.register(modBus)
}