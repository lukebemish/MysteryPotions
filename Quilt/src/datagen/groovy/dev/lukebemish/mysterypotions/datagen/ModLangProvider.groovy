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
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionSimple.get(), 'Swampy Brew')
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionFire.get(), 'Flaming Concoction')
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionWater.get(), 'Mariner\'s Mixture')
    }
}
