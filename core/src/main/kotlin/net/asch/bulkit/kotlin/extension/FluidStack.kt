package net.asch.bulkit.kotlin.extension

import net.asch.bulkit.api.data.ResourceIdentifier
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

fun FluidStack.identifier(): ResourceIdentifier<Fluid> = ResourceIdentifier<Fluid>(fluidHolder, componentsPatch)
fun ResourceIdentifier<Fluid>.stack(amount: Int): FluidStack = FluidStack(holder, amount, componentsPatch)