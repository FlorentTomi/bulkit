package net.asch.bulkit.api.resource

import net.asch.bulkit.api.capability.disk.DiskHandler
import net.asch.bulkit.api.capability.disk.DiskModHandler
import net.asch.bulkit.api.data.ResourceIdentifier
import net.asch.bulkit.api.item.Disk
import net.asch.bulkit.api.setup.DiskDataComponents
import net.asch.bulkit.api.setup.Disks
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

class ResourceType<R> private constructor(
    val name: String,
    val id: DataComponentType<ResourceIdentifier<R>>,
    val disk: Disk,
    private val registerDiskCapabilities: IRegisterDiskCapabilities? = null
) {
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        registerDiskCapabilities?.register(event, disk)
        event.registerItem(DiskHandler.CAPABILITY, DiskHandler::create, disk)
        event.registerItem(DiskModHandler.CAPABILITY, DiskModHandler::create, disk)
    }

    class Builder<R>(private val name: String) : Supplier<ResourceType<R>> {
        val key = "resource_$name"
        private lateinit var id: Supplier<DataComponentType<ResourceIdentifier<R>>>
        private val disk: DeferredItem<Disk> = Disks.REGISTER.registerItem("disk_$name", ::Disk)
        private var registerDiskCapabilities: IRegisterDiskCapabilities? = null

        fun registry(registry: Registry<R>): Builder<R> {
            id = DiskDataComponents.REGISTER.registerComponentType("resource_id_$name") { builder ->
                builder.persistent(ResourceIdentifier.codec(registry))
                    .networkSynchronized(ResourceIdentifier.streamCodec(registry)).cacheEncoding()
            }

            return this
        }

        fun registerDiskCapabilities(fn: IRegisterDiskCapabilities): Builder<R> {
            registerDiskCapabilities = fn

            return this
        }

        override fun get(): ResourceType<R> = ResourceType(name, id.get(), disk.get(), registerDiskCapabilities)
    }

    fun interface IRegisterDiskCapabilities {
        fun register(event: RegisterCapabilitiesEvent, disk: Disk)
    }
}