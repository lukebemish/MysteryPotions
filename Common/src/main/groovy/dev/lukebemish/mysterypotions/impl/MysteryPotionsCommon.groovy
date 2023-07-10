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
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import org.groovymc.cgl.reg.RegistrationProvider
import org.groovymc.cgl.reg.RegistryObject

import java.util.function.Consumer

@CompileStatic
final class MysteryPotionsCommon {
    static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MOD_ID)
    static final RegistryRandomizer ITEM_RANDOMIZER = new RegistryRandomizer(Registries.ITEM)

    final List<RegistryObject<Item>> brewingTabTargets = new ArrayList<>()
    final List<BrewingRecipe> brewingRecipes = new ArrayList<>()

    final RegistryObject<Item> potionSimple = registerItem('potion_simple') {
        MysteryPotionData data = MysteryPotionData.of(
            Set.of(
                new ResourceLocation('blindness'),
                new ResourceLocation('nausea'),
                new ResourceLocation('invisibility'),
                new ResourceLocation('luck')
            ), [80,160,240], [0]
        )
        return new MysteryPotionItem(it, new Item.Properties().stacksTo(1), List.of(data))
    }

    final RegistryObject<Item> potionIntermediate = registerItem('potion_intermediate') {
        MysteryPotionData data1 = MysteryPotionData.of(
            Set.of(
                new ResourceLocation('speed'),
                new ResourceLocation('slowness'),
                new ResourceLocation('strength'),
                new ResourceLocation('weakness'),
                new ResourceLocation('haste'),
                new ResourceLocation('mining_fatigue'),
            ), [2400,3600,4800], [0]
        )
        MysteryPotionData data2 = MysteryPotionData.of(
            Set.of(
                new ResourceLocation('instant_health'),
                new ResourceLocation('instant_damage')
            ), [80], [0]
        )
        return new MysteryPotionItem(it, new Item.Properties().stacksTo(1), List.of(data1, data2))
    }

    final RegistryObject<Item> potionAdvanced = registerItem('potion_advanced') {
        MysteryPotionData data1 = MysteryPotionData.of(
            Set.of(
                new ResourceLocation('regeneration'),
                new ResourceLocation('wither')
            ), [3600,7200,9600], [1,2]
        )
        MysteryPotionData data2 = MysteryPotionData.of(
            Set.of(
                new ResourceLocation('slow_falling'),
                new ResourceLocation('levitation')
            ), [240,360,480], [1,2,3]
        )
        return new MysteryPotionItem(it, new Item.Properties().stacksTo(1), List.of(data1, data2))
    }

    final RegistryObject<Item> potionVillager = registerItem('potion_villager') {
        MysteryPotionData data = MysteryPotionData.of(
            Set.of(
                new ResourceLocation('hero_of_the_village'),
                new ResourceLocation('bad_omen'),
            ), [3600,9600], [0]
        )
        return new MysteryPotionItem(it, new Item.Properties().stacksTo(1), List.of(data),
            { ServerLevel serverLevel, LivingEntity livingEntity ->
                serverLevel.isVillage(livingEntity.blockPosition())
        }, true)
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
                    MobEffectInstance instance = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 7200, 0)
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
        return new MysteryActionItem(it, new Item.Properties().stacksTo(1), data,
            { ServerLevel serverLevel, LivingEntity livingEntity ->
                livingEntity.underWater || livingEntity.airSupply != livingEntity.maxAirSupply
            }, true)
    }

    private static MysteryPotionsCommon INSTANCE

    private MysteryPotionsCommon() {
        brewingRecipes << new BrewingRecipe(
            { Ingredient.of(PotionUtils.setPotion(Items.POTION.defaultInstance, Potions.WATER)) },
            { Ingredient.of(Items.LILY_PAD) },
            { potionSimple.get().defaultInstance }
        )
        brewingRecipes << new BrewingRecipe(
            { Ingredient.of(PotionUtils.setPotion(Items.POTION.defaultInstance, Potions.FIRE_RESISTANCE)) },
            { Ingredient.of(Items.CHARCOAL) },
            { potionFire.get().defaultInstance }
        )
        brewingRecipes << new BrewingRecipe(
            { Ingredient.of(PotionUtils.setPotion(Items.POTION.defaultInstance, Potions.WATER)) },
            { Ingredient.of(Items.PUFFERFISH) },
            { potionWater.get().defaultInstance }
        )
    }

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
