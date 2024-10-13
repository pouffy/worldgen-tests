package com.example.examplemod.worldgen.feature.volcano;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class VolcanoFeature extends Feature<NoneFeatureConfiguration> {

    public VolcanoFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos pos = context.origin();

        int radiusMin = 13;
        int radiusRand = 9;

        int tipMin = (int) (25 * 0.8);
        int tipRand = (int) (25 * 0.6);

        int tip = tipMin + worldgenlevel.getRandom().nextInt(tipRand);
        int tipHeight = (int) (tip * 0.87);

        int radius = radiusMin + worldgenlevel.getRandom().nextInt(radiusRand);
        Vec3 tipPosition = new Vec3(pos.getX(), pos.getY() + tipHeight, pos.getZ());

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double fromCenter = Math.sqrt(x * x + z * z);
                if (fromCenter <= radius) {
                    Vec3 from = new Vec3(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    Vec3 per = tipPosition.subtract(from).normalize();
                    Vec3 current = from;

                    double distance = from.distanceTo(tipPosition);

                    for (double i = 0; i < distance; i += 0.5) {
                        BlockPos targetPos = posFromVec(current);
                        if (i > 0 && i < distance / 1.3) {
                            worldgenlevel.setBlock(targetPos, Blocks.BLACKSTONE.defaultBlockState(), 3);
                        } else {
                            createLavaPit(targetPos, worldgenlevel);
                            worldgenlevel.setBlock(reversePos(targetPos), Blocks.AIR.defaultBlockState(), 3);
                        }

                        current = current.add(per.x * 0.5, per.y * 0.5, per.z * 0.5); // Scale the movement by a smaller amount
                    }
                }
            }
        }

        boolean steepSlope = randomsource.nextBoolean();
        for (int depth = 0; depth < 120; depth++) {
            int currentRadius = radius;

            if (steepSlope) {
                currentRadius += depth;
            } else {
                currentRadius += depth / 2;
            }

            for (int angle = 0; angle < 360; angle += 15) {
                int dx = (int) (currentRadius * Math.cos(Math.toRadians(angle)));
                int dz = (int) (currentRadius * Math.sin(Math.toRadians(angle)));

                BlockPos newPos = pos.below(depth).offset(dx, 0, dz);

                if (worldgenlevel.isEmptyBlock(newPos)) {
                    worldgenlevel.setBlock(newPos, Blocks.BLACKSTONE.defaultBlockState(), 3);
                }
            }

            for (int x = -currentRadius; x <= currentRadius; x++) {
                for (int z = -currentRadius; z <= currentRadius; z++) {
                    double fromCenter = Math.sqrt(x * x + z * z);
                    if (fromCenter <= currentRadius) {
                        BlockPos targetPos = pos.below(depth).offset(x, 0, z);
                        if (worldgenlevel.isEmptyBlock(targetPos)) {
                            worldgenlevel.setBlock(targetPos, Blocks.BLACKSTONE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }



        return true;
    }

    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }

    public BlockPos reversePos(BlockPos pos) {
        return new BlockPos(-pos.getX(), -pos.getY(), -pos.getZ());
    }

    public void createLavaPit(BlockPos origin, WorldGenLevel worldgenlevel) {
        int radius = 3;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y >= -3; y--) {
                    double fromCenter = Math.sqrt(x * x + z * z);
                    if (fromCenter <= radius) {
                        BlockPos targetPos = origin.offset(x, y, z);
                        if (worldgenlevel.getBlockState(targetPos).getBlock() == Blocks.BLACKSTONE) {
                            worldgenlevel.setBlock(targetPos, Blocks.EMERALD_BLOCK.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }
}
