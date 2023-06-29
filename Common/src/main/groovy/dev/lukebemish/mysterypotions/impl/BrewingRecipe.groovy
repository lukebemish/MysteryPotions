package dev.lukebemish.mysterypotions.impl

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovy.transform.TupleConstructor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

@TupleConstructor
@CompileStatic
class BrewingRecipe {
    final Closure<Ingredient> input
    final Closure<Ingredient> ingredient
    final Closure<ItemStack> result

    @Memoized Ingredient getInputIngredient() {
        return input.call()
    }

    @Memoized Ingredient getIngredientIngredient() {
        return ingredient.call()
    }

    @Memoized ItemStack getResultItemStack() {
        return result.call()
    }
}
