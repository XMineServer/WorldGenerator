package ru.sidey383.worlgenerator.generator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class IslandChunkGenerator extends ChunkGenerator {

    private static final int ELLIPSOID_HORIZONTAL_RADIUS = 100;
    private static final int ELLIPSOID_VERTICAL_RADIUS = 50;
    private static final Location location = new Location(null, 0, 100, 0);

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        chunkData.setRegion(0, worldInfo.getMinHeight(), 0, 16, worldInfo.getMaxHeight(), 16, Material.AIR);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int dirtLevel = location.getBlockY();
                int stoneLevel = location.getBlockY() - 5;
                int bedrockLevel = worldInfo.getMinHeight();
                chunkData.setBlock(x, dirtLevel, z, Material.GRASS_BLOCK);
                for (int y = dirtLevel - 1; y > stoneLevel; --y) {
                    chunkData.setBlock(x, y, z, Material.DIRT);
                }
                for (int y = stoneLevel; y > bedrockLevel; --y) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                chunkData.setBlock(x, bedrockLevel, z, Material.BEDROCK);
            }
        }
    }

    @Override
    public boolean shouldGenerateNoise() {
        return true; // Мы полностью контролируем генерацию
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false; // Отключаем стандартную генерацию поверхности
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return false; // Мы сами генерируем бедрок
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
                new OrePopulator(Material.IRON_ORE, 1, 7, 20, 70, 10, Material.STONE),
                new OrePopulator(Material.DIAMOND_ORE, 1, 2, 5, 30, 1.2, Material.STONE),
                new OrePopulator(Material.GOLD_ORE, 1, 3, 30, 60, 3.5, Material.STONE),
                new OrePopulator(Material.COAL_ORE, 1, 3, 50, 100, 20, Material.STONE)
        );
    }

}
