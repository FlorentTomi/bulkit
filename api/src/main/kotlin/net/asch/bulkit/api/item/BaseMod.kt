package net.asch.bulkit.api.item

import net.minecraft.world.item.Item

abstract class BaseMod(val type: Types, properties: Properties) : Item(properties.stacksTo(16)) {
    enum class Types {
        Upgrade, Downgrade;
    }
}