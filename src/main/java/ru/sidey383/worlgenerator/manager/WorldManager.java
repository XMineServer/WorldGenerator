package ru.sidey383.worlgenerator.manager;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

public class WorldManager {

    private final ChunkGenerator generator;

    public WorldManager(ChunkGenerator chunkGenerator) {
        this.generator = chunkGenerator;
    }

    public World createWorld(String name) {
        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.generator(generator);
        worldCreator.generateStructures(false);
        return worldCreator.createWorld();
    }

}
