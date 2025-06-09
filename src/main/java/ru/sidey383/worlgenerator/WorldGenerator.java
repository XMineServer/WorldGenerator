package ru.sidey383.worlgenerator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sidey383.worlgenerator.command.GenerateWorldCommand;
import ru.sidey383.worlgenerator.command.WorldMoveCommand;
import ru.sidey383.worlgenerator.manager.WorldManager;

public class WorldGenerator extends JavaPlugin {

    @Override
    public void onEnable() {
        WorldManager manager = new WorldManager();
        GenerateWorldCommand generateCommand = new GenerateWorldCommand(manager);
        WorldMoveCommand moveCommand = new WorldMoveCommand();
        Bukkit.getPluginCommand("generateworld").setExecutor(generateCommand);
        Bukkit.getPluginCommand("worldmove").setExecutor(moveCommand);
        Bukkit.getPluginCommand("worldmove").setTabCompleter(moveCommand);
    }
}
