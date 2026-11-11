package net.indtech.registry;

import net.indtech.IndTech;
import net.indtech.block.CableBlock;
import net.indtech.block.ElectricFurnaceBlock;
import net.indtech.block.GeneratorBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;

public class ModBlocks {
    public static final Block TIN_ORE = register("tin_ore",
            new Block(AbstractBlock.Settings.create()
                    .strength(3.0f, 3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));

    public static final Block GENERATOR = register("generator",
            new GeneratorBlock(AbstractBlock.Settings.create()
                    .strength(3.0f, 6.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> state.get(Properties.LIT) ? 13 : 0)));

    public static final Block ELECTRIC_FURNACE = register("electric_furnace",
            new ElectricFurnaceBlock(AbstractBlock.Settings.create()
                    .strength(3.0f, 6.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> state.get(Properties.LIT) ? 9 : 0)));

    public static final Block CABLE = register("cable",
            new CableBlock(AbstractBlock.Settings.create()
                    .strength(0.5f, 1.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()));

    private static Block register(String name, Block block) {
        Registry.register(Registries.BLOCK, IndTech.id(name), block);
        Registry.register(Registries.ITEM, IndTech.id(name), new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static void init() {
        // loads the class so the static fields above register the blocks
    }
}
