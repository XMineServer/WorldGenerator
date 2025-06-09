package ru.sidey383.worlgenerator.generator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class IslandChunkGenerator extends ChunkGenerator {

    private final IslandShapeGenerator shapeGenerator;

    public IslandChunkGenerator(IslandShapeGenerator shapeGenerator) {
        this.shapeGenerator = shapeGenerator;
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        long seed = worldInfo.getSeed();
        if (!shapeGenerator.hasIslandInChunk(chunkX, chunkZ)) return;
        PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator(new Random(seed));
        chunkData.setRegion(0, worldInfo.getMinHeight(), 0, 16, worldInfo.getMaxHeight(), 16, Material.AIR);
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = x + (chunkX << 4);
                int absZ = z + (chunkZ << 4);
                Integer bottomLevel = shapeGenerator.bodyBottom(absX, absZ, perlinNoiseGenerator);
                Integer topLevel = shapeGenerator.bodyTop(absX, absZ, perlinNoiseGenerator);
                if (bottomLevel == null || topLevel == null || bottomLevel > topLevel) continue;
                int stoneLevel = Math.max(bottomLevel, topLevel - 4);
                chunkData.setBlock(x, topLevel, z, Material.GRASS_BLOCK);
                chunkData.setRegion(x, stoneLevel, z, x+1, topLevel, z+1, Material.DIRT);
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
        return true;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of(
                new OrePopulator(Material.IRON_ORE, 1, 7, 20, 70, 10, Material.STONE),
                new OrePopulator(Material.DIAMOND_ORE, 1, 2, 5, 30, 1.2, Material.STONE),
                new OrePopulator(Material.GOLD_ORE, 1, 3, 30, 60, 3.5, Material.STONE),
                new OrePopulator(Material.COAL_ORE, 1, 3, 50, 100, 20, Material.STONE)
        );
    }

}
