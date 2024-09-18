package net.asch.bulkit.setup

import com.mojang.datafixers.types.constant.EmptyPart
import net.asch.bulkit.BulkIt
import net.asch.bulkit.block.entity.DiskDriveEntity
import net.asch.bulkit.block.entity.NetworkViewEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object BlockEntities {
    private val REGISTER: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BulkIt.ID)

    val NETWORK_VIEW: DeferredHolder<BlockEntityType<*>, BlockEntityType<NetworkViewEntity>> =
        REGISTER.register("disk_drive") { ->
            BlockEntityType.Builder.of(
                ::NetworkViewEntity, *BulkIt.BLOCKS.networkViews.map { it.get() }.toTypedArray()
            ).build(EmptyPart())
        }

    val DISK_DRIVE: DeferredHolder<BlockEntityType<*>, BlockEntityType<DiskDriveEntity>> =
        REGISTER.register("disk_drive") { ->
            BlockEntityType.Builder.of(
                ::DiskDriveEntity, BulkIt.BLOCKS.diskDrive.get()
            ).build(EmptyPart())
        }

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
}