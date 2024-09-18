package net.asch.bulkit.network.disk

import net.asch.bulkit.BulkIt
import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.capability.disk.IDiskHandler
import net.asch.bulkit.api.data.ResourceIdentifier
import net.asch.bulkit.api.item.Disk
import net.asch.bulkit.api.network.ItemPayloadC2S
import net.asch.bulkit.api.setup.BulkItCapabilities
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object DiskUpdatePayloads {
    fun register(registrar: PayloadRegistrar) {
        ItemPayloadC2S.register(registrar, SetItem.TYPE, SetItem.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, AddItem.TYPE, AddItem.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, SetFluid.TYPE, SetFluid.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, AddFluid.TYPE, AddFluid.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, Grow.TYPE, Grow.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, Shrink.TYPE, Shrink.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, Lock.TYPE, Lock.STREAM_CODEC)
        ItemPayloadC2S.register(registrar, VoidExcess.TYPE, VoidExcess.STREAM_CODEC)
    }

    fun setItem(usedHand: InteractionHand, id: ResourceIdentifier<Item>) = SetItem(usedHand, id).send()
    fun addItem(usedHand: InteractionHand, stack: ItemStack) = AddItem(usedHand, stack).send()
    fun setFluid(usedHand: InteractionHand, id: ResourceIdentifier<Fluid>) = SetFluid(usedHand, id).send()
    fun addFluid(usedHand: InteractionHand, stack: FluidStack) = AddFluid(usedHand, stack).send()
    fun grow(usedHand: InteractionHand, amount: Long) = Grow(usedHand, amount).send()
    fun shrink(usedHand: InteractionHand, amount: Long) = Shrink(usedHand, amount).send()
    fun lock(usedHand: InteractionHand, locked: Boolean) = Lock(usedHand, locked).send()
    fun voidExcess(usedHand: InteractionHand, voidExcess: Boolean) = VoidExcess(usedHand, voidExcess).send()

    private class SetItem(usedHand: InteractionHand, val id: ResourceIdentifier<Item>) :
        ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            stack.set(BulkIt.RESOURCES.item.get().id, id)
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<SetItem>(ResourceLocation.fromNamespaceAndPath(BulkIt.ID, "disk_set_item"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetItem> = StreamCodec.composite(
                INTERACTION_HAND_CODEC,
                SetItem::usedHand,
                ResourceIdentifier.streamCodec(BuiltInRegistries.ITEM),
                SetItem::id,
                ::SetItem
            )
        }
    }

    private class AddItem(usedHand: InteractionHand, val stackToAdd: ItemStack) :
        ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            val diskCapability = stack.getCapability(Capabilities.ItemHandler.ITEM)
            diskCapability?.insertItem(0, stackToAdd, false)
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<AddItem>(ResourceLocation.fromNamespaceAndPath(BulkIt.ID, "disk_add_item"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, AddItem> = StreamCodec.composite(
                INTERACTION_HAND_CODEC,
                AddItem::usedHand,
                ItemStack.OPTIONAL_STREAM_CODEC,
                AddItem::stackToAdd,
                ::AddItem
            )
        }
    }

    private class SetFluid(usedHand: InteractionHand, val id: ResourceIdentifier<Fluid>) :
        ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            stack.set(BulkIt.RESOURCES.fluid.get().id, id)
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<SetFluid>(ResourceLocation.fromNamespaceAndPath(BulkIt.ID, "disk_set_fluid"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetFluid> = StreamCodec.composite(
                INTERACTION_HAND_CODEC,
                SetFluid::usedHand,
                ResourceIdentifier.streamCodec(BuiltInRegistries.FLUID),
                SetFluid::id,
                ::SetFluid
            )
        }
    }

    private class AddFluid(usedHand: InteractionHand, val stackToAdd: FluidStack) :
        ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            val diskCapability = stack.getCapability(Capabilities.FluidHandler.ITEM)
            diskCapability?.fill(stackToAdd, IFluidHandler.FluidAction.EXECUTE)
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<AddFluid>(ResourceLocation.fromNamespaceAndPath(BulkIt.ID, "disk_add_fluid"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, AddFluid> = StreamCodec.composite(
                INTERACTION_HAND_CODEC,
                AddFluid::usedHand,
                FluidStack.OPTIONAL_STREAM_CODEC,
                AddFluid::stackToAdd,
                ::AddFluid
            )
        }
    }

    private class Grow(usedHand: InteractionHand, val amount: Long) : ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            val diskCapability = stack.getCapability(BulkItCapabilities.Disk.RESOURCE, Unit)
            diskCapability?.let { diskCapability.amountL += amount }
        }

        companion object {
            val TYPE = CustomPacketPayload.Type<Grow>(ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "disk_grow"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Grow> = StreamCodec.composite(
                INTERACTION_HAND_CODEC, Grow::usedHand, ByteBufCodecs.VAR_LONG, Grow::amount, ::Grow
            )
        }
    }

    private class Shrink(usedHand: InteractionHand, val amount: Long) : ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            val diskCapability = stack.getCapability(BulkItCapabilities.Disk.RESOURCE, Unit)
            diskCapability?.let { diskCapability.amountL -= amount }
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<Shrink>(ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "disk_shrink"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Shrink> = StreamCodec.composite(
                INTERACTION_HAND_CODEC, Shrink::usedHand, ByteBufCodecs.VAR_LONG, Shrink::amount, ::Shrink
            )
        }
    }

    private class Lock(usedHand: InteractionHand, val locked: Boolean) : ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            val diskCapability = stack.getCapability(BulkItCapabilities.Disk.RESOURCE, Unit)
            diskCapability?.isLocked = locked
        }

        companion object {
            val TYPE = CustomPacketPayload.Type<Lock>(ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "disk_lock"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Lock> = StreamCodec.composite(
                INTERACTION_HAND_CODEC, Lock::usedHand, ByteBufCodecs.BOOL, Lock::locked, ::Lock
            )
        }
    }

    private class VoidExcess(usedHand: InteractionHand, val voidExcess: Boolean) :
        ItemPayloadC2S<Disk>(Disk::class, usedHand) {
        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
        override fun handleItem(stack: ItemStack) {
            val diskCapability = stack.getCapability(BulkItCapabilities.Disk.RESOURCE, Unit)
            diskCapability?.isVoidExcess = voidExcess
        }

        companion object {
            val TYPE =
                CustomPacketPayload.Type<VoidExcess>(ResourceLocation.fromNamespaceAndPath(BulkItApi.ID, "disk_void"))
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, VoidExcess> = StreamCodec.composite(
                INTERACTION_HAND_CODEC, VoidExcess::usedHand, ByteBufCodecs.BOOL, VoidExcess::voidExcess, ::VoidExcess
            )
        }
    }
}