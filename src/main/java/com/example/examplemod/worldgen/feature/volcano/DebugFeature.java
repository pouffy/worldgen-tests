package com.example.examplemod.worldgen.feature.volcano;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DebugFeature extends Feature<NoneFeatureConfiguration> {

    public DebugFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelAccessor world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        List<BlockPos> tipPositions = new ArrayList<>();

        for (int coneIndex = 0; coneIndex < 5; coneIndex++) {
            BlockPos conePos = pos.offset(random.nextInt(12) - 1, 0, random.nextInt(12) - 1);

            int baseRadius = 23 + random.nextInt(8);
            int totalHeight = 24 + random.nextInt(12);
            int cutOffHeight = totalHeight - random.nextInt(6,12);

            for (int y = 0; y < cutOffHeight; y++) {
                int currentRadius = baseRadius - (y * baseRadius / cutOffHeight);

                for (int x = -currentRadius; x <= currentRadius; x++) {
                    for (int z = -currentRadius; z <= currentRadius; z++) {
                        if (x * x + z * z <= currentRadius * currentRadius) {
                            BlockPos blockPos = conePos.offset(x, y, z);
                            if (world.isEmptyBlock(blockPos)) {
                                world.setBlock(blockPos, Blocks.STONE.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }

            BlockPos tipPosition = conePos.offset(0, cutOffHeight - 1, 0);
            tipPositions.add(tipPosition);
        }

        createCrater(world, tipPositions, random);
        createCliffEdges(world, tipPositions, pos.getY(), random);

        return true;
    }

    private void createCrater(LevelAccessor world, List<BlockPos> tipPositions, RandomSource random) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        int tipHeight = tipPositions.get(0).getY();

        for (BlockPos tip : tipPositions) {
            minX = Math.min(minX, tip.getX());
            maxX = Math.max(maxX, tip.getX());
            minZ = Math.min(minZ, tip.getZ());
            maxZ = Math.max(maxZ, tip.getZ());
            tipHeight = Math.min(tipHeight, tip.getY());
        }

        BlockPos craterCenter = new BlockPos((minX + maxX) / 2, tipHeight + 1, (minZ + maxZ) / 2);
        int craterRadius = 4 + random.nextInt(3); // Radius of the crater

        for (int x = -craterRadius; x <= craterRadius; x++) {
            for (int z = -craterRadius; z <= craterRadius; z++) {
                if (x * x + z * z <= craterRadius * craterRadius) {
                    BlockPos blockPos = craterCenter.offset(x, 0, z);
                    if (world.isEmptyBlock(blockPos)) {
                        world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos lavaPos = craterCenter.offset(x, -1, z);
                if (world.isEmptyBlock(lavaPos)) {
                    world.setBlock(lavaPos, Blocks.LAVA.defaultBlockState(), 2);
                }
            }
        }
    }

    private void createCliffEdges(LevelAccessor world, List<BlockPos> basePositions, int baseYLevel, RandomSource random) {
        // Add smaller cones around the edges of the volcano
        for (BlockPos base : basePositions) {
            // Random smaller cone parameters
            int edgeHeight = 3 + random.nextInt(2); // Shorter height for cliff-edge cones
            int edgeBaseRadius = 2 + random.nextInt(2); // Smaller base radius

            // Calculate positions around the edge of the main cone
            for (int angle = 0; angle < 360; angle += 90) { // Change 90 to a smaller value for more cones
                double radian = Math.toRadians(angle);
                int offsetX = (int) (Math.cos(radian) * edgeBaseRadius * 1.5); // Spread the smaller cones out
                int offsetZ = (int) (Math.sin(radian) * edgeBaseRadius * 1.5);
                BlockPos edgePos = new BlockPos(base.getX() + offsetX, baseYLevel, base.getZ() + offsetZ); // Set Y to base level

                // Create smaller cone
                for (int y = 0; y < edgeHeight; y++) {
                    int currentRadius = edgeBaseRadius - (y * edgeBaseRadius / edgeHeight); // Tapering effect

                    for (int x = -currentRadius; x <= currentRadius; x++) {
                        for (int z = -currentRadius; z <= currentRadius; z++) {
                            if (x * x + z * z <= currentRadius * currentRadius) { // Circular base
                                BlockPos blockPos = edgePos.offset(x, y, z);
                                if (world.isEmptyBlock(blockPos)) {
                                    world.setBlock(blockPos, Blocks.STONE.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
