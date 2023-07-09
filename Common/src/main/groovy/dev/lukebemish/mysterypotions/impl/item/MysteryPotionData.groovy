/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.item

import dev.lukebemish.mysterypotions.impl.Constants
import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

import java.util.function.Supplier

@CompileStatic
@TupleConstructor
class MysteryPotionData {
    final RandomizerHolder<Supplier<MobEffect>> effects
    final RandomizerHolder<Integer> durations
    final RandomizerHolder<Integer> amplifiers

    static MysteryPotionData of(Set<ResourceLocation> effects, List<Integer> durations, List<Integer> amplifiers) {
        return new MysteryPotionData(
            new RandomizerHolder<>(effects.collectEntries {
                final ResourceLocation key = it
                [key, {-> BuiltInRegistries.MOB_EFFECT.get(key)} as Supplier<MobEffect>]
            }),
            new RandomizerHolder<>(durations.collectEntries {
                [new ResourceLocation(Constants.MOD_ID, it.toString()), it]
            }),
            new RandomizerHolder<>(amplifiers.collectEntries {
                [new ResourceLocation(Constants.MOD_ID, it.toString()), it]
            })
        )
    }
}
