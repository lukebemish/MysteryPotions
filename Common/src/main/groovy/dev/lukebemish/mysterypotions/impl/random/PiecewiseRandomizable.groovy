package dev.lukebemish.mysterypotions.impl.random

import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.NotNull

interface PiecewiseRandomizable {
    @NotNull ResourceLocation getSeedLocation()

    @NotNull Map<String, RandomizerHolder<?>> getRandomizers()
}
