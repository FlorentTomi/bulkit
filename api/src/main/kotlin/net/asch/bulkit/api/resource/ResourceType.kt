package net.asch.bulkit.api.resource

import net.asch.bulkit.api.data.ResourceIdentifier
import net.asch.bulkit.api.item.Disk
import net.asch.bulkit.api.resource.ResourceType.IRegisterCapability
import net.asch.bulkit.api.setup.DiskDataComponents
import net.asch.bulkit.api.setup.Disks
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ICapabilityProvider
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

class ResourceType<R> private constructor(
    val name: String,
    val id: DataComponentType<ResourceIdentifier<R>>,
    val disk: Disk,
    private val diskCaps: List<IRegisterCapability<Disk>>,
    private val diskDriveCaps: List<IRegisterCapability<BlockEntityType<*>>>,
    private val networkViewCaps: List<IRegisterCapability<BlockEntityType<*>>>
) {
    fun registerCapabilities(
        event: RegisterCapabilitiesEvent,
        diskDriveEntityType: BlockEntityType<*>,
        networkViewEntityType: BlockEntityType<*>
    ) {
        diskCaps.forEach { it.register(event, disk) }
        diskDriveCaps.forEach { it.register(event, diskDriveEntityType) }
        networkViewCaps.forEach { it.register(event, networkViewEntityType) }
    }

    class Builder<R>(private val name: String) : Supplier<ResourceType<R>> {
        val key = "resource_$name"
        private lateinit var id: Supplier<DataComponentType<ResourceIdentifier<R>>>
        private val disk: DeferredItem<Disk> = Disks.REGISTER.registerItem("disk_$name", ::Disk)

        private val diskCaps: MutableList<IRegisterCapability<Disk>> = mutableListOf()
        private val diskDriveCaps: MutableList<IRegisterCapability<BlockEntityType<*>>> = mutableListOf()
        private val networkViewCaps: MutableList<IRegisterCapability<BlockEntityType<*>>> = mutableListOf()

        fun registry(registry: Registry<R>): Builder<R> {
            id = DiskDataComponents.REGISTER.registerComponentType("resource_id_$name") { builder ->
                builder.persistent(ResourceIdentifier.codec(registry))
                    .networkSynchronized(ResourceIdentifier.streamCodec(registry)).cacheEncoding()
            }

            return this
        }

        fun <T, C : Any?> registerDiskCapabilities(
            resourceCap: ItemCapability<T, C>,
            resourceCapProvider: ICapabilityProvider<ItemStack, C, T>,
            vararg additionalCaps: IRegisterCapability<Disk>
        ): Builder<R> {
            diskCaps.add(IRegisterCapability.item(resourceCap, resourceCapProvider))
            diskCaps.addAll(additionalCaps)
            return this
        }

        fun <T, C : Any?> registerDiskDriveCapabilities(
            resourceCap: BlockCapability<T, C>,
            resourceCapProvider: ICapabilityProvider<BlockEntity, C, T>,
            vararg additionalCaps: IRegisterCapability<BlockEntityType<*>>
        ): Builder<R> {
            diskDriveCaps.add(IRegisterCapability.blockEntity(resourceCap, resourceCapProvider))
            diskDriveCaps.addAll(additionalCaps)
            return this
        }

        fun <T, C : Any?> registerNetworkViewCapabilities(
            resourceCap: BlockCapability<T, C>,
            resourceCapProvider: ICapabilityProvider<BlockEntity, C, T>,
            vararg additionalCaps: IRegisterCapability<BlockEntityType<*>>
        ): Builder<R> {
            networkViewCaps.add(IRegisterCapability.blockEntity(resourceCap, resourceCapProvider))
            networkViewCaps.addAll(additionalCaps)
            return this
        }

        override fun get(): ResourceType<R> =
            ResourceType(name, id.get(), disk.get(), diskCaps, diskDriveCaps, networkViewCaps)
    }

    fun interface IRegisterCapability<T> {
        fun register(event: RegisterCapabilitiesEvent, obj: T)

        companion object {
            fun <I : Item, T, C : Any?> item(
                cap: ItemCapability<T, C>, capProvider: ICapabilityProvider<ItemStack, C, T>
            ): IRegisterCapability<I> =
                IRegisterCapability { event, item -> event.registerItem(cap, capProvider, item) }

            fun <T, C : Any?> blockEntity(
                cap: BlockCapability<T, C>, capProvider: ICapabilityProvider<BlockEntity, C, T>
            ): IRegisterCapability<BlockEntityType<*>> = IRegisterCapability { event, bEntityType ->
                event.registerBlockEntity(cap, bEntityType, capProvider)
            }
        }
    }
}