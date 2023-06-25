/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.item

import dev.lukebemish.mysterypotions.impl.Constants
import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import groovy.transform.CompileStatic
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

import java.util.function.Supplier

@CompileStatic
class MysteryPotionData {
    final RandomizerHolder<Supplier<MobEffect>> effects
    final RandomizerHolder<Integer> durations
    final RandomizerHolder<Integer> amplifiers

    MysteryPotionData(Set<ResourceLocation> effects, List<Integer> durations, List<Integer> amplifiers) {
        this.effects = new RandomizerHolder<>(effects.collectEntries {
            final ResourceLocation key = it
            [key, {-> BuiltInRegistries.MOB_EFFECT.get(key)} as Supplier<MobEffect>]
        })
        this.durations = new RandomizerHolder<>(durations.collectEntries {
            [new ResourceLocation(Constants.MOD_ID, it.toString()), it]
        })
        this.amplifiers = new RandomizerHolder<>(amplifiers.collectEntries {
            [new ResourceLocation(Constants.MOD_ID, it.toString()), it]
        })
    }
}
