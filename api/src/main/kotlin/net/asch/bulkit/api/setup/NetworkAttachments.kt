package net.asch.bulkit.api.setup

import com.mojang.serialization.Codec
import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.data.DriveDiskHandler
import net.minecraft.core.BlockPos
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object NetworkAttachments {
    private val REGISTER: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "${BulkItApi.ID}.network")

    val VIEW_ROOT_POS: DeferredHolder<AttachmentType<*>, AttachmentType<BlockPos>> =
        REGISTER.register("view_root_pos") { ->
            AttachmentType.builder { -> BlockPos.ZERO }.serialize(BlockPos.CODEC).build()
        }

    val VIEW_SLOT_MAP: DeferredHolder<AttachmentType<*>, AttachmentType<Map<Int, Int>>> =
        REGISTER.register("view_slot_map") { ->
            AttachmentType.builder { -> emptyMap<Int, Int>() }.serialize(Codec.unboundedMap(Codec.INT, Codec.INT))
                .build()
        }

    val DISK_HANDLER: DeferredHolder<AttachmentType<*>, AttachmentType<DriveDiskHandler>> =
        REGISTER.register("disk_handler") { ->
            AttachmentType.serializable(::DriveDiskHandler).build()
        }

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
    fun <A> registerAttachment(
        name: String, sup: () -> AttachmentType<A>
    ): DeferredHolder<AttachmentType<*>, AttachmentType<A>> = REGISTER.register(name, sup)
}