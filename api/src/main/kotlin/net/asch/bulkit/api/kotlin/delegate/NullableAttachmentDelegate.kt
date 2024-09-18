package net.asch.bulkit.api.kotlin.delegate

import net.neoforged.neoforge.attachment.AttachmentHolder
import net.neoforged.neoforge.attachment.AttachmentType
import kotlin.jvm.optionals.getOrNull
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NullableAttachmentDelegate<R, V : Any>(
    private val attachmentHolder: AttachmentHolder, private val attachmentType: AttachmentType<V>
) : ReadWriteProperty<R, V?> {
    override fun getValue(thisRef: R, property: KProperty<*>): V? =
        attachmentHolder.getExistingData(attachmentType).getOrNull()

    override fun setValue(thisRef: R, property: KProperty<*>, value: V?) {
        if (value == null) {
            attachmentHolder.removeData(attachmentType)
        } else {
            attachmentHolder.setData(attachmentType, value)
        }
    }
}