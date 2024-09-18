package net.asch.bulkit.api.setup

import net.asch.bulkit.api.BulkItApi
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters
import net.minecraft.world.item.CreativeModeTab.Output
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object Items {
    private val REGISTER: DeferredRegister.Items = DeferredRegister.createItems(BulkItApi.ID)

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
    fun <I : Item> registerItem(name: String, sup: (Item.Properties) -> I): DeferredItem<I> =
        REGISTER.registerItem(name, sup)

    fun display(params: ItemDisplayParameters, output: Output) {
        REGISTER.entries.forEach { output.accept(it.get()) }
    }
}