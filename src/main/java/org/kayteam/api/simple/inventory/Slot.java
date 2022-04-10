package org.kayteam.api.simple.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Slot {

    private final int slot;

    private final List<Item> items = new ArrayList<>();

    public Slot(int slot) {
        this.slot = slot;
    }

    public void addItem(Item item) {
        items.add(item);
        items.sort((o1, o2) -> {
            if (o1.getPriority() > o2.getPriority()) return 1;
            else if (o1.getPriority() == o2.getPriority()) return 0;
            return -1;
        });
    }

    public List<Item> getItems() {
        return items;
    }

}