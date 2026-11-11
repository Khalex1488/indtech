package net.indtech.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.indtech.block.MachineBlock;
import net.indtech.energy.EnergyDevice;
import net.indtech.registry.ModBlockEntities;
import net.indtech.screen.ElectricFurnaceScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ElectricFurnaceBlockEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory, EnergyDevice {

    public static final int MAX_ENERGY = 1000;
    public static final int ENERGY_PER_TICK = 3;
    public static final int TOTAL_PROGRESS = 130;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private int energy;
    private int progress;

    private final PropertyDelegate delegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energy;
                case 1 -> MAX_ENERGY;
                case 2 -> progress;
                case 3 -> TOTAL_PROGRESS;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> energy = value;
                case 2 -> progress = value;
                default -> {
                }
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRIC_FURNACE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ElectricFurnaceBlockEntity be) {
        boolean changed = false;
        boolean working = false;

        ItemStack input = be.inventory.get(0);
        if (!input.isEmpty()) {
            Optional<RecipeEntry<SmeltingRecipe>> recipe = world.getRecipeManager()
                    .getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(input), world);

            if (recipe.isPresent()) {
                ItemStack result = recipe.get().value().getResult(world.getRegistryManager());
                if (be.canOutput(result) && be.energy >= ENERGY_PER_TICK) {
                    be.energy -= ENERGY_PER_TICK;
                    be.progress++;
                    working = true;
                    changed = true;

                    if (be.progress >= TOTAL_PROGRESS) {
                        be.progress = 0;
                        input.decrement(1);
                        ItemStack out = be.inventory.get(1);
                        if (out.isEmpty()) {
                            be.inventory.set(1, result.copy());
                        } else {
                            out.increment(result.getCount());
                        }
                    }
                }
            }
        }

        if (!working && be.progress != 0) {
            be.progress = 0;
            changed = true;
        }

        boolean wasLit = state.get(MachineBlock.LIT);
        if (wasLit != working) {
            world.setBlockState(pos, state.with(MachineBlock.LIT, working), Block.NOTIFY_ALL);
            changed = true;
        }
        if (changed) {
            be.markDirty();
        }
    }

    private boolean canOutput(ItemStack result) {
        if (result.isEmpty()) {
            return false;
        }
        ItemStack out = inventory.get(1);
        if (out.isEmpty()) {
            return true;
        }
        return ItemStack.areItemsAndComponentsEqual(out, result)
                && out.getCount() + result.getCount() <= out.getMaxCount();
    }

    @Override
    public int receiveEnergy(int amount) {
        int accepted = Math.max(0, Math.min(amount, MAX_ENERGY - energy));
        if (accepted > 0) {
            energy += accepted;
            markDirty();
        }
        return accepted;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.indtech.electric_furnace");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ElectricFurnaceScreenHandler(syncId, playerInventory, this, delegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        Inventories.writeNbt(nbt, inventory, lookup);
        nbt.putInt("Energy", energy);
        nbt.putInt("Progress", progress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        Inventories.readNbt(nbt, inventory, lookup);
        energy = nbt.getInt("Energy");
        progress = nbt.getInt("Progress");
    }
}
