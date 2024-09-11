package net.asch.bulkit.api.item

import net.minecraft.world.item.Item

abstract class BaseMod(properties: Properties) : Item(properties.stacksTo(16))