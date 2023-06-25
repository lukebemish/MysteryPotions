package dev.lukebemish.mysterypotions.datagen

import dev.lukebemish.mysterypotions.impl.MysteryPotionsCommon
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class ModLangProvider extends FabricLanguageProvider {
    protected ModLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput)
    }

    @Override
    void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionSimple.get(), "Swampy Brew")
        translationBuilder.add(MysteryPotionsCommon.INSTANCE.potionFire.get(), "Flaming Concoction")
    }
}
