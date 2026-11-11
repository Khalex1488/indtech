package net.indtech.energy;

import net.indtech.block.CableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EnergyNetwork {
    private static final int MAX_CABLES = 512;

    private EnergyNetwork() {
    }

    /**
     * Finds every energy device reachable from {@code origin} directly or through connected cables.
     */
    public static List<EnergyDevice> findDevices(World world, BlockPos origin) {
        List<EnergyDevice> result = new ArrayList<>();
        Set<BlockPos> visitedCables = new HashSet<>();
        Set<BlockPos> seenDevices = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();

        visitNeighbours(world, origin, origin, queue, visitedCables, seenDevices, result);

        while (!queue.isEmpty() && visitedCables.size() <= MAX_CABLES) {
            BlockPos cable = queue.poll();
            visitNeighbours(world, cable, origin, queue, visitedCables, seenDevices, result);
        }
        return result;
    }

    private static void visitNeighbours(World world, BlockPos from, BlockPos origin,
                                        ArrayDeque<BlockPos> queue, Set<BlockPos> visitedCables,
                                        Set<BlockPos> seenDevices, List<EnergyDevice> result) {
        for (Direction dir : Direction.values()) {
            BlockPos next = from.offset(dir);
            if (next.equals(origin)) {
                continue;
            }
            if (world.getBlockState(next).getBlock() instanceof CableBlock) {
                if (visitedCables.add(next)) {
                    queue.add(next);
                }
            } else {
                BlockEntity be = world.getBlockEntity(next);
                if (be instanceof EnergyDevice device && seenDevices.add(next)) {
                    result.add(device);
                }
            }
        }
    }
}
