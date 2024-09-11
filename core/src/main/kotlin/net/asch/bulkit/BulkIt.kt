package net.asch.bulkit

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.resource.DeferredResources
import net.asch.bulkit.api.resource.ResourceType
import net.asch.bulkit.api.setup.DiskDataComponents
import net.asch.bulkit.api.setup.Disks
import net.asch.bulkit.setup.CreativeModTabs
import net.asch.bulkit.setup.Resources
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(BulkIt.ID)
object BulkIt {
    const val ID = BulkItApi.ID
    val LOGGER: Logger = LogManager.getLogger()

    val RESOURCES = Resources()

    init {
        val modBus = MOD_BUS
        modBus.addListener(NewRegistryEvent::class.java, DeferredResources::onNewRegistry)
        modBus.addListener(FMLLoadCompleteEvent::class.java, ::onLoadComplete)
        modBus.addListener(RegisterCapabilitiesEvent::class.java, ::onRegisterCapability)

        register(modBus)
    }

    private fun register(modBus: IEventBus) {
        net.asch.bulkit.api.setup.Resources.register(modBus)
        Disks.register(modBus)
        DiskDataComponents.register(modBus)

        CreativeModTabs.register(modBus)
    }

    private fun onLoadComplete(event: FMLLoadCompleteEvent) {
        val registeredResources = DeferredResources.registeredResourceTypes()
        val msg = "registered resources=[${
            registeredResources.asSequence().map(ResourceType<*>::name).joinToString(",")
        }]"

        LOGGER.info(msg)
    }

    private fun onRegisterCapability(event: RegisterCapabilitiesEvent) {
        DeferredResources.registeredResourceTypes().forEach { resourceType ->
            resourceType.registerCapabilities(event)
        }
    }
}