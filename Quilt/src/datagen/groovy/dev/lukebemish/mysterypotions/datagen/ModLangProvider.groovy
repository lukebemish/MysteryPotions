/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.datagen

import dev.lukebemish.mysterypotions.impl.MysteryPotionsCommon
import groovy.transform.CompileStatic
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

@CompileStatic
class ModLangProvider extends FabricLanguageProvider {
    protected ModLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput)
    }

    @Override
    void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionSwamp.get(), 'Swampy Brew')
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionFire.get(), 'Flaming Concoction')
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionWater.get(), 'Mariner\'s Mixture')
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionFloatingBlood.get(), 'Floating Blood')
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionTime.get(), 'Bottled Time')
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionVillager.get(), 'Distilled Emeralds')

        translationBuilder.add('item.mysterypotions.potion_villager.desc', 'Probably won\'t do much outside of a village')
        translationBuilder.add('item.mysterypotions.potion_water.desc', 'Probably won\'t do much outside of water')
    }
}
