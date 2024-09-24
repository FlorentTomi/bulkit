package net.asch.bulkit.api.setup

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.item.Disk
import net.asch.bulkit.api.item.Mod
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters
import net.minecraft.world.item.CreativeModeTab.Output
import net.minecraft.world.item.Item.Properties
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object Disks {
    private val REGISTER: DeferredRegister.Items = DeferredRegister.createItems("${BulkItApi.ID}.disk")

    val MODS: List<DeferredItem<Mod>> =
        Mod.UPGRADE_MULTIPLIERS.map { multiplier ->
            registerMod("upgrade_$multiplier", Properties().component(ModDataComponents.MULTIPLIER, multiplier))
        } + registerMod(
            "downgrade", Properties().component(ModDataComponents.MULTIPLIER, -1)
        )

    fun registerDisk(resourceName: String): DeferredItem<Disk> = REGISTER.registerItem("disk_$resourceName", ::Disk)
    fun registerMod(modName: String, properties: Properties): DeferredItem<Mod> =
        REGISTER.registerItem("mod_$modName", ::Mod)

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
    fun display(params: ItemDisplayParameters, output: Output) {
        REGISTER.entries.forEach { output.accept(it.get()) }
    }
}