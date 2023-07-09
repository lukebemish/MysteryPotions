/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.datagen

import dev.lukebemish.mysterypotions.impl.MysteryPotionsCommon
import groovy.transform.CompileStatic
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplates

@CompileStatic
class ModModelProvider extends FabricModelProvider {
    ModModelProvider(FabricDataOutput output) {
        super(output)
    }

    @Override
    void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(MysteryPotionsCommon.INSTANCE.potionSimple.get(), ModelTemplates.FLAT_ITEM)
        itemModelGenerator.generateFlatItem(MysteryPotionsCommon.INSTANCE.potionFire.get(), ModelTemplates.FLAT_ITEM)
        itemModelGenerator.generateFlatItem(MysteryPotionsCommon.INSTANCE.potionWater.get(), ModelTemplates.FLAT_ITEM)
        itemModelGenerator.generateFlatItem(MysteryPotionsCommon.INSTANCE.potionIntermediate.get(), ModelTemplates.FLAT_ITEM)
        itemModelGenerator.generateFlatItem(MysteryPotionsCommon.INSTANCE.potionAdvanced.get(), ModelTemplates.FLAT_ITEM)
        itemModelGenerator.generateFlatItem(MysteryPotionsCommon.INSTANCE.potionVillager.get(), ModelTemplates.FLAT_ITEM)
    }
}
