package net.createlosttechnology.event;

import net.createlosttechnology.CreateLostTechnology;
import net.createlosttechnology.config.ModConfig;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.*;

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
     * Checks whether a recipe should be removed/disabled based on configuration rules.
     *
     * @param recipe The recipe to inspect
     * @return true if the recipe should be removed, false if it is allowed
     */
    public static boolean shouldDisableRecipe(Recipe<?> recipe) {
        if (recipe == null) {
            return false;
        }

        try {
            // 1. Check Result Item
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

            // 2. Check Ingredients (Materials)
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
                        ItemStack[] matchingStacks = ingredient.getItems();
                        for (ItemStack stack : matchingStacks) {
                            if (stack.isEmpty()) continue;
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
                    }
                }
            }
        } catch (Exception e) {
            // Some specialized recipes might fail when getResultItem(RegistryAccess.EMPTY) is called without world context.
            // Safely log in debug and ignore.
            CreateLostTechnology.LOGGER.debug("Could not inspect recipe {}: {}", recipe.getId(), e.getMessage());
        }

        return false;
    }

    /**
     * Determines whether an item is considered a "lost technology" uncraftable item.
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
