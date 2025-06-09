package ru.sidey383.worlgenerator.generator;

import org.bukkit.util.noise.NoiseGenerator;

import javax.annotation.Nullable;

public interface IslandShapeGenerator {

    boolean hasIslandInChunk(int chunkX, int chunkZ);

    @Nullable
    Integer bodyBottom(int x, int z, NoiseGenerator noiseGenerator);

    @Nullable
    Integer bodyTop(int x, int z, NoiseGenerator noiseGenerator);

}
