package ru.sidey383.worlgenerator.generator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class IslandChunkGenerator extends ChunkGenerator {

    private final IslandShapeGenerator shapeGenerator;
    private final LeakGenerator leakGenerator;
    private final int LEAK_WATER_START = 97;

    public IslandChunkGenerator(IslandShapeGenerator shapeGenerator, LeakGenerator leakGenerator) {
        this.shapeGenerator = shapeGenerator;
        this.leakGenerator = leakGenerator;
    }

    public Integer getGroundHeight(int x, int z, NoiseGenerator islandNoise, NoiseGenerator leakNoise) {
        Integer leakDepth = leakGenerator.leakDeep(x, z, leakNoise);
        if (leakDepth != null) return LEAK_WATER_START - leakDepth;
        return shapeGenerator.bodyTop(x, z, islandNoise);
    }

    public double getInterpolateValue(int x, int z, NoiseGenerator islandNoise, NoiseGenerator leakNoise) {
        double value = 0;
        for (int x_ = -5; x_ <= 5; ++x_) {
            double subValue = 0;
            for (int z_ = -5; z_ <= 5; ++z_) {
                double weight = Blender.BASE_WEIGHTS[x_+5][z_+5];
                Integer level = getGroundHeight(x + x_, z + z_, islandNoise, leakNoise);
                subValue += weight * (level != null ? level : LEAK_WATER_START);
            }
            value += subValue;
        }
        return value / Blender.WEIGHT_SUM;
    }


    @Override
    public void generateNoise(WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        long seed = worldInfo.getSeed();
        if (!shapeGenerator.hasIslandInChunk(chunkX, chunkZ)) return;
        PerlinNoiseGenerator islandNoise = new PerlinNoiseGenerator(new Random(seed));
        PerlinNoiseGenerator leakNoise = new PerlinNoiseGenerator(new Random(seed + 1));
        chunkData.setRegion(0, worldInfo.getMinHeight(), 0, 16, worldInfo.getMaxHeight(), 16, Material.AIR);
        LeakGenerator.DeepInterpolate deepInterpolate = new LeakGenerator.DeepInterpolate();
        Vector noLeakBlock = new Vector(0, -1, 0);
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = x + (chunkX << 4);
                int absZ = z + (chunkZ << 4);
                Integer groundLevel = shapeGenerator.bodyTop(absX, absZ, islandNoise);
                Integer bottomLevel = shapeGenerator.bodyBottom(absX, absZ, islandNoise);
                if (bottomLevel == null || groundLevel == null || bottomLevel > groundLevel) continue;
                Integer waterDeep = leakGenerator.leakDeep(absX, absZ, leakNoise);
                int stoneLevel = groundLevel - 4;
                if (waterDeep != null) {
                    leakGenerator.nearestNoLeakBlock(absX, absZ, leakNoise, noLeakBlock);
                    int sandStart = LEAK_WATER_START - waterDeep;
                    if (noLeakBlock.getY() == 0) {
                        noLeakBlock.setY(-1);
                        sandStart = Math.min(LEAK_WATER_START, (int)Math.ceil(getInterpolateValue(absX, absZ, islandNoise, leakNoise)));
                    }
                    stoneLevel = sandStart - 2;
                    chunkData.setRegion(x, sandStart, z, x + 1, LEAK_WATER_START, z + 1, Material.WATER);
                    chunkData.setRegion(x, stoneLevel, z, x + 1, sandStart, z + 1, Material.SAND);
                } else {
                    leakGenerator.interpolateValue(absX, absZ, leakNoise, deepInterpolate);
                    if (deepInterpolate.hasInterpolate()) {
                        groundLevel = Math.max(LEAK_WATER_START, (int) Math.floor(getInterpolateValue(absX, absZ, islandNoise, leakNoise)));
                        stoneLevel = Math.min(stoneLevel, groundLevel - 1);
                        chunkData.setRegion(x, stoneLevel, z, x + 1, groundLevel, z + 1, Material.RED_SAND);
                    } else {
                        chunkData.setBlock(x, groundLevel, z, Material.GRASS_BLOCK);
                        chunkData.setRegion(x, stoneLevel, z, x + 1, groundLevel, z + 1, Material.DIRT);
                    }
                }
                chunkData.setRegion(x, bottomLevel, z, x+1, stoneLevel, z+1, Material.STONE);
            }
        }
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of(
                new OrePopulator(Material.IRON_ORE, 1, 30, 50, 30, 10, 0.2),
                new OrePopulator(Material.DIAMOND_ORE, 3, 5, 10, 10, 2.2, 0.05),
                new OrePopulator(Material.GOLD_ORE, 1, 3, 30, 40, 3.5, 0.1),
                new OrePopulator(Material.COAL_ORE, 10, 40, 55, 50, 3, 0.4)
        );
    }

}
