/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.item

import dev.lukebemish.mysterypotions.impl.random.PiecewiseRandomizable
import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import org.jetbrains.annotations.Nullable

@CompileStatic
class MysteryPotionItem extends Item implements PiecewiseRandomizable {
    private final ResourceLocation location
    private final List<MysteryPotionData> data
    private final Closure<Boolean> allowUse
    private final boolean tooltip

    MysteryPotionItem(ResourceLocation location, Properties properties, List<MysteryPotionData> data) {
        this(location, properties, data, { level, entity -> true }, false)
    }

    MysteryPotionItem(ResourceLocation location, Properties properties, List<MysteryPotionData> data,
                      @ClosureParams(value = SimpleType.class, options = [
                          'net.minecraft.server.level.ServerLevel',
                          'net.minecraft.world.entity.LivingEntity'
                      ]) Closure<Boolean> allowUse, boolean tooltip) {
        super(properties)
        this.location = location
        this.data = data
        this.allowUse = allowUse
        this.tooltip = tooltip
    }

    @Override
    ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            if (livingEntity instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(livingEntity, stack)
            }
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
    void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (level instanceof ServerLevel) {
            if (!allowUse.call(level, livingEntity)) {
                livingEntity.stopUsingItem()
            }
        }
    }

    @Override
    void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (tooltip)
            tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY))
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
