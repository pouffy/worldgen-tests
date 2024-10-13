package com.example.examplemod.worldgen.feature.ice_spike;

import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class IceSpikeFeature extends Feature<NoneFeatureConfiguration> {

    public IceSpikeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos pos = context.origin();

        boolean large = true;
        int tipMin = (int) ((large ? 75 : 60) * 0.8);
        int tipRand = (int) ((large ? 95 : 75) * 0.6);
        int radiusMin = large ? 7 : 3;
        int radiusRand = large ? 4 : 1;

        int tip = tipMin + worldgenlevel.getRandom().nextInt(tipRand);
        int topX = worldgenlevel.getRandom().nextInt(tip) - tip / 2;
        int topZ = worldgenlevel.getRandom().nextInt(tip) - tip / 2;

        int tipHeight = (int) (tip * 0.87);

        int radius = radiusMin + worldgenlevel.getRandom().nextInt(radiusRand);
        Vec3 to = new Vec3(pos.getX() + topX, pos.getY() + tipHeight, pos.getZ() + topZ);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double fromCenter = Math.sqrt(x * x + z * z);
                if (fromCenter <= radius) {
                    Vec3 from = new Vec3(pos.getX() + x, pos.getY(), pos.getZ() + z);

                    if (worldgenlevel.getBlockState(posFromVec(from).below()).isAir()) {
                        continue;
                    }

                    Vec3 per = to.subtract(from).normalize();
                    Vec3 current = from.add(0, 0, 0);
                    double distance = from.distanceTo(to);

                    for (double i = 0; i < distance; i += 0.5) {
                        BlockPos targetPos = posFromVec(current);
                        if (i > 0 && i < distance / 1.3) {
                            int roll = randomsource.nextInt(3);
                            if (roll == 0) {
                                worldgenlevel.setBlock(targetPos, Blocks.PACKED_ICE.defaultBlockState(), 3);
                            } else if (roll == 1) {
                                worldgenlevel.setBlock(targetPos, Blocks.BLUE_ICE.defaultBlockState(), 3);
                            } else if (roll == 2) {
                                worldgenlevel.setBlock(targetPos, Blocks.ICE.defaultBlockState(), 3);
                            }
                        } else {
                            worldgenlevel.setBlock(targetPos, Blocks.ICE.defaultBlockState(), 3);
                        }

                        if (i <= 0) {
                            BlockPos getFromTarget = targetPos;
                            while (worldgenlevel.isEmptyBlock(getFromTarget.below())) {
                                if (randomsource.nextBoolean()) {
                                    worldgenlevel.setBlock(getFromTarget, Blocks.BLUE_ICE.defaultBlockState(), 3);
                                } else {
                                    worldgenlevel.setBlock(getFromTarget, Blocks.PACKED_ICE.defaultBlockState(), 3);
                                }
                                getFromTarget = getFromTarget.below();
                            }
                        }

                        // Use a smaller vertical step
                        current = current.add(per.x * 0.5, per.y * 0.2, per.z * 0.5);
                    }
                }
            }
        }

        // After the main spike generation loop
        placeSnowCover(worldgenlevel, pos, tipHeight, 59, randomsource); // Place snow cover on the spike
        placeIcicles(worldgenlevel, pos, 59, tipHeight, randomsource); // Place icicles around the base
        createHangingSnow(worldgenlevel, pos, 59, tipHeight, randomsource); // Place hanging snow structures

        return true;
    }

    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }

    public void placeSnowCover(WorldGenLevel worldgenlevel, BlockPos pos, int tipHeight, int radius, RandomSource randomsource) {
        int tipY = pos.getY() + tipHeight; // Y-coordinate of the tip of the spike

        // Create a plane of snow at the tip height
        //for (int x = -radius; x <= radius; x++) {
        //    for (int z = -radius; z <= radius; z++) {
        //        // Check if within circular area for placing snow
        //        if (Math.sqrt(x * x + z * z) <= radius) {
        //            BlockPos snowPos = new BlockPos(pos.getX() + x, tipY, pos.getZ() + z);
        //            worldgenlevel.setBlock(snowPos, Blocks.SNOW_BLOCK.defaultBlockState(), 3); // Place snow block
        //        }
        //    }
        //}

        // Now, iterate downwards to find ice blocks
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Check if within circular area for placing snow
                if (Math.sqrt(x * x + z * z) <= radius) {
                    // Start from the tip height and go down
                    for (int y = tipY; y >= pos.getY(); y--) {
                        BlockPos checkPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);

                        // Check if the block below is ice
                        if (worldgenlevel.getBlockState(checkPos).is(Blocks.BLUE_ICE) || worldgenlevel.getBlockState(checkPos).is(Blocks.PACKED_ICE) || worldgenlevel.getBlockState(checkPos).is(Blocks.ICE)) {
                            // Place a snow block above the found ice block
                            BlockPos snowAbovePos = checkPos.above();
                            if (worldgenlevel.isEmptyBlock(snowAbovePos)) {
                                worldgenlevel.setBlock(snowAbovePos, Blocks.SNOW_BLOCK.defaultBlockState(), 3); // Place snow block
                                worldgenlevel.setBlock(snowAbovePos.above(), Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, randomsource.nextInt(1, 4)), 3); // Remove the ice block
                            }
                            break; // Stop once we find and place snow above the first ice block
                        }
                    }
                }
            }
        }
    }

    public void placeIcicles(WorldGenLevel worldgenlevel, BlockPos pos, int radius, int tipHeight, RandomSource randomsource) {
        // Determine the Y position for the underside of the spike
        int tipY = pos.getY() + tipHeight; // Y-coordinate of the tip of the spike

        BlockState ice = Blocks.ICE.defaultBlockState(); // BlockState for the icicle [Debug uses emerald block for visibility]
        int roll = randomsource.nextInt(2);
        if (roll == 1) {
            ice = Blocks.PACKED_ICE.defaultBlockState();
        } else if (roll == 2) {
            ice = Blocks.BLUE_ICE.defaultBlockState();
        }

        int baseY = pos.getY(); // Base Y-coordinate of the spike
        int icicleLengthMin = 2; // Minimum length of the icicle
        int icicleLengthMax = 5; // Maximum length of the icicle
        int icicleDensity = 4; // Density of potential icicles

        // Iterate over positions around the base of the spike to create icicles
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Check if within circular area for placing snow
                if (Math.sqrt(x * x + z * z) <= radius) {
                    // Start from the tip height and go down
                    for (int y = tipY; y >= pos.getY(); y--) {
                        BlockPos checkPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);

                        // Check if there is an ice block above the base position
                        if (worldgenlevel.isEmptyBlock(checkPos.above()) && (worldgenlevel.getBlockState(checkPos.above(2)).is(Blocks.ICE) || worldgenlevel.getBlockState(checkPos.above(2)).is(Blocks.PACKED_ICE) || worldgenlevel.getBlockState(checkPos.above(2)).is(Blocks.BLUE_ICE))) {
                            // Randomly decide to create an icicle
                            if (worldgenlevel.getRandom().nextInt(icicleDensity) == 0) {
                                int icicleLength = icicleLengthMin + worldgenlevel.getRandom().nextInt(icicleLengthMax - icicleLengthMin + 1);

                                for (int k = 0; k < icicleLength; k++) {
                                    BlockPos iciclePos = checkPos.below((k + 1) -2);
                                    if (worldgenlevel.isEmptyBlock(iciclePos)) {
                                        worldgenlevel.setBlock(iciclePos, ice, 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void createHangingSnow(WorldGenLevel worldgenlevel, BlockPos pos, int radius, int tipHeight, RandomSource randomsource) {
        BlockState snow = Blocks.SNOW_BLOCK.defaultBlockState(); // BlockState for the snow [Debug uses gold block for visibility]
        int tipY = pos.getY() + tipHeight;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Check if within circular area for placing snow
                if (Math.sqrt(x * x + z * z) <= radius) {
                    // Start from the tip height and go down
                    for (int y = tipY; y >= pos.getY(); y--) {
                        BlockPos checkPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);

                        //iterate all blocks beside the checked pos and check if one is snow
                        boolean besideSnow = false;
                        boolean iceBelow = false;
                        for (Direction direction : Direction.values()) {
                            BlockPos besidePos = checkPos.relative(direction);
                            if (worldgenlevel.getBlockState(besidePos).is(Blocks.SNOW_BLOCK)) {
                                besideSnow = true;
                                break;
                            }

                        }
                        for (Direction direction : Direction.values()) {
                            BlockPos besidePos = checkPos.relative(direction);
                            if (besideSnow && (worldgenlevel.getBlockState(besidePos.below()).is(Blocks.ICE) || worldgenlevel.getBlockState(besidePos.below()).is(Blocks.PACKED_ICE) || worldgenlevel.getBlockState(besidePos.below()).is(Blocks.BLUE_ICE))) {
                                iceBelow = true;
                                break;
                            }
                        }

                        boolean surroundedByAir = false;

                        for (Direction direction : Direction.values()) {
                            BlockPos besidePos = checkPos.relative(direction);
                            if (worldgenlevel.isEmptyBlock(besidePos)) {
                                surroundedByAir = true;
                                break;
                            }
                        }

                        if (besideSnow && iceBelow && !surroundedByAir) {
                            //pick a random direction beside the checked pos and place snow
                            Direction direction = Direction.values()[randomsource.nextInt(Direction.values().length)];
                            if (direction != Direction.UP && direction != Direction.DOWN && !worldgenlevel.getBlockState(checkPos.below()).is(Blocks.SNOW)) {
                                BlockPos besidePos = checkPos.relative(direction);
                                if (worldgenlevel.isEmptyBlock(besidePos)) {
                                    for (int i = 0; i < randomsource.nextInt(3); i++) {
                                        BlockPos checkPos2 = besidePos.below(i);
                                        if (worldgenlevel.isEmptyBlock(checkPos2) && !worldgenlevel.getBlockState(checkPos2.below()).is(Blocks.SNOW) && !worldgenlevel.getBlockState(checkPos2.below(i)).is(Blocks.SNOW)) {
                                            worldgenlevel.setBlock(checkPos2, snow, 3);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
