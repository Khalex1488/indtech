package net.indtech.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.block.ShapeContext;
import net.minecraft.world.WorldView;

import java.util.Map;

public class CableBlock extends Block {
    public static final BooleanProperty NORTH = ConnectingBlock.NORTH;
    public static final BooleanProperty EAST = ConnectingBlock.EAST;
    public static final BooleanProperty SOUTH = ConnectingBlock.SOUTH;
    public static final BooleanProperty WEST = ConnectingBlock.WEST;
    public static final BooleanProperty UP = ConnectingBlock.UP;
    public static final BooleanProperty DOWN = ConnectingBlock.DOWN;
    private static final Map<Direction, BooleanProperty> PROPERTIES = ConnectingBlock.FACING_PROPERTIES;

    private static final VoxelShape CORE = Block.createCuboidShape(6, 6, 6, 10, 10, 10);
    private static final VoxelShape ARM_DOWN = Block.createCuboidShape(6, 0, 6, 10, 6, 10);
    private static final VoxelShape ARM_UP = Block.createCuboidShape(6, 10, 6, 10, 16, 10);
    private static final VoxelShape ARM_NORTH = Block.createCuboidShape(6, 6, 0, 10, 10, 6);
    private static final VoxelShape ARM_SOUTH = Block.createCuboidShape(6, 6, 10, 10, 10, 16);
    private static final VoxelShape ARM_WEST = Block.createCuboidShape(0, 6, 6, 6, 10, 10);
    private static final VoxelShape ARM_EAST = Block.createCuboidShape(10, 6, 6, 16, 10, 10);

    public CableBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(NORTH, false).with(EAST, false).with(SOUTH, false)
                .with(WEST, false).with(UP, false).with(DOWN, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = getDefaultState();
        for (Direction dir : Direction.values()) {
            BlockPos neighbourPos = ctx.getBlockPos().offset(dir);
            state = state.with(PROPERTIES.get(dir),
                    connectsTo(ctx.getWorld().getBlockState(neighbourPos)));
        }
        return state;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                   WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state.with(PROPERTIES.get(direction), connectsTo(neighborState));
    }

    private static boolean connectsTo(BlockState neighbour) {
        return neighbour.getBlock() instanceof CableBlock || neighbour.getBlock() instanceof MachineBlock;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = CORE;
        if (state.get(DOWN)) shape = VoxelShapes.union(shape, ARM_DOWN);
        if (state.get(UP)) shape = VoxelShapes.union(shape, ARM_UP);
        if (state.get(NORTH)) shape = VoxelShapes.union(shape, ARM_NORTH);
        if (state.get(SOUTH)) shape = VoxelShapes.union(shape, ARM_SOUTH);
        if (state.get(WEST)) shape = VoxelShapes.union(shape, ARM_WEST);
        if (state.get(EAST)) shape = VoxelShapes.union(shape, ARM_EAST);
        return shape;
    }
}
