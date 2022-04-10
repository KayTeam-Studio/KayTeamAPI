package org.kayteam.api.simple.inventory.action.actions;

import org.bukkit.entity.Player;
import org.kayteam.api.simple.inventory.SimpleInventoryBuilder;
import org.kayteam.api.simple.inventory.SimpleInventoryView;
import org.kayteam.api.simple.inventory.action.Action;
import org.kayteam.api.simple.inventory.action.ActionType;

public class CloseAction extends Action {

    public CloseAction(String name) {
        super(name, ActionType.CLOSE);
    }

    @Override
    public void execute(Player player, SimpleInventoryView simpleInventoryView) {
        player.closeInventory();
    }

}