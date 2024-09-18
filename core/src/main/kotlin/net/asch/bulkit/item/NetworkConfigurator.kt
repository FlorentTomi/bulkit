package net.asch.bulkit.item

import net.asch.bulkit.network.network.NetworkConfiguratorPayloads
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class NetworkConfigurator(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (pPlayer.isSecondaryUseActive) {
            NetworkConfiguratorPayloads.clearRootPosition(pUsedHand)
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand))
        }

        return super.use(pLevel, pPlayer, pUsedHand)
    }
}