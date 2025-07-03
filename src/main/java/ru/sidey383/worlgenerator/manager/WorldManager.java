package ru.sidey383.worlgenerator.manager;

import net.kyori.adventure.key.Key;
import net.thenextlvl.worlds.api.WorldsProvider;
import net.thenextlvl.worlds.api.generator.LevelStem;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import ru.sidey383.worlgenerator.generator.IslandChunkGenerator;
import ru.sidey383.worlgenerator.generator.LeakGenerator;
import ru.sidey383.worlgenerator.generator.SimpleIslandShapeGenerator;

import java.util.Random;

public class WorldManager {

    @Nullable
    public World createWorld(String name) {
        IslandChunkGenerator generator = new IslandChunkGenerator(
                new SimpleIslandShapeGenerator(150, 170, new Vector(0, 100, 0), 0.7),
                new LeakGenerator(0, 0, 100, 40, new Random())
        );
        WorldsProvider provider = Bukkit.getServicesManager().load(WorldsProvider.class);
        var level = provider.levelBuilder(Bukkit.getWorldContainer().toPath().resolve(name))
                .key(Key.key("skyisland", name))
                .chunkGenerator(generator)
                .structures(false)
                .levelStem(LevelStem.OVERWORLD)
                .build();

        return level.create().orElse(null);
    }

}
