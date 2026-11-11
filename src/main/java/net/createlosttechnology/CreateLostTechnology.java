package net.createlosttechnology;

import com.mojang.logging.LogUtils;
import net.createlosttechnology.config.ModConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateLostTechnology.MOD_ID)
public class CreateLostTechnology {
    public static final String MOD_ID = "create_lost_technology";
    public static final String MOD_NAME = "Create is Lost Technology";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateLostTechnology() {
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.COMMON_SPEC, "create_lost_technology-common.toml");

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("[{}] Initialized successfully! The technology has been forgotten...", MOD_NAME);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("[{}] Setup completed.", MOD_NAME);
    }
}
