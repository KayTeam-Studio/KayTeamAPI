package org.kayteam.api.simple.inventory.action;

import org.bukkit.entity.Player;
import org.kayteam.api.simple.inventory.SimpleInventoryView;

public abstract class Action {

    private final String name;
    private final ActionType actionType;

    public Action(String name, ActionType actionType) {
        this.name = name;
        this.actionType = actionType;
    }

    public abstract void execute(Player player, SimpleInventoryView simpleInventoryView);

}