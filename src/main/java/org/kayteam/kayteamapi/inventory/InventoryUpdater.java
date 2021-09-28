package org.kayteam.kayteamapi.inventory;

import org.bukkit.Bukkit;
import org.kayteam.kayteamapi.scheduler.Task;

import java.util.HashMap;
import java.util.Objects;

public class InventoryUpdater extends Task {

    private final InventoryManager inventoryManager;
    private final InventoryBuilder inventoryBuilder;

    private HashMap<Integer, Integer> intervals = new HashMap<>();

    public InventoryUpdater(InventoryManager inventoryManager, InventoryBuilder inventoryBuilder) {
        super(inventoryManager.getJavaPlugin(), 1);
        this.inventoryManager = inventoryManager;
        this.inventoryBuilder = inventoryBuilder;
    }

    @Override
    public void actions() {
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
        } else {
            stopScheduler();
        }
    }

}