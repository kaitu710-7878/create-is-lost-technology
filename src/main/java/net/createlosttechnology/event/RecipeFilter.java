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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

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

    public static final Set<ResourceLocation> KINETIC_MACHINES = Set.of(
            // Andesite Casing machines
            new ResourceLocation("create", "mechanical_press"),
            new ResourceLocation("create", "mechanical_mixer"),
            new ResourceLocation("create", "mechanical_saw"),
            new ResourceLocation("create", "mechanical_drill"),
            new ResourceLocation("create", "mechanical_harvester"),
            new ResourceLocation("create", "mechanical_plough"),
            new ResourceLocation("create", "mechanical_roller"),
            new ResourceLocation("create", "deployer"),
            new ResourceLocation("create", "encased_fan"),
            new ResourceLocation("create", "millstone"),
            new ResourceLocation("create", "rope_pulley"),
            new ResourceLocation("create", "elevator_pulley"),
            new ResourceLocation("create", "mechanical_piston"),
            new ResourceLocation("create", "sticky_mechanical_piston"),
            new ResourceLocation("create", "mechanical_bearing"),
            new ResourceLocation("create", "windmill_bearing"),
            new ResourceLocation("create", "clockwork_bearing"),
            new ResourceLocation("create", "gearbox"),
            new ResourceLocation("create", "vertical_gearbox"),
            new ResourceLocation("create", "clutch"),
            new ResourceLocation("create", "gearshift"),
            new ResourceLocation("create", "speedometer"),
            new ResourceLocation("create", "stressometer"),
            new ResourceLocation("create", "encased_chain_drive"),
            new ResourceLocation("create", "andesite_door"),
            // Chassis & Gantry
            new ResourceLocation("create", "radial_chassis"),
            new ResourceLocation("create", "linear_chassis"),
            new ResourceLocation("create", "secondary_linear_chassis"),
            new ResourceLocation("create", "gantry_shaft"),
            new ResourceLocation("create", "gantry_carriage"),
            // Power generation & transmission
            new ResourceLocation("create", "water_wheel"),
            new ResourceLocation("create", "large_water_wheel"),
            new ResourceLocation("create", "turntable"),
            new ResourceLocation("create", "weighted_ejector"),
            // Advanced machines & logistics
            new ResourceLocation("create", "crushing_wheel"),
            new ResourceLocation("create", "mechanical_arm"),
            new ResourceLocation("create", "mechanical_crafter"),
            new ResourceLocation("create", "sequenced_gearshift"),
            new ResourceLocation("create", "rotation_speed_controller"),
            new ResourceLocation("create", "spout"),
            new ResourceLocation("create", "item_drain"),
            new ResourceLocation("create", "hose_pulley"),
            new ResourceLocation("create", "portable_storage_interface"),
            new ResourceLocation("create", "portable_fluid_interface"),
            new ResourceLocation("create", "smart_chute"),
            new ResourceLocation("create", "chute"),
            new ResourceLocation("create", "cart_assembler")
    );

    /**
     * Purges invalid, missing tag, and lost technology recipes from a given RecipeManager instance.
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

        CreateLostTechnology.LOGGER.info("[Create is Lost Technology] Successfully purged {} recipes from RecipeManager.", removedCount);
        return removedCount;
    }

    /**
     * Core decision engine: whether a recipe should be disabled/purged.
     *
     * @param recipe The recipe to inspect
     * @return true if the recipe must be purged, false if allowed
     */
    public static boolean shouldDisableRecipe(Recipe<?> recipe) {
        if (recipe == null) {
            return false;
        }

        // --- RULE 1: ROOT-CAUSE FIX FOR MISSING TAGS ---
        // Purge any recipe that has empty tags, unbound tags, or missing ingredients (detected via Forge's BARRIER injection)
        if (ModConfig.PURGE_BROKEN_RECIPES.get() && hasEmptyOrMissingIngredient(recipe)) {
            return true;
        }

        // --- RULE 2: IDENTIFIER HEURISTICS FOR LOST TECH ---
        ResourceLocation recipeId = recipe.getId();
        if (recipeId != null && "create".equals(recipeId.getNamespace())) {
            String path = recipeId.getPath();
            if (ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get() && path.contains("andesite_casing")) {
                return true;
            }
            if (ModConfig.DISABLE_MACHINE_RECIPES.get() || ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get()) {
                if (path.startsWith("crafting/kinetics/mechanical_")
                        || path.contains("chassis")
                        || path.contains("gantry")
                        || path.equals("crafting/kinetics/deployer")
                        || path.equals("crafting/kinetics/encased_fan")
                        || path.equals("crafting/kinetics/millstone")
                        || path.equals("crafting/kinetics/gearbox")
                        || path.equals("crafting/kinetics/clutch")
                        || path.equals("crafting/kinetics/gearshift")
                        || path.equals("crafting/kinetics/rope_pulley")
                        || path.equals("crafting/kinetics/elevator_pulley")
                        || path.equals("crafting/kinetics/water_wheel")
                        || path.equals("crafting/kinetics/large_water_wheel")
                        || path.equals("crafting/kinetics/windmill_bearing")
                        || path.equals("crafting/kinetics/mechanical_bearing")
                        || path.equals("crafting/kinetics/clockwork_bearing")
                        || path.equals("crafting/kinetics/speedometer")
                        || path.equals("crafting/kinetics/stressometer")
                        || path.equals("crafting/kinetics/encased_chain_drive")
                        || path.equals("crafting/kinetics/cart_assembler")
                        || path.startsWith("mechanical_crafting/crushing_wheel")) {
                    return true;
                }
            }
            if (ModConfig.DISABLE_COGWHEEL_RECIPES.get() && (path.contains("cogwheel") || path.contains("large_cogwheel"))) {
                return true;
            }
            if (ModConfig.DISABLE_ANDESITE_ALLOY_RECIPES.get() && path.contains("andesite_alloy")) {
                return true;
            }
        }

        // --- RULE 3: RESULT ITEM (OUTPUT) CHECK ---
        try {
            ItemStack resultStack = recipe.getResultItem(RegistryAccess.EMPTY);
            if (!resultStack.isEmpty()) {
                Item resultItem = resultStack.getItem();
                // Purge if output is a barrier block (missing output item)
                if (resultItem == Items.BARRIER || resultItem == Blocks.BARRIER.asItem() || resultItem == Items.AIR) {
                    return true;
                }

                ResourceLocation resultId = BuiltInRegistries.ITEM.getKey(resultItem);
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
                    if ((ModConfig.DISABLE_MACHINE_RECIPES.get() || ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get())
                            && KINETIC_MACHINES.contains(resultId)) {
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

        // --- RULE 4: INGREDIENTS (INPUT MATERIALS) CHECK ---
        try {
            List<Ingredient> ingredients = recipe.getIngredients();
            if (ingredients != null && !ingredients.isEmpty()) {
                boolean checkAlloy = ModConfig.DISABLE_ANDESITE_ALLOY_RECIPES.get();
                boolean checkCog = ModConfig.DISABLE_COGWHEEL_RECIPES.get();
                boolean checkCasing = ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get();
                boolean checkMachines = ModConfig.DISABLE_MACHINE_RECIPES.get();
                List<? extends String> customIngredients = ModConfig.CUSTOM_DISABLED_INGREDIENTS.get();
                boolean hasCustom = customIngredients != null && !customIngredients.isEmpty();

                if (checkAlloy || checkCog || checkCasing || checkMachines || hasCustom) {
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient == null || ingredient.isEmpty()) {
                            continue;
                        }
                        try {
                            ItemStack[] matchingStacks = ingredient.getItems();
                            for (ItemStack stack : matchingStacks) {
                                if (stack == null || stack.isEmpty()) continue;
                                Item item = stack.getItem();
                                if (item == Items.BARRIER || item == Blocks.BARRIER.asItem() || item == Items.AIR) continue;

                                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
                                if (itemId == null) continue;

                                if (checkAlloy && ANDESITE_ALLOY_ITEMS.contains(itemId)) {
                                    return true;
                                }
                                if (checkCog && COGWHEEL_ITEMS.contains(itemId)) {
                                    return true;
                                }
                                if (checkCasing && ANDESITE_CASING.equals(itemId)) {
                                    return true;
                                }
                                if (checkMachines && KINETIC_MACHINES.contains(itemId)) {
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
     * Checks if a recipe contains any ingredient that resolves to no items, empty tags, or BARRIER items.
     * In Minecraft Forge, an empty/missing tag produces an ItemStack of Blocks.BARRIER named "Empty tag: ...".
     * Detecting this eliminates 100% of "Missing Tag" warnings in JEI without hardcoding item lists.
     */
    public static boolean hasEmptyOrMissingIngredient(Recipe<?> recipe) {
        if (recipe == null) return false;
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            return false;
        }

        for (Ingredient ingredient : ingredients) {
            if (ingredient == null) continue;

            if (ingredient.isEmpty()) {
                return true;
            }

            try {
                ItemStack[] items = ingredient.getItems();
                if (items == null || items.length == 0) {
                    return true;
                }

                boolean hasValidRealItem = false;
                for (ItemStack stack : items) {
                    if (stack != null && !stack.isEmpty()) {
                        Item item = stack.getItem();
                        // Forge injects Blocks.BARRIER when a tag is empty/missing!
                        if (item != Items.AIR && item != Items.BARRIER && item != Blocks.BARRIER.asItem()) {
                            hasValidRealItem = true;
                            break;
                        }
                    }
                }

                // If ingredient only resolves to BARRIER or nothing, it is an empty/missing tag
                if (!hasValidRealItem) {
                    return true;
                }
            } catch (Exception e) {
                // If resolving ingredient throws an error (e.g. tag unbound or corrupted), it's a broken ingredient
                return true;
            }
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
        if (ModConfig.DISABLE_MACHINE_RECIPES.get() && KINETIC_MACHINES.contains(id)) {
            return true;
        }

        List<? extends String> customOutputs = ModConfig.CUSTOM_DISABLED_OUTPUT_ITEMS.get();
        if (customOutputs != null && !customOutputs.isEmpty()) {
            return customOutputs.contains(id.toString());
        }

        return false;
    }
}
