package com.example.examplemod.manager;

import com.example.examplemod.events.ModCommonSetupEvents;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModEventManager {
    protected static void registerEvents(IEventBus modBus, IEventBus forgeBus) {
        registerClientEvents(modBus, forgeBus);
        registerCommonEvents(modBus, forgeBus);
        registerServerEvents(modBus, forgeBus);
    }

    private static void registerClientEvents(IEventBus modBus, IEventBus forgeBus) {

    }

    private static void registerCommonEvents(IEventBus modBus, IEventBus forgeBus) {
        modBus.register(ModCommonSetupEvents.ModSetupEvents.class);
    }

    private static void registerServerEvents(IEventBus modBus, IEventBus forgeBus) {

    }
}
