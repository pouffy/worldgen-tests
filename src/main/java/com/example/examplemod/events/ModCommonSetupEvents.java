package com.example.examplemod.events;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.datagen.ModFeaturesProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModCommonSetupEvents {

    public static class ModSetupEvents {
        @SubscribeEvent
        public static void onGatherDataEvent(final GatherDataEvent event) {
            DataGenerator dataGenerator = event.getGenerator();
            final ExistingFileHelper efh = event.getExistingFileHelper();
            final PackOutput output = event.getGenerator().getPackOutput();
            final CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

            otherProviders(output, lookup, efh).forEach(provider -> dataGenerator.addProvider(event.includeServer(), provider));
        }

        public static List<DataProvider> otherProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper efh) {
            RegistrySetBuilder builder = new RegistrySetBuilder()
                    .add(Registries.CONFIGURED_FEATURE, ModFeaturesProvider.ConfiguredFeatures::bootstrap)
                    .add(Registries.PLACED_FEATURE, ModFeaturesProvider.PlacedFeatures::bootstrap)
                    .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModFeaturesProvider.BiomeModifiers::bootstrap);

            return List.of(
                    new DatapackBuiltinEntriesProvider(output, lookup, builder, Set.of(ExampleMod.MODID))
            );
        }
    }
}
