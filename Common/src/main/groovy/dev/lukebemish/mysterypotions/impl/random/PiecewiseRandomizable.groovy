/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.random

import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.NotNull

interface PiecewiseRandomizable {
    @NotNull ResourceLocation getSeedLocation()

    @NotNull Map<String, RandomizerHolder<?>> getRandomizers()
}
