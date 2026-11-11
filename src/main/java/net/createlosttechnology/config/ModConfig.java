package net.createlosttechnology.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collections;
import java.util.List;

public class ModConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec.BooleanValue DISABLE_ZINC_ORE;
    public static final ForgeConfigSpec.BooleanValue DISABLE_ANDESITE_ALLOY_RECIPES;
    public static final ForgeConfigSpec.BooleanValue DISABLE_COGWHEEL_RECIPES;
    public static final ForgeConfigSpec.BooleanValue DISABLE_ANDESITE_CASING_RECIPES;
    public static final ForgeConfigSpec.BooleanValue DISABLE_MACHINE_RECIPES;
    public static final ForgeConfigSpec.BooleanValue PURGE_BROKEN_RECIPES;
    public static final ForgeConfigSpec.BooleanValue SHOW_LOST_TECH_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CUSTOM_DISABLED_OUTPUT_ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CUSTOM_DISABLED_INGREDIENTS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Create is Lost Technology - Configuration Settings").push("general");

        DISABLE_ZINC_ORE = builder
                .comment("Whether to prevent Zinc Ore and Deepslate Zinc Ore from generating in the world.",
                        "亜鉛鉱石および深層亜鉛鉱石のワールド生成を無効化するかどうか。")
                .define("disableZincOre", true);

        DISABLE_ANDESITE_ALLOY_RECIPES = builder
                .comment("Whether to disable all recipes that produce Andesite Alloy or Andesite Alloy Blocks.",
                        "安山岩合金および安山岩合金ブロックを成果物とするすべてのレシピを無効化するかどうか。")
                .define("disableAndesiteAlloyRecipes", true);

        DISABLE_COGWHEEL_RECIPES = builder
                .comment("Whether to disable all recipes that produce Cogwheels and Large Cogwheels.",
                        "歯車および大きな歯車を成果物とするすべてのレシピを無効化するかどうか。")
                .define("disableCogwheelRecipes", true);

        DISABLE_ANDESITE_CASING_RECIPES = builder
                .comment("Whether to disable recipes that produce Andesite Casing OR require Andesite Casing as an ingredient.",
                        "安山岩ケーシング自体のクラフト、および安山岩ケーシングを材料とするすべてのレシピを無効化するかどうか。")
                .define("disableAndesiteCasingRecipes", true);

        DISABLE_MACHINE_RECIPES = builder
                .comment("Whether to disable crafting recipes for all Create kinetic machines (Mechanical Press, Mixer, Saw, Drill, Harvester, etc.).",
                        "Createの主要な機械（メカニカルプレス、ミキサー、ノコギリ、ドリル、収穫機等）自体のクラフトレシピを無効化するかどうか。")
                .define("disableMachineRecipes", true);

        PURGE_BROKEN_RECIPES = builder
                .comment("Whether to automatically purge broken recipes containing empty tags, unresolvable tags, or missing ingredients (eliminates 'Missing Tag' issues in JEI).",
                        "空のタグや未解決のタグ、存在しない素材を要求する不完全なレシピを自動パージするかどうか（JEIでのミッシングタグ表示を根本防止）。")
                .define("purgeBrokenRecipes", true);

        SHOW_LOST_TECH_TOOLTIP = builder
                .comment("Whether to show a 'Lost Technology' tooltip on items that cannot be crafted.",
                        "クラフト不能になったアイテムに「失われた技術」ツールチップを表示するかどうか。")
                .define("showLostTechTooltip", true);

        CUSTOM_DISABLED_OUTPUT_ITEMS = builder
                .comment("Additional item registry IDs whose recipes should be disabled (as result items).",
                        "追加で成果物として無効化したいアイテムIDのリスト。")
                .defineListAllowEmpty("customDisabledOutputItems", Collections.emptyList(), ModConfig::validateResourceLocation);

        CUSTOM_DISABLED_INGREDIENTS = builder
                .comment("Additional item registry IDs whose presence in any recipe ingredient will disable that recipe.",
                        "材料に含まれていた場合にそのレシピを無効化する追加のアイテムIDリスト。")
                .defineListAllowEmpty("customDisabledIngredients", Collections.emptyList(), ModConfig::validateResourceLocation);

        builder.pop();
        COMMON_SPEC = builder.build();
    }

    private static boolean validateResourceLocation(Object obj) {
        return obj instanceof String str && str.contains(":");
    }
}
