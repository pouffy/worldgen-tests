package com.example.examplemod.worldgen.feature;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.worldgen.feature.ice_spike.IceSpikeFeature;
import com.example.examplemod.worldgen.feature.volcano.DebugFeature;
import com.example.examplemod.worldgen.feature.volcano.VolcanoFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ExampleMod.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ICE_SPIKE = FEATURES.register("ice_spike", () -> new IceSpikeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> VOLCANO = FEATURES.register("volcano", () -> new VolcanoFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> DEBUG = FEATURES.register("debug", () -> new DebugFeature(NoneFeatureConfiguration.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_SPIKE_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ExampleMod.MODID, "ice_spike"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOLCANO_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ExampleMod.MODID, "volcano"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEBUG_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ExampleMod.MODID, "debug"));

    public static final ResourceKey<PlacedFeature> ICE_SPIKE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ExampleMod.MODID, "ice_spike"));
    public static final ResourceKey<PlacedFeature> VOLCANO_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ExampleMod.MODID, "volcano"));
    public static final ResourceKey<PlacedFeature> DEBUG_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ExampleMod.MODID, "debug"));

    public static final ResourceKey<BiomeModifier> ICE_SPIKE_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(ExampleMod.MODID, "ice_spike"));
    public static final ResourceKey<BiomeModifier> VOLCANO_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(ExampleMod.MODID, "volcano"));
    public static final ResourceKey<BiomeModifier> DEBUG_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(ExampleMod.MODID, "debug"));
}
