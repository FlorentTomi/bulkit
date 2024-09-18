package net.asch.bulkit.api.kotlin.delegate

import net.neoforged.neoforge.attachment.AttachmentHolder
import net.neoforged.neoforge.attachment.AttachmentType
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class DefaultedAttachmentDelegate<R, V : Any>(
    private val attachmentHolder: AttachmentHolder,
    private val attachmentType: AttachmentType<V>,
    private val defaultValue: V
) : ReadWriteProperty<R, V> {
    override fun getValue(thisRef: R, property: KProperty<*>): V {
        if (!attachmentHolder.hasData(attachmentType)) {
            attachmentHolder.setData(attachmentType, defaultValue)
        }

        return attachmentHolder.getData(attachmentType)
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: V) {
        attachmentHolder.setData(attachmentType, value)
    }
}