/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.item

import dev.lukebemish.mysterypotions.impl.random.PiecewiseRandomizable
import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import groovy.transform.CompileStatic
import net.minecraft.resources.ResourceLocation
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent

@CompileStatic
class MysteryPotionItem extends Item implements PiecewiseRandomizable {
    private final ResourceLocation location
    private final List<MysteryPotionData> data

    MysteryPotionItem(ResourceLocation location, Properties properties, List<MysteryPotionData> data) {
        super(properties)
        this.location = location
        this.data = data
    }

    @Override
    ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            if (!level.isClientSide) {
                for (MysteryPotionData d : data) {
                    MobEffectInstance instance = new MobEffectInstance(
                        d.effects.enabled.get(),
                        d.durations.enabled,
                        d.amplifiers.enabled
                    )
                    if (instance.effect.instantenous)
                        instance.effect.applyInstantenousEffect(livingEntity, livingEntity, livingEntity, instance.amplifier, 1.0d)
                    else
                        livingEntity.addEffect(instance)
                }
            }
            livingEntity.awardStat(Stats.ITEM_USED.get(this))
            if (!livingEntity.abilities.instabuild) {
                stack.shrink(1)
            }
        }
        if (livingEntity !instanceof Player || !((Player) livingEntity).abilities.instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE)
            }

            if (livingEntity instanceof Player) {
                livingEntity.inventory.add(new ItemStack(Items.GLASS_BOTTLE))
            }
        }

        livingEntity.gameEvent(GameEvent.DRINK)
        return stack
    }

    @Override
    int getUseDuration(ItemStack stack) {
        return 32
    }

    @Override
    UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK
    }

    @Override
    InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return ItemUtils.startUsingInstantly(level, player, usedHand)
    }

    @Override
    ResourceLocation getSeedLocation() {
        return location
    }

    @Override
    Map<String, RandomizerHolder<?>> getRandomizers() {
        Map<String, RandomizerHolder<?>> map = new HashMap<>()
        for (int i = 0; i < data.size(); i ++) {
            map["effect$i" as String] = data[i].effects
            map["duration$i" as String] = data[i].durations
            map["amplifier$i" as String] = data[i].amplifiers
        }
        return map
    }
}
