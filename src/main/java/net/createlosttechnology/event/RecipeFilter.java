package net.createlosttechnology.event;

import com.google.common.collect.ImmutableMap;
import net.createlosttechnology.CreateLostTechnology;
import net.createlosttechnology.config.ModConfig;
import net.createlosttechnology.mixin.RecipeManagerAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecipeFilter {
    public static final Set<ResourceLocation> ANDESITE_ALLOY_ITEMS = Set.of(
            new ResourceLocation("create", "andesite_alloy"),
            new ResourceLocation("create", "andesite_alloy_block")
    );

    public static final Set<ResourceLocation> COGWHEEL_ITEMS = Set.of(
            new ResourceLocation("create", "cogwheel"),
            new ResourceLocation("create", "large_cogwheel")
    );

    public static final ResourceLocation ANDESITE_CASING = new ResourceLocation("create", "andesite_casing");

    /**
     * Purges lost technology recipes from a given RecipeManager instance.
     *
     * @param manager The RecipeManager to clean
     * @return Number of recipes purged
     */
    public static int purgeRecipeManager(RecipeManager manager) {
        if (manager == null) {
            return 0;
        }

        RecipeManagerAccessor accessor = (RecipeManagerAccessor) manager;
        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> originalRecipes = accessor.getRecipes();
        if (originalRecipes == null) {
            return 0;
        }

        int removedCount = 0;
        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> newRecipesByType = new HashMap<>();
        Map<ResourceLocation, Recipe<?>> newByName = new HashMap<>();

        for (Map.Entry<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> typeEntry : originalRecipes.entrySet()) {
            RecipeType<?> type = typeEntry.getKey();
            Map<ResourceLocation, Recipe<?>> originalMap = typeEntry.getValue();
            Map<ResourceLocation, Recipe<?>> filteredMap = new HashMap<>();

            for (Map.Entry<ResourceLocation, Recipe<?>> recipeEntry : originalMap.entrySet()) {
                ResourceLocation id = recipeEntry.getKey();
                Recipe<?> recipe = recipeEntry.getValue();

                if (shouldDisableRecipe(recipe)) {
                    removedCount++;
                    CreateLostTechnology.LOGGER.debug("[Create is Lost Technology] Purged recipe: {}", id);
                } else {
                    filteredMap.put(id, recipe);
                    newByName.put(id, recipe);
                }
            }

            newRecipesByType.put(type, ImmutableMap.copyOf(filteredMap));
        }

        accessor.setRecipes(ImmutableMap.copyOf(newRecipesByType));
        accessor.setByName(ImmutableMap.copyOf(newByName));

        CreateLostTechnology.LOGGER.info("[Create is Lost Technology] Purged {} lost technology recipes from RecipeManager.", removedCount);
        return removedCount;
    }

    /**
     * Determines whether a recipe should be disabled based strictly on target items
     * (Andesite Alloy, Cogwheels, Andesite Casing, and recipes using Andesite Casing as an ingredient).
     *
     * @param recipe The recipe to inspect
     * @return true if the recipe should be disabled, false otherwise
     */
    public static boolean shouldDisableRecipe(Recipe<?> recipe) {
        if (recipe == null) {
            return false;
        }

        // --- 1. RESULT ITEM (OUTPUT) CHECK ---
        try {
            ItemStack resultStack = recipe.getResultItem(RegistryAccess.EMPTY);
            if (!resultStack.isEmpty()) {
                ResourceLocation resultId = BuiltInRegistries.ITEM.getKey(resultStack.getItem());
                if (resultId != null) {
                    if (ModConfig.DISABLE_ANDESITE_ALLOY_RECIPES.get() && ANDESITE_ALLOY_ITEMS.contains(resultId)) {
                        return true;
                    }
                    if (ModConfig.DISABLE_COGWHEEL_RECIPES.get() && COGWHEEL_ITEMS.contains(resultId)) {
                        return true;
                    }
                    if (ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get() && ANDESITE_CASING.equals(resultId)) {
                        return true;
                    }

                    List<? extends String> customOutputs = ModConfig.CUSTOM_DISABLED_OUTPUT_ITEMS.get();
                    if (customOutputs != null && !customOutputs.isEmpty()) {
                        String idStr = resultId.toString();
                        for (String disabled : customOutputs) {
                            if (idStr.equals(disabled)) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            CreateLostTechnology.LOGGER.debug("Could not inspect result for recipe {}: {}", recipe.getId(), e.getMessage());
        }

        // --- 2. INGREDIENTS CHECK (Machines/Items requiring Andesite Casing) ---
        try {
            List<Ingredient> ingredients = recipe.getIngredients();
            if (ingredients != null && !ingredients.isEmpty()) {
                boolean checkCasing = ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get();
                List<? extends String> customIngredients = ModConfig.CUSTOM_DISABLED_INGREDIENTS.get();
                boolean hasCustom = customIngredients != null && !customIngredients.isEmpty();

                if (checkCasing || hasCustom) {
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient == null || ingredient.isEmpty()) {
                            continue;
                        }
                        try {
                            ItemStack[] matchingStacks = ingredient.getItems();
                            for (ItemStack stack : matchingStacks) {
                                if (stack == null || stack.isEmpty()) continue;

                                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
                                if (itemId == null) continue;

                                if (checkCasing && ANDESITE_CASING.equals(itemId)) {
                                    return true;
                                }
                                if (hasCustom) {
                                    String idStr = itemId.toString();
                                    for (String disabled : customIngredients) {
                                        if (idStr.equals(disabled)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        } catch (Exception ingredientEx) {
                            CreateLostTechnology.LOGGER.debug("Could not resolve ingredient in {}: {}", recipe.getId(), ingredientEx.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            CreateLostTechnology.LOGGER.debug("Could not inspect ingredients for recipe {}: {}", recipe.getId(), e.getMessage());
        }

        return false;
    }

    /**
     * Determines whether an item is considered a "lost technology" uncraftable item for tooltips.
     */
    public static boolean isLostTechnologyItem(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) return false;

        if (ModConfig.DISABLE_ANDESITE_ALLOY_RECIPES.get() && ANDESITE_ALLOY_ITEMS.contains(id)) {
            return true;
        }
        if (ModConfig.DISABLE_COGWHEEL_RECIPES.get() && COGWHEEL_ITEMS.contains(id)) {
            return true;
        }
        if (ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get() && ANDESITE_CASING.equals(id)) {
            return true;
        }

        List<? extends String> customOutputs = ModConfig.CUSTOM_DISABLED_OUTPUT_ITEMS.get();
        if (customOutputs != null && !customOutputs.isEmpty()) {
            return customOutputs.contains(id.toString());
        }

        return false;
    }
}
