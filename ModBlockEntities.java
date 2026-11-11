package net.indtech.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.indtech.IndTech;
import net.indtech.block.entity.ElectricFurnaceBlockEntity;
import net.indtech.block.entity.GeneratorBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<GeneratorBlockEntity> GENERATOR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            IndTech.id("generator"),
            FabricBlockEntityTypeBuilder.create(GeneratorBlockEntity::new, ModBlocks.GENERATOR).build());

    public static final BlockEntityType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            IndTech.id("electric_furnace"),
            FabricBlockEntityTypeBuilder.create(ElectricFurnaceBlockEntity::new, ModBlocks.ELECTRIC_FURNACE).build());

    public static void init() {
        // loads the class so the static fields above register the block entity types
    }
}
