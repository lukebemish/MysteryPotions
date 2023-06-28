/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl

import dev.lukebemish.mysterypotions.impl.item.MysteryActionData
import dev.lukebemish.mysterypotions.impl.item.MysteryActionItem
import dev.lukebemish.mysterypotions.impl.item.MysteryPotionData
import dev.lukebemish.mysterypotions.impl.item.MysteryPotionItem
import dev.lukebemish.mysterypotions.impl.random.PiecewiseRandomizable
import dev.lukebemish.mysterypotions.impl.random.RegistryRandomizer
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import org.groovymc.cgl.reg.RegistrationProvider
import org.groovymc.cgl.reg.RegistryObject

import java.util.function.Consumer

@CompileStatic
final class MysteryPotionsCommon {
    static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MOD_ID)
    static final RegistryRandomizer ITEM_RANDOMIZER = new RegistryRandomizer(Registries.ITEM)

    final List<RegistryObject<Item>> brewingTabTargets = new ArrayList<>()

    final RegistryObject<Item> potionSimple = registerItem('potion_simple') {
        MysteryPotionData data = new MysteryPotionData(
            Set.of(
                new ResourceLocation("blindness"),
                new ResourceLocation("nausea"),
                new ResourceLocation("invisibility"),
                new ResourceLocation("luck")
            ), [80,160,240], [0]
        )
        return new MysteryPotionItem(it, new Item.Properties().stacksTo(1), data)
    }

    final RegistryObject<Item> potionFire = registerItem('potion_fire') {
        MysteryActionData data = new MysteryActionData(
            Map.of(
                new ResourceLocation(Constants.MOD_ID, 'fire'),
                { LivingEntity entity ->
                    entity.setSecondsOnFire(15)
                } as Consumer<LivingEntity>,
                new ResourceLocation(Constants.MOD_ID, 'resistance'),
                { LivingEntity entity ->
                    MobEffectInstance instance = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0)
                    entity.addEffect(instance)
                } as Consumer<LivingEntity>
            )
        )
        return new MysteryActionItem(it, new Item.Properties().stacksTo(1), data)
    }

    final RegistryObject<Item> potionWater = registerItem('potion_water') {
        MysteryActionData data = new MysteryActionData(
            Map.of(
                new ResourceLocation(Constants.MOD_ID, 'drown'),
                { LivingEntity entity ->
                    entity.airSupply = 0
                } as Consumer<LivingEntity>,
                new ResourceLocation(Constants.MOD_ID, 'air'),
                { LivingEntity entity ->
                    entity.airSupply = entity.maxAirSupply
                } as Consumer<LivingEntity>
            )
        )
        return new MysteryActionItem(it, new Item.Properties().stacksTo(1), data)
    }

    private static MysteryPotionsCommon INSTANCE

    static init() {
        INSTANCE = new MysteryPotionsCommon()
    }

    static MysteryPotionsCommon getINSTANCE() {
        return INSTANCE
    }

    RegistryObject<Item> registerItem(String location, @ClosureParams(value = SimpleType, options = 'net.minecraft.resources.ResourceLocation') Closure<Item> itemSupplier) {
        var registered = ITEMS.register(location, {->
            ResourceLocation rl = new ResourceLocation(Constants.MOD_ID, location)
            Item item = itemSupplier.call(rl)

            if (item instanceof PiecewiseRandomizable) {
                ITEM_RANDOMIZER.randomizables.put(rl, (PiecewiseRandomizable) item)
            }

            return item
        })
        brewingTabTargets.add registered
        return registered
    }

    static void setupRandomized(ServerLevel level) {
        ITEM_RANDOMIZER.setup(level)
    }
}
