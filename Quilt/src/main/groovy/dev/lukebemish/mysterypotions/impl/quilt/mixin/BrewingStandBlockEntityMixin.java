package dev.lukebemish.mysterypotions.impl.quilt.mixin;

import dev.lukebemish.mysterypotions.impl.BrewingRecipe;
import dev.lukebemish.mysterypotions.impl.MysteryPotionsCommon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {

    @Inject(method = "canPlaceItem", at = @At("HEAD"))
    private void mysterypotions$canPlaceItem(int index, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue())
            return;
        if (index <= 2) {
            for (BrewingRecipe recipe : MysteryPotionsCommon.getINSTANCE().getBrewingRecipes()) {
                Ingredient input = recipe.getInputIngredient();
                if (input.test(stack)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
