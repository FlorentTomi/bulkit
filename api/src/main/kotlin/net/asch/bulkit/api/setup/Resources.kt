package net.asch.bulkit.api.setup

import net.asch.bulkit.api.BulkItApi
import net.asch.bulkit.api.resource.DeferredResources
import net.neoforged.bus.api.IEventBus

object Resources {
    val REGISTER: DeferredResources = DeferredResources.create(BulkItApi.ID)

    fun register(modBus: IEventBus) = REGISTER.register(modBus)
}