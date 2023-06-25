package dev.lukebemish.mysterypotions.impl.item


import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity

import java.util.function.Consumer

class MysteryActionData {
    final RandomizerHolder<Consumer<LivingEntity>> actions

    MysteryActionData(Map<ResourceLocation, Consumer<LivingEntity>> actions) {
        this.actions = new RandomizerHolder<>(actions)
    }
}
