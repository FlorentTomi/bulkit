package net.asch.bulkit.api.capability.disk

interface IDiskHandler {
    var amountL: Long
    var isLocked: Boolean
    var isVoidExcess: Boolean
    var amountI: Int

    fun getMultiplier(defaultMultiplier: Int): Int
}