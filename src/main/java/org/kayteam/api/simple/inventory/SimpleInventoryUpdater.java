package org.kayteam.api.simple.inventory;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.kayteam.api.scheduler.ScheduleAction;

public class SimpleInventoryUpdater implements ScheduleAction {

    private final SimpleInventoryManager simpleInventoryManager;
    private final Player player;
    private final SimpleInventoryView simpleInventoryView;

    public SimpleInventoryUpdater(SimpleInventoryManager simpleInventoryManager, Player player, SimpleInventoryView simpleInventoryView) {
        this.simpleInventoryManager = simpleInventoryManager;
        this.player = player;
        this.simpleInventoryView = simpleInventoryView;
    }

    public SimpleInventoryManager getSimpleInventoryManager() {
        return simpleInventoryManager;
    }

    public Player getPlayer() {
        return player;
    }

    public SimpleInventoryView getSimpleInventoryView() {
        return simpleInventoryView;
    }

    @Override
    public boolean action(BukkitTask bukkitTask) {
        if (simpleInventoryManager.containInventoryView(player)) {
            simpleInventoryView.update();
            return false;
        } else {
            bukkitTask.cancel();
            return true;
        }
    }

}