package net.asch.bulkit.api.setup

import net.asch.bulkit.api.BulkItApi
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object Blocks {
    private val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(BulkItApi.ID)

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
    fun <B : Block> registerBlock(
        name: String, props: BlockBehaviour.Properties, sup: (BlockBehaviour.Properties) -> B
    ): DeferredBlock<B> = REGISTER.registerBlock(name, sup, props)
}