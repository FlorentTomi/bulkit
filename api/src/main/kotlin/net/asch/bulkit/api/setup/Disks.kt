package net.asch.bulkit.api.setup

import net.asch.bulkit.api.BulkItApi
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters
import net.minecraft.world.item.CreativeModeTab.Output
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object Disks {
    val REGISTER: DeferredRegister.Items = DeferredRegister.createItems("${BulkItApi.ID}.disk")

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
    fun display(params: ItemDisplayParameters, output: Output) {
        REGISTER.entries.forEach { output.accept(it.get()) }
    }
}