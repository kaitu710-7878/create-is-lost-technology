package net.createlosttechnology.mixin;

import net.createlosttechnology.event.RecipeFilter;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(targets = "mezz.jei.library.recipes.RecipeManagerInternal", remap = false)
public abstract class JeiRecipeFilterMixin {

    @ModifyVariable(method = "addRecipes", at = @At("HEAD"), argsOnly = true, remap = false)
    private <T> List<T> createLostTech_filterJeiRecipes(List<T> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            return recipes;
        }

        List<T> filtered = new ArrayList<>(recipes.size());
        for (T item : recipes) {
            if (item instanceof Recipe<?> recipe) {
                if (!RecipeFilter.shouldDisableRecipe(recipe)) {
                    filtered.add(item);
                }
            } else {
                filtered.add(item);
            }
        }
        return filtered;
    }
}
