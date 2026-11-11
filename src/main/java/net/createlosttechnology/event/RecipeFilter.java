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
     * Checks whether a recipe should be removed/disabled based on configuration rules.
     *
     * @param recipe The recipe to inspect
     * @return true if the recipe should be removed, false if it is allowed
     */
    public static boolean shouldDisableRecipe(Recipe<?> recipe) {
        if (recipe == null) {
            return false;
        }

        // 1. Fast check by Recipe ID
        ResourceLocation recipeId = recipe.getId();
        if (recipeId != null && "create".equals(recipeId.getNamespace())) {
            String path = recipeId.getPath();
            // Andesite casing application in-world / manual
            if (ModConfig.DISABLE_ANDESITE_CASING_RECIPES.get() && path.contains("andesite_casing")) {
                return true;
            }
            // Machine / Chassis / Gantry crafting
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
            // Cogwheel recipes
            if (ModConfig.DISABLE_COGWHEEL_RECIPES.get() && (path.contains("cogwheel") || path.contains("large_cogwheel"))) {
                return true;
            }
            // Andesite alloy recipes
            if (ModConfig.DISABLE_ANDESITE_ALLOY_RECIPES.get() && path.contains("andesite_alloy")) {
                return true;
            }
        }

        try {
            // 2. Check Result Item
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

        // 3. Check Ingredients (Materials) safely per ingredient
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
                                if (stack.isEmpty()) continue;
                                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
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
