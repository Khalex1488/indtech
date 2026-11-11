package net.indtech.block;

import com.mojang.serialization.MapCodec;
import net.indtech.block.entity.ElectricFurnaceBlockEntity;
import net.indtech.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ElectricFurnaceBlock extends MachineBlock {
    public static final MapCodec<ElectricFurnaceBlock> CODEC = createCodec(ElectricFurnaceBlock::new);

    public ElectricFurnaceBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<ElectricFurnaceBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricFurnaceBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlockEntities.ELECTRIC_FURNACE, ElectricFurnaceBlockEntity::tick);
    }
}
