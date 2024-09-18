package net.asch.bulkit.setup

import net.asch.bulkit.api.setup.Items
import net.asch.bulkit.item.NetworkConfigurator
import net.neoforged.neoforge.registries.DeferredItem

class Items {
    val networkConfigurator: DeferredItem<NetworkConfigurator> =
        Items.registerItem("network_configurator", ::NetworkConfigurator)
}