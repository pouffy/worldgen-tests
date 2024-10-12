package com.example.examplemod.manager;

import com.example.examplemod.worldgen.feature.ModFeatures;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModRegistryManager {
    protected static void registerRegistries(IEventBus modBus) {
        ModFeatures.FEATURES.register(modBus);
    }
}
