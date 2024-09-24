package net.asch.bulkit.api.capability.disk

import net.asch.bulkit.api.item.Mod
import net.asch.bulkit.api.setup.DiskDataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ComponentItemHandler

class DiskModHandler(disk: ItemStack, ctx: Unit) : ComponentItemHandler(disk, DiskDataComponents.MODS.get(), 4) {
    fun hasCreative(): Boolean = contents.nonEmptyStream().anyMatch(Mod::isCreative)
    fun hasDowngrade(): Boolean = contents.nonEmptyStream().anyMatch(Mod::isDowngrade)

    fun getMultiplier(defaultMultiplier: Int): Int {
        val multiplierOpt = contents.nonEmptyStream().map(Mod::getMultiplier).filter { it != null }
            .mapToInt { it!! }.reduce { acc, mult -> acc * mult }

        if (multiplierOpt.isEmpty) {
            return defaultMultiplier
        }

        val multiplier = multiplierOpt.asInt
        return if (multiplier < 0) multiplier else multiplier * defaultMultiplier
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        if (!super.isItemValid(slot, stack) || stack.item !is Mod) {
            return false
        }

        if (Mod.isCreative(stack)) {
            return !hasCreative()
        }

        if (Mod.isDowngrade(stack)) {
            return !hasDowngrade()
        }

        return true
    }
}