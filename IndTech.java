package net.indtech;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.indtech.registry.ModBlockEntities;
import net.indtech.registry.ModBlocks;
import net.indtech.registry.ModItems;
import net.indtech.registry.ModScreenHandlers;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class IndTech implements ModInitializer {
    public static final String MOD_ID = "indtech";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModScreenHandlers.init();

        // Tin ore generation (feature JSON lives in data/indtech/worldgen)
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, id("ore_tin"))
        );
    }
}
