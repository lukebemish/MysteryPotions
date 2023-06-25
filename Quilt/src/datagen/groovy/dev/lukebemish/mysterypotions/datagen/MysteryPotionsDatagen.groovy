/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.datagen

import dev.lukebemish.mysterypotions.impl.Constants
import groovy.transform.CompileStatic
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

@CompileStatic
class MysteryPotionsDatagen implements DataGeneratorEntrypoint {

    @Override
    void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        Constants.LOGGER.info 'Starting datagen...'

        fabricDataGenerator.createPack().addProvider(ModModelProvider::new as FabricDataGenerator.Pack.Factory)
        fabricDataGenerator.createPack().addProvider(ModLangProvider::new as FabricDataGenerator.Pack.Factory)
    }

    @Override
    String getEffectiveModId() {
        return Constants.MOD_ID
    }
}
