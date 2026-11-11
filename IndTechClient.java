package net.indtech;

import net.fabricmc.api.ClientModInitializer;
import net.indtech.client.ElectricFurnaceScreen;
import net.indtech.client.GeneratorScreen;
import net.indtech.registry.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class IndTechClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.GENERATOR, GeneratorScreen::new);
        HandledScreens.register(ModScreenHandlers.ELECTRIC_FURNACE, ElectricFurnaceScreen::new);
    }
}
