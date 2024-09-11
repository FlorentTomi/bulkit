package net.asch.builkit.mekanism.kotlin.extension

import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasStack
import net.asch.bulkit.api.data.ResourceIdentifier
import net.minecraft.core.component.DataComponentPatch

fun GasStack.identifier(): ResourceIdentifier<Gas> = ResourceIdentifier<Gas>(chemicalHolder, DataComponentPatch.EMPTY)
fun ResourceIdentifier<Gas>.stack(amount: Long): GasStack = GasStack(holder, amount)