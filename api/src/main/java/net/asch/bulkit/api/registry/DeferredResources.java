package net.asch.bulkit.api.registry;

import net.asch.bulkit.api.BulkIt;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Collection;

public class DeferredResources extends DeferredRegister<ResourceType<?>> {
    private static final ResourceKey<Registry<ResourceType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(BulkIt.location("resource_types"));
    public static final Registry<ResourceType<?>> REGISTRY = new RegistryBuilder<ResourceType<?>>(REGISTRY_KEY).sync(true).create();

    public DeferredResources(String namespace) {
        super(REGISTRY_KEY, namespace);
    }

    public <T> DeferredHolder<ResourceType<?>, ResourceType<T>> registerResourceType(ResourceType.Builder<T> builder) {
        return register(builder.key, builder);
    }

    public static void register(NewRegistryEvent event) {
        event.register(REGISTRY);
    }

    public static Collection<ResourceType<?>> registeredResources() {
        return REGISTRY.stream().toList();
    }
}
