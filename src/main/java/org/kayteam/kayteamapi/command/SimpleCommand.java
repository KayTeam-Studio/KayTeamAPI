package org.kayteam.kayteamapi.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SimpleCommand extends Command implements CommandExecutor, TabCompleter{

    private final String command;

    public SimpleCommand(String command) {
        super(command);
        this.command = command;
    }

    public void onPlayerExecute(Player sender, String[] args) {}
    public List<String> onPlayerTabComplete(Player sender, String[] args) { return new ArrayList<>(); }
    public  void onConsoleExecute(ConsoleCommandSender sender, String[] args) {}
    public List<String> onConsoleTabComplete(ConsoleCommandSender sender, String[] args) { return new ArrayList<>(); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            onPlayerExecute((Player) sender, args);
        } else {
            onConsoleExecute((ConsoleCommandSender) sender, args);
        }
        return true;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player) {
            onPlayerExecute((Player) sender, args);
        } else {
            onConsoleExecute((ConsoleCommandSender) sender, args);
        }
        return true;
    }

    public void registerCommand(JavaPlugin javaPlugin) {
        PluginCommand pluginCommand = javaPlugin.getCommand(command);
        if (pluginCommand == null) {
            try {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                commandMap.register(command, this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }


    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (sender instanceof Player) {
            return onPlayerTabComplete((Player) sender, args);
        } else {
            return onConsoleTabComplete((ConsoleCommandSender) sender, args);
        }
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        if (sender instanceof Player) {
            return onPlayerTabComplete((Player) sender, args);
        } else {
            return onConsoleTabComplete((ConsoleCommandSender) sender, args);
        }
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            return onPlayerTabComplete((Player) sender, args);
        } else {
            return onConsoleTabComplete((ConsoleCommandSender) sender, args);
        }
    }
}
