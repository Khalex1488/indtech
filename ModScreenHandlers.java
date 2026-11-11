package net.indtech.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.indtech.IndTech;
import net.indtech.screen.ElectricFurnaceScreenHandler;
import net.indtech.screen.GeneratorScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<GeneratorScreenHandler> GENERATOR = Registry.register(
            Registries.SCREEN_HANDLER,
            IndTech.id("generator"),
            new ExtendedScreenHandlerType<GeneratorScreenHandler, BlockPos>(
                    GeneratorScreenHandler::new, BlockPos.PACKET_CODEC));

    public static final ScreenHandlerType<ElectricFurnaceScreenHandler> ELECTRIC_FURNACE = Registry.register(
            Registries.SCREEN_HANDLER,
            IndTech.id("electric_furnace"),
            new ExtendedScreenHandlerType<ElectricFurnaceScreenHandler, BlockPos>(
                    ElectricFurnaceScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void init() {
        // loads the class so the static fields above register the screen handler types
    }
}
