package net.asch.bulkit.api.item

import net.asch.bulkit.api.setup.ModDataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class Mod(properties: Properties) : Item(properties.stacksTo(16)) {
    companion object {
        const val DOWNGRADE_MULTIPLIER: Int = -1
        val UPGRADE_MULTIPLIERS: List<Int> = listOf(8, 16, 32)

        fun getMultiplier(stack: ItemStack): Int? = stack.get(ModDataComponents.MULTIPLIER)
        fun isUpgrade(stack: ItemStack): Boolean =
            stack.has(ModDataComponents.MULTIPLIER) && stack.get(ModDataComponents.MULTIPLIER)!! > 0

        fun isDowngrade(stack: ItemStack): Boolean = stack.get(ModDataComponents.MULTIPLIER) == DOWNGRADE_MULTIPLIER
        fun isCreative(stack: ItemStack): Boolean = stack.has(ModDataComponents.CREATIVE)
    }
}