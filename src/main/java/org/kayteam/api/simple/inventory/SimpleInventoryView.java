package org.kayteam.api.simple.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.kayteam.api.simple.inventory.action.Action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SimpleInventoryView {

    private final Player player;
    private final Inventory inventory;
    private int updateInterval;
    private LinkedHashMap<Integer, Item> items = new LinkedHashMap<>();
    private List<Action> closeActions = new ArrayList<>();

    public SimpleInventoryView(Player player,  Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public LinkedHashMap<Integer, Item> getItems() {
        return items;
    }

    public List<Action> getCloseActions() {
        return closeActions;
    }

    public void update() {
        for (Item item:items.values()) {
            if (item.isUpdate()) {
                item.updateItem(player);
                inventory.setItem(item.getSlot(), item.getItemStack());
            }
        }
    }

}