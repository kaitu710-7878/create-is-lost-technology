package net.createlosttechnology.event;

import net.createlosttechnology.CreateLostTechnology;
import net.createlosttechnology.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateLostTechnology.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!ModConfig.SHOW_LOST_TECH_TOOLTIP.get()) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        if (RecipeFilter.isLostTechnologyItem(stack.getItem())) {
            event.getToolTip().add(Component.translatable("tooltip.create_lost_technology.lost_tech_header")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            event.getToolTip().add(Component.translatable("tooltip.create_lost_technology.lost_tech_desc")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
