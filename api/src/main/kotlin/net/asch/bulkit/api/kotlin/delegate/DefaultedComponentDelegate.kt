package net.asch.bulkit.api.kotlin.delegate

import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.common.MutableDataComponentHolder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class DefaultedComponentDelegate<R, V : Any>(
    private val componentHolder: MutableDataComponentHolder,
    private val componentType: DataComponentType<V>,
    private val defaultValue: V
) : ReadWriteProperty<R, V> {
    override fun getValue(thisRef: R, property: KProperty<*>): V =
        componentHolder.getOrDefault(componentType, defaultValue)

    override fun setValue(thisRef: R, property: KProperty<*>, value: V) {
        componentHolder.set(componentType, value)
    }
}