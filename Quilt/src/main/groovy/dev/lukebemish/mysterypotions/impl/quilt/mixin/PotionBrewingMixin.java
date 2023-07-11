/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.quilt.mixin;

import dev.lukebemish.mysterypotions.impl.BrewingRecipe;
import dev.lukebemish.mysterypotions.impl.MysteryPotionsCommon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    private static void mysterypotions$mix(ItemStack reagent, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        for (BrewingRecipe recipe : MysteryPotionsCommon.getINSTANCE().getBrewingRecipes()) {
            var targetIngredient = recipe.getIngredientIngredient();
            if (!targetIngredient.test(reagent)) {
                continue;
            }
            var targetInput = recipe.getInputIngredient();
            if (!targetInput.test(input)) {
                continue;
            }
            cir.setReturnValue(recipe.getResultItemStack());
        }
    }

    @Inject(method = "hasPotionMix", at = @At("HEAD"), cancellable = true)
    private static void mysterypotions$hasPotionMix(ItemStack input, ItemStack reagent, CallbackInfoReturnable<Boolean> cir) {
        for (BrewingRecipe recipe : MysteryPotionsCommon.getINSTANCE().getBrewingRecipes()) {
            var targetIngredient = recipe.getIngredientIngredient();
            if (!targetIngredient.test(reagent)) {
                continue;
            }
            var targetInput = recipe.getInputIngredient();
            if (!targetInput.test(input)) {
                continue;
            }
            cir.setReturnValue(true);
            return;
        }
    }

    @Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
    private static void mysterypotions$isIngredient(ItemStack reagent, CallbackInfoReturnable<Boolean> cir) {
        for (BrewingRecipe recipe : MysteryPotionsCommon.getINSTANCE().getBrewingRecipes()) {
            var targetIngredient = recipe.getIngredientIngredient();
            if (!targetIngredient.test(reagent)) {
                continue;
            }
            cir.setReturnValue(true);
            return;
        }
    }
}
