package org.kayteam.api.simple.inventory.action.actions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kayteam.api.simple.inventory.SimpleInventoryView;
import org.kayteam.api.simple.inventory.action.Action;
import org.kayteam.api.simple.inventory.action.ActionType;

public class PlayerAction extends Action {

    private final String command;

    public PlayerAction(String name, String command) {
        super(name, ActionType.CONSOLE);
        this.command = command;
    }

    @Override
    public void execute(Player player, SimpleInventoryView simpleInventoryView) {
        String finalCommand = command;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) finalCommand = PlaceholderAPI.setPlaceholders(player, finalCommand);
        Bukkit.getServer().dispatchCommand(player, finalCommand);
    }

}