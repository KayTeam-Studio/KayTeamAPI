package org.kayteam.kayteamapi.inventory;

import org.kayteam.kayteamapi.scheduler.Task;

import java.util.Objects;

public class InventoryUpdater extends Task {

    private final InventoryManager inventoryManager;
    private final InventoryBuilder inventoryBuilder;

    private int interval = 0;
    private int maxInterval = 0;

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
                        if (inventoryBuilder.getUpdateInterval(slot) == interval) {
                            inventoryBuilder.getInventory().setItem(slot, inventoryBuilder.getItem(slot).getItem());
                        } else {
                            if (inventoryBuilder.getUpdateInterval(slot) > maxInterval) {
                                maxInterval = inventoryBuilder.getUpdateInterval(slot);
                            }
                        }
                    } else {
                        if (Objects.equals(inventoryBuilder.getInventory().getItem(slot), inventoryBuilder.getItem(slot).getItem())) {
                            inventoryBuilder.getInventory().setItem(slot, inventoryBuilder.getItem(slot).getItem());
                        }
                    }
                }
            }
            interval++;
            if (interval >= maxInterval) {
                interval = 0;
            }
        } else {
            stopScheduler();
        }
    }

}