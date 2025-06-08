package ru.sidey383.worlgenerator.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorldMoveCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length < 1) return false;
        if (!(commandSender instanceof Entity e)) {
            return false;
        }
        String worldName = strings[0];
        World w = Bukkit.getWorld(worldName);
        if (w == null) {
            commandSender.sendMessage(Component.text("Can't found world %s".formatted(worldName)));
            return false;
        }
        e.teleport(new Location(w, 0, 200, 0));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String @NotNull [] strings) {
        if (strings.length <= 1) {
            return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .filter(name -> strings.length == 0 || name.toLowerCase().startsWith(strings[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
