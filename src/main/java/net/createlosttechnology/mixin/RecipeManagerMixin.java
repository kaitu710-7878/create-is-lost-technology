package net.createlosttechnology.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import net.createlosttechnology.CreateLostTechnology;
import net.createlosttechnology.event.RecipeFilter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Shadow
    private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;

    @Shadow
    private Map<ResourceLocation, Recipe<?>> byName;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("RETURN"))
    private void createLostTech_filterRecipesOnApply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        filterAllRecipes();
    }

    @Inject(method = "replaceRecipes", at = @At("RETURN"))
    private void createLostTech_filterRecipesOnReplace(Iterable<Recipe<?>> pRecipes, CallbackInfo ci) {
        filterAllRecipes();
    }

    private void filterAllRecipes() {
        if (this.recipes == null || this.byName == null) {
            return;
        }

        int removedCount = 0;
        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> newRecipesByType = new HashMap<>();
        Map<ResourceLocation, Recipe<?>> newByName = new HashMap<>();

        for (Map.Entry<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> typeEntry : this.recipes.entrySet()) {
            RecipeType<?> type = typeEntry.getKey();
            Map<ResourceLocation, Recipe<?>> originalMap = typeEntry.getValue();
            Map<ResourceLocation, Recipe<?>> filteredMap = new HashMap<>();

            for (Map.Entry<ResourceLocation, Recipe<?>> recipeEntry : originalMap.entrySet()) {
                ResourceLocation id = recipeEntry.getKey();
                Recipe<?> recipe = recipeEntry.getValue();

                if (RecipeFilter.shouldDisableRecipe(recipe)) {
                    removedCount++;
                    CreateLostTechnology.LOGGER.debug("[Create is Lost Technology] Removed recipe: {}", id);
                } else {
                    filteredMap.put(id, recipe);
                    newByName.put(id, recipe);
                }
            }

            newRecipesByType.put(type, ImmutableMap.copyOf(filteredMap));
        }

        this.recipes = ImmutableMap.copyOf(newRecipesByType);
        this.byName = ImmutableMap.copyOf(newByName);

        CreateLostTechnology.LOGGER.info("[Create is Lost Technology] Successfully purged {} recipes from RecipeManager.", removedCount);
    }
}
