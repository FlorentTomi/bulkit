package net.asch.bulkit.api.item

import net.asch.bulkit.api.setup.DiskDataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemContainerContents

class Disk(properties: Properties) : Item(
    properties.component(DiskDataComponents.AMOUNT, 0).component(DiskDataComponents.LOCKED, false)
        .component(DiskDataComponents.VOID_EXCESS, false)
        .component(DiskDataComponents.MODS, ItemContainerContents.EMPTY).stacksTo(16)
)