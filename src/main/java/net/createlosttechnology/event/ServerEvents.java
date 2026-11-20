package net.createlosttechnology.event;

import net.createlosttechnology.CreateLostTechnology;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateLostTechnology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        if (server != null) {
            RecipeFilter.purgeRecipeManager(server.getRecipeManager());
        }
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        // When event.getPlayer() is null, it means a /reload occurred on the server.
        if (event.getPlayer() == null) {
            MinecraftServer server = event.getPlayerList().getServer();
            if (server != null) {
                RecipeFilter.purgeRecipeManager(server.getRecipeManager());
            }
        }
    }
}
