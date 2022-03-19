package org.kayteam.api.inventory;

import org.bukkit.scheduler.BukkitTask;
import org.kayteam.api.scheduler.ScheduleAction;

public class InventoryUpdater implements ScheduleAction {

    private final InventoryManager inventoryManager;
    private final InventoryBuilder inventoryBuilder;


    public InventoryUpdater(InventoryManager inventoryManager, InventoryBuilder inventoryBuilder) {
        this.inventoryManager = inventoryManager;
        this.inventoryBuilder = inventoryBuilder;
    }

    /**
     * This action can be cancel task if this return true
     *
     * @return Cancel task if true
     */
    @Override
    public boolean action(BukkitTask bukkitTask) {
        if (inventoryManager.containInventoryBuilder(inventoryBuilder)) {
            for (int slot = 0; slot < (inventoryBuilder.getRows() * 9); slot++) {
                if (inventoryBuilder.getInventory().getSize() > slot) {
                    if (inventoryBuilder.isUpdateItem(slot)) {
                        inventoryBuilder.getInventory().setItem(slot, inventoryBuilder.getItem(slot).getItem());
                    }
                }
            }
            return false;
        } else {
            bukkitTask.cancel();
            return true;
        }
    }
}