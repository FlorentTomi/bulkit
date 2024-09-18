package net.asch.bulkit.api.resource

import net.asch.bulkit.api.data.ResourceIdentifier
import net.asch.bulkit.api.item.Disk
import net.asch.bulkit.api.setup.DiskDataComponents
import net.asch.bulkit.api.setup.Disks
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

class ResourceType<R> private constructor(
    val name: String,
    val id: DataComponentType<ResourceIdentifier<R>>,
    val disk: Disk,
    private val registerCapabilities: IRegisterResourceCapabilities<R>? = null
) {
    fun registerCapabilities(
        event: RegisterCapabilitiesEvent,
        diskDriveEntityType: BlockEntityType<*>,
        networkViewEntityType: BlockEntityType<*>
    ) = registerCapabilities?.register(event, this, diskDriveEntityType, networkViewEntityType)

    class Builder<R>(private val name: String) : Supplier<ResourceType<R>> {
        val key = "resource_$name"
        private lateinit var id: Supplier<DataComponentType<ResourceIdentifier<R>>>
        private val disk: DeferredItem<Disk> = Disks.REGISTER.registerItem("disk_$name", ::Disk)
        private var registerCapabilities: IRegisterResourceCapabilities<R>? = null

        fun registry(registry: Registry<R>): Builder<R> {
            id = DiskDataComponents.REGISTER.registerComponentType("resource_id_$name") { builder ->
                builder.persistent(ResourceIdentifier.codec(registry))
                    .networkSynchronized(ResourceIdentifier.streamCodec(registry)).cacheEncoding()
            }

            return this
        }

        fun registerCapabilities(fn: IRegisterResourceCapabilities<R>): Builder<R> {
            registerCapabilities = fn
            return this
        }

        override fun get(): ResourceType<R> = ResourceType(name, id.get(), disk.get(), registerCapabilities)
    }

    fun interface IRegisterResourceCapabilities<R> {
        fun register(
            event: RegisterCapabilitiesEvent,
            resourceType: ResourceType<R>,
            diskDriveEntityType: BlockEntityType<*>,
            networkViewEntityType: BlockEntityType<*>
        )
    }
}