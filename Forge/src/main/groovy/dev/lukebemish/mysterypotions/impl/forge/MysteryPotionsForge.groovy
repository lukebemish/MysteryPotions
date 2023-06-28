/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.forge


import dev.lukebemish.mysterypotions.impl.Constants
import dev.lukebemish.mysterypotions.impl.MysteryPotionsCommon
import groovy.transform.CompileStatic
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.CreativeModeTabs
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.event.server.ServerStartedEvent
import org.groovymc.gml.GMod

@GMod(Constants.MOD_ID)
@CompileStatic
class MysteryPotionsForge {
    MysteryPotionsForge() {
        MysteryPotionsCommon.init()

        forgeBus.addListener(ServerStartedEvent) {
            ServerLevel level = it.server.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("overworld")))
            MysteryPotionsCommon.setupRandomized(level)
        }

        forgeBus.addListener(BuildCreativeModeTabContentsEvent) {
            if (it.tabKey == CreativeModeTabs.FOOD_AND_DRINKS) {
                MysteryPotionsCommon.INSTANCE.brewingTabTargets.each { registered ->
                    it.accept(registered)
                }
            }
        }
    }
}
