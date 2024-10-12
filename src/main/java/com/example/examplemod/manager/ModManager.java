package com.example.examplemod.manager;

import net.minecraftforge.eventbus.api.IEventBus;

public class ModManager {
    public static void registerAll(IEventBus modBus, IEventBus forgeBus) {
        ModConfigManager.registerConfigs();
        ModEventManager.registerEvents(modBus, forgeBus);
        ModRegistryManager.registerRegistries(modBus);
    }
}
