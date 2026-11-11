package net.indtech.block.entity;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.indtech.energy.EnergyDevice;
import net.indtech.energy.EnergyNetwork;
import net.indtech.registry.ModBlockEntities;
import net.indtech.screen.GeneratorScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class GeneratorBlockEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory, EnergyDevice {

    public static final int MAX_ENERGY = 4000;
    public static final int OUTPUT_PER_TICK = 10;   // EU produced each tick while burning
    public static final int TRANSFER_PER_TICK = 32; // EU pushed into the network each tick

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int energy;
    private int burnTime;
    private int maxBurnTime;

    private final PropertyDelegate delegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energy;
                case 1 -> MAX_ENERGY;
                case 2 -> burnTime;
                case 3 -> maxBurnTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> energy = value;
                case 2 -> burnTime = value;
                case 3 -> maxBurnTime = value;
                default -> {
                }
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GENERATOR, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, GeneratorBlockEntity be) {
        boolean changed = false;
        boolean wasLit = state.get(net.indtech.block.MachineBlock.LIT);

        // Start burning a new fuel item if there is room for the energy
        if (be.burnTime <= 0 && be.energy + OUTPUT_PER_TICK <= MAX_ENERGY) {
            ItemStack fuel = be.inventory.get(0);
            if (!fuel.isEmpty()) {
                Integer time = FuelRegistry.INSTANCE.get(fuel.getItem());
                if (time != null && time > 0) {
                    be.burnTime = time;
                    be.maxBurnTime = time;
                    if (fuel.isOf(Items.LAVA_BUCKET)) {
                        be.inventory.set(0, new ItemStack(Items.BUCKET));
                    } else {
                        fuel.decrement(1);
                    }
                    changed = true;
                }
            }
        }

        // Burn and produce energy
        if (be.burnTime > 0 && be.energy + OUTPUT_PER_TICK <= MAX_ENERGY) {
            be.burnTime--;
            be.energy += OUTPUT_PER_TICK;
            changed = true;
        }

        // Push energy to machines through the cable network
        if (be.energy > 0) {
            List<EnergyDevice> devices = EnergyNetwork.findDevices(world, pos);
            for (EnergyDevice device : devices) {
                if (be.energy <= 0) {
                    break;
                }
                int accepted = device.receiveEnergy(Math.min(be.energy, TRANSFER_PER_TICK));
                if (accepted > 0) {
                    be.energy -= accepted;
                    changed = true;
                }
            }
        }

        boolean lit = be.burnTime > 0;
        if (lit != wasLit) {
            world.setBlockState(pos, state.with(net.indtech.block.MachineBlock.LIT, lit), Block.NOTIFY_ALL);
            changed = true;
        }
        if (changed) {
            be.markDirty();
        }
    }

    @Override
    public int receiveEnergy(int amount) {
        return 0; // the generator only produces
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return FuelRegistry.INSTANCE.get(stack.getItem()) != null;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.indtech.generator");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GeneratorScreenHandler(syncId, playerInventory, this, delegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        Inventories.writeNbt(nbt, inventory, lookup);
        nbt.putInt("Energy", energy);
        nbt.putInt("BurnTime", burnTime);
        nbt.putInt("MaxBurnTime", maxBurnTime);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        Inventories.readNbt(nbt, inventory, lookup);
        energy = nbt.getInt("Energy");
        burnTime = nbt.getInt("BurnTime");
        maxBurnTime = nbt.getInt("MaxBurnTime");
    }
}
