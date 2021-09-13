package org.kayteam.kayteamapi.inventory;

import org.kayteam.kayteamapi.scheduler.Task;

import java.util.Objects;

public class InventoryUpdater extends Task {

    private final InventoryManager inventoryManager;
    private final InventoryBuilder inventoryBuilder;

    public InventoryUpdater(InventoryManager inventoryManager, InventoryBuilder inventoryBuilder) {
        super(inventoryManager.getJavaPlugin(), 20);
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
                        inventoryBuilder.getInventory().setItem(slot, inventoryBuilder.getItem(slot).getItem());
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