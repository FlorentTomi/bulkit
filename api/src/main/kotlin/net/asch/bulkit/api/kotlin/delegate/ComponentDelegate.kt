package net.asch.bulkit.api.kotlin.delegate

import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.common.MutableDataComponentHolder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ComponentDelegate<R, V : Any>(
    private val componentHolder: MutableDataComponentHolder,
    private val componentType: DataComponentType<V>
) : ReadWriteProperty<R, V> {
    override fun getValue(thisRef: R, property: KProperty<*>): V {
        val v = componentHolder.get(componentType) ?: throw RuntimeException("component $componentType can't be null")
        return v
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: V) {
        componentHolder.set(componentType, value)
    }
}