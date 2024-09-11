package net.asch.bulkit.kotlin.extension

import net.asch.bulkit.api.data.ResourceIdentifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

fun ItemStack.identifier(): ResourceIdentifier<Item> = ResourceIdentifier<Item>(itemHolder, componentsPatch)
fun ResourceIdentifier<Item>.stack(amount: Int): ItemStack = ItemStack(holder, amount, componentsPatch)