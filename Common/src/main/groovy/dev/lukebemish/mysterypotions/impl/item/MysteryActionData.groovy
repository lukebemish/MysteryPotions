/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.item


import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import groovy.transform.CompileStatic
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity

import java.util.function.Consumer

@CompileStatic
class MysteryActionData {
    final RandomizerHolder<Consumer<LivingEntity>> actions

    MysteryActionData(Map<ResourceLocation, Consumer<LivingEntity>> actions) {
        this.actions = new RandomizerHolder<>(actions)
    }
}
