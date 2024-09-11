package net.asch.bulkit.api.resource

import net.asch.bulkit.api.BulkItApi
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

class DeferredResources private constructor(modId: String) : DeferredRegister<ResourceType<*>>(REGISTRY_KEY, modId) {
    fun <R> registerResourceType(builder: ResourceType.Builder<R>): DeferredHolder<ResourceType<*>, ResourceType<R>> =
        register(builder.key, builder)

    companion object {
        private val REGISTRY_KEY: ResourceKey<Registry<ResourceType<*>>> =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "resource_types"))
        private val REGISTRY: Registry<ResourceType<*>> = RegistryBuilder(REGISTRY_KEY).sync(true).create()

        fun onNewRegistry(event: NewRegistryEvent) = event.register(REGISTRY)
        fun create(modId: String): DeferredResources = DeferredResources(modId)
        fun registeredResourceTypes(): Collection<ResourceType<*>> = REGISTRY.stream().toList()
    }
}