package net.createlosttechnology.mixin;

import com.google.gson.JsonElement;
import net.createlosttechnology.event.RecipeFilter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("RETURN"))
    private void createLostTech_filterRecipesOnApply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        RecipeFilter.purgeRecipeManager((RecipeManager) (Object) this);
    }

    @Inject(method = "replaceRecipes", at = @At("RETURN"))
    private void createLostTech_filterRecipesOnReplace(Iterable<Recipe<?>> pRecipes, CallbackInfo ci) {
        RecipeFilter.purgeRecipeManager((RecipeManager) (Object) this);
    }
}
