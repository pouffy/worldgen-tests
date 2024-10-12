package com.example.examplemod.datagen;

import com.example.examplemod.worldgen.feature.ModFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;

import java.util.List;

public class ModFeaturesProvider {
    public static class ConfiguredFeatures {
        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
            ctx.register(ModFeatures.ICE_SPIKE_CONFIGURED, new ConfiguredFeature<>(ModFeatures.ICE_SPIKE.get(), new NoneFeatureConfiguration()));
        }
    }

    public static class PlacedFeatures {
        public static void bootstrap(BootstapContext<PlacedFeature> ctx) {
            ctx.register(ModFeatures.ICE_SPIKE_PLACED, new PlacedFeature(ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(ModFeatures.ICE_SPIKE_CONFIGURED),
                    List.of(
                            RarityFilter.onAverageOnceEvery(7),
                            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(100), VerticalAnchor.absolute(320))),
                            BiomeFilter.biome()
                    )
            ));
        }
    }

    public static class BiomeModifiers {
        public static void bootstrap(BootstapContext<BiomeModifier> ctx) {
            final HolderGetter<PlacedFeature> featureRegistry = ctx.lookup(Registries.PLACED_FEATURE);
            final HolderGetter<Biome> biomeRegistry = ctx.lookup(Registries.BIOME);

            ctx.register(ModFeatures.ICE_SPIKE_MODIFIER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                            biomeRegistry.getOrThrow(Tags.Biomes.IS_MOUNTAIN),
                            HolderSet.direct(featureRegistry.getOrThrow(ModFeatures.ICE_SPIKE_PLACED)),
                            GenerationStep.Decoration.SURFACE_STRUCTURES
                    )
            );
        }
    }
}
