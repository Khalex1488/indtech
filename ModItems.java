package net.indtech.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.indtech.IndTech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItems {
    public static final Item RAW_TIN = register("raw_tin", new Item(new Item.Settings()));
    public static final Item TIN_INGOT = register("tin_ingot", new Item(new Item.Settings()));
    public static final Item BRONZE_INGOT = register("bronze_ingot", new Item(new Item.Settings()));

    public static final ItemGroup MAIN_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            IndTech.id("main"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModBlocks.GENERATOR))
                    .displayName(Text.translatable("itemGroup.indtech.main"))
                    .entries((context, entries) -> {
                        entries.add(ModBlocks.TIN_ORE);
                        entries.add(RAW_TIN);
                        entries.add(TIN_INGOT);
                        entries.add(BRONZE_INGOT);
                        entries.add(ModBlocks.CABLE);
                        entries.add(ModBlocks.GENERATOR);
                        entries.add(ModBlocks.ELECTRIC_FURNACE);
                    })
                    .build());

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, IndTech.id(name), item);
    }

    public static void init() {
        // loads the class so the static fields above register the items
    }
}
