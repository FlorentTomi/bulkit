package net.asch.bulkit.api

import net.asch.bulkit.api.resource.DeferredResources
import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.*
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object BulkItApi {
    const val ID: String = "bulkit"
    val LOGGER: Logger = LogManager.getLogger()

    fun register(modBus: IEventBus) {
        modBus.addListener(NewRegistryEvent::class.java, DeferredResources::onNewRegistry)
        modBus.addListener(FMLLoadCompleteEvent::class.java, ::onLoadComplete)

        Blocks.register(modBus)
        CreativeModTabs.register(modBus)
        DiskDataComponents.register(modBus)
        Disks.register(modBus)
        Items.register(modBus)
        NetworkConfiguratorDataComponents.register(modBus)
        NetworkAttachments.register(modBus)
        Resources.register(modBus)
    }

    fun location(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(ID, name)

    private fun onLoadComplete(event: FMLLoadCompleteEvent) {
        val registeredResources = DeferredResources.registeredResourceTypes()
        val msg = "registered resources=[${
            registeredResources.asSequence().map(ResourceType<*>::name).joinToString(",")
        }]"

        LOGGER.info(msg)
    }
}