package org.kayteam.api.inventory;

import org.bukkit.scheduler.BukkitTask;
import org.kayteam.api.scheduler.ScheduleAction;

import java.util.HashMap;
import java.util.Objects;

public class InventoryUpdater implements ScheduleAction {

    private final InventoryManager inventoryManager;
    private final InventoryBuilder inventoryBuilder;

    private final HashMap<Integer, Integer> intervals = new HashMap<>();

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
            inventoryBuilder.onReload();
            for (int slot = 0; slot < (inventoryBuilder.getRows() * 9); slot++) {
                if (inventoryBuilder.getInventory().getSize() > slot) {
                    if (inventoryBuilder.isUpdatable(slot)) {
                        if (!intervals.containsKey(slot)) {
                            intervals.put(slot, inventoryBuilder.getUpdateInterval(slot));
                        }
                        int interval = intervals.get(slot);
                        if (interval == 0) {
                            intervals.put(slot, inventoryBuilder.getUpdateInterval(slot));
                            inventoryBuilder.getInventory().setItem(slot, inventoryBuilder.getItem(slot).getItem());
                        } else {
                            intervals.put(slot, interval - 1);
                        }
                    } else {
                        if (Objects.equals(inventoryBuilder.getInventory().getItem(slot), inventoryBuilder.getItem(slot).getItem())) {
                            inventoryBuilder.getInventory().setItem(slot, inventoryBuilder.getItem(slot).getItem());
                        }
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