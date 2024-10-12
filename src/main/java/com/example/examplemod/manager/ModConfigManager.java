package com.example.examplemod.manager;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.config.ModClientConfig;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ModConfigManager {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ModClientConfig CLIENT;

    static {
        //final Pair<CSCommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CSCommonConfig::new);
        final Pair<ModClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ModClientConfig::new);

        //COMMON_SPEC = commonSpecPair.getRight();
        //COMMON = commonSpecPair.getLeft();

        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static void registerConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, ExampleMod.MODID + "/" + ExampleMod.MODID + "-client.toml");
    }
}
