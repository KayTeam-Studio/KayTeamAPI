/*
 *   Copyright (C) 2021 SirOswaldo
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.kayteam.kayteamapi.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleCommand implements CommandExecutor, TabCompleter {

    public SimpleCommand(JavaPlugin javaPlugin, String command) {
        PluginCommand pluginCommand = javaPlugin.getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
            javaPlugin.getLogger().info("The command '" + command + "' registered");
        } else {
            javaPlugin.getLogger().info("The command '" + command + "' no can registered");
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            onPlayerExecute((Player) commandSender, arguments);
        } else {
            onConsoleExecute((ConsoleCommandSender) commandSender, command, arguments);
        }
        return true;
    }
    public void onPlayerExecute(Player player, String[] arguments) {}
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {}

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            return onPlayerTabComplete((Player) commandSender, command, arguments);
        } else {
            return onConsoleTabComplete((ConsoleCommandSender) commandSender, command, arguments);
        }
    }
    public List<String> onPlayerTabComplete(Player player, Command command, String[] arguments)  {
        return new ArrayList<>();
    }
    public List<String> onConsoleTabComplete(ConsoleCommandSender console, Command command, String[] arguments)  { return new ArrayList<>(); }

}
