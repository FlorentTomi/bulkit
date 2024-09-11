package net.asch.bulkit.setup

import net.asch.bulkit.BulkIt.ID
import net.asch.bulkit.api.setup.Disks
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object CreativeModTabs {
    private val REGISTER = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, ID)

    private val CORE = REGISTER.register(ID) { ->
        CreativeModeTab.builder().title(Component.literal("BulkIt")).build()
    }

    private val DISKS = REGISTER.register("${ID}_disks") { ->
        CreativeModeTab.builder().title(Component.literal("BulkIt - Disks")).displayItems(Disks::display).build()
    }

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
}