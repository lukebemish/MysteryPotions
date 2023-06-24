/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl

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
import net.minecraft.world.item.Item
import org.groovymc.cgl.reg.RegistrationProvider

@CompileStatic
final class MysteryPotionsCommon {
    static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MOD_ID)
    static final RegistryRandomizer ITEM_RANDOMIZER = new RegistryRandomizer(Registries.ITEM)

    static void init() {
        registerItem('potion_simple') {
            MysteryPotionData data = new MysteryPotionData(
                Set.of(
                    new ResourceLocation("blindness"),
                    new ResourceLocation("nausea"),
                    new ResourceLocation("invisibility"),
                    new ResourceLocation("luck")
                ), [80,160,240], [1]
            )
            return new MysteryPotionItem(it, new Item.Properties().stacksTo(1), data)
        }
    }

    static void registerItem(String location, @ClosureParams(value = SimpleType, options = 'net.minecraft.resources.ResourceLocation') Closure<Item> itemSupplier) {
        ITEMS.register(location, {->
            ResourceLocation rl = new ResourceLocation(Constants.MOD_ID, location)
            Item item = itemSupplier.call(rl)

            if (item instanceof PiecewiseRandomizable) {
                ITEM_RANDOMIZER.randomizables.put(rl, (PiecewiseRandomizable) item)
            }

            return item
        })
    }

    static void setupRandomized(ServerLevel level) {
        ITEM_RANDOMIZER.setup(level)
    }
}
