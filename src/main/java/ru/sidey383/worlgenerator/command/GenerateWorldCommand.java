package ru.sidey383.worlgenerator.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.sidey383.worlgenerator.manager.WorldManager;

public class GenerateWorldCommand implements CommandExecutor {

    private final WorldManager worldManager;

    public GenerateWorldCommand(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length < 1) return false;
        String worldName = strings[0];
        World w = Bukkit.getWorld(worldName);
        if (w != null) {
            commandSender.sendMessage(Component.text("World %s already exists".formatted(worldName)));
            return false;
        }
        worldManager.createWorld(worldName);
        return true;
    }
}
