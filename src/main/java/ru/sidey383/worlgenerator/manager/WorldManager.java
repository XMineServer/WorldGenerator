package ru.sidey383.worlgenerator.manager;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.util.Vector;
import ru.sidey383.worlgenerator.generator.IslandChunkGenerator;
import ru.sidey383.worlgenerator.generator.SimpleIslandShapeGenerator;

public class WorldManager {

    public World createWorld(String name) {
        IslandChunkGenerator generator = new IslandChunkGenerator(
                new SimpleIslandShapeGenerator(50, 30, new Vector(0, 100, 0), 0.7)
        );
        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.generator(generator);
        worldCreator.generateStructures(false);
        return worldCreator.createWorld();
    }

}
