package org.kayteam.api.simple.inventory;

import java.util.ArrayList;
import java.util.List;

public class Slot {

    private final int slot;

    private final List<Item> items;

    public Slot(int slot) {
        this.slot = slot;
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
        // TODO Order
    }

}