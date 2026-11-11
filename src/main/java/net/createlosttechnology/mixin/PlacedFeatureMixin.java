package net.createlosttechnology.mixin;

import net.createlosttechnology.config.ModConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureMixin {

    @Shadow
    public abstract Holder<ConfiguredFeature<?, ?>> feature();

    @Inject(method = "placeWithContext", at = @At("HEAD"), cancellable = true)
    private void createLostTech_disableZincOre(PlacementContext context, RandomSource source, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfig.DISABLE_ZINC_ORE.get()) {
            return;
        }

        Holder<ConfiguredFeature<?, ?>> featureHolder = this.feature();
        if (featureHolder == null) {
            return;
        }

        // 1. Check feature registry ID
        if (featureHolder.unwrapKey().isPresent()) {
            ResourceLocation featureId = featureHolder.unwrapKey().get().location();
            if (isZincOreFeature(featureId)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 2. Check targets inside OreConfiguration
        ConfiguredFeature<?, ?> configured = featureHolder.value();
        if (configured != null && configured.config() instanceof OreConfiguration oreConfig) {
            for (OreConfiguration.TargetBlockState target : oreConfig.targetStates) {
                if (target.state != null) {
                    ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(target.state.getBlock());
                    if (isZincOreBlock(blockId)) {
                        cir.setReturnValue(false);
                        return;
                    }
                }
            }
        }
    }

    private static boolean isZincOreFeature(ResourceLocation id) {
        if (id == null) return false;
        String path = id.getPath().toLowerCase();
        String namespace = id.getNamespace().toLowerCase();
        return (namespace.equals("create") || namespace.equals("minecraft")) && path.contains("zinc");
    }

    private static boolean isZincOreBlock(ResourceLocation id) {
        if (id == null) return false;
        return id.getNamespace().equals("create") && (id.getPath().equals("zinc_ore") || id.getPath().equals("deepslate_zinc_ore"));
    }
}
