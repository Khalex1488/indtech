package net.indtech.block;

import com.mojang.serialization.MapCodec;
import net.indtech.block.entity.GeneratorBlockEntity;
import net.indtech.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeneratorBlock extends MachineBlock {
    public static final MapCodec<GeneratorBlock> CODEC = createCodec(GeneratorBlock::new);

    public GeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<GeneratorBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GeneratorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlockEntities.GENERATOR, GeneratorBlockEntity::tick);
    }
}
