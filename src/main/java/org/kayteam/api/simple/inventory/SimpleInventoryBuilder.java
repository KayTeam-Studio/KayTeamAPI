/*
 *   Copyright (C) 2021 SirOswaldo
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.kayteam.api.simple.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.api.scheduler.SimpleScheduler;
import org.kayteam.api.simple.inventory.action.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SimpleInventoryBuilder {

    private final String name;
    private final String title;
    private final int rows;
    private final int updateInterval;
    private final LinkedHashMap<Integer, Slot> slots;
    private final List<Action> closeActions;

    public SimpleInventoryBuilder(String name) {
        this.name = name;
        title = "Default Title";
        rows = 3;
        this.updateInterval = 0;
        slots = new LinkedHashMap<>();
        closeActions = new ArrayList<>();
    }
    public SimpleInventoryBuilder(String name, String title) {
        this.name = name;
        this.title = title;
        rows = 3;
        this.updateInterval = 0;
        slots = new LinkedHashMap<>();
        closeActions = new ArrayList<>();
    }
    public SimpleInventoryBuilder(String name, String title, int rows) {
        this.name = name;
        this.title = title;
        this.rows = rows;
        this.updateInterval = 0;
        slots = new LinkedHashMap<>();
        closeActions = new ArrayList<>();
    }
    public SimpleInventoryBuilder(String name, String title, int rows, int updateInterval) {
        this.name = name;
        this.title = title;
        this.rows = rows;
        this.updateInterval = updateInterval;
        slots = new LinkedHashMap<>();
        closeActions = new ArrayList<>();
    }
    public SimpleInventoryBuilder(SimpleInventoryBuilder simpleInventoryBuilder) {
        this.name = simpleInventoryBuilder.getName();
        title = simpleInventoryBuilder.getTitle();
        rows = simpleInventoryBuilder.getRows();
        updateInterval = simpleInventoryBuilder.getUpdateInterval();
        slots = simpleInventoryBuilder.getSlots();
        closeActions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public LinkedHashMap<Integer, Slot> getSlots() {
        return slots;
    }

    public List<Action> getCloseActions() {
        return closeActions;
    }

    public SimpleInventoryView generateInventoryView(JavaPlugin javaPlugin, Player player) {
        // todo Generar el SimpleInventoryView
        // todo Colocar prioridades
        // todo y view requirements
        Inventory inventory = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', title));
        // Starting Items load
        for (int slot = 0; slot < (rows * 9); slot++) {
            if (inventory.getSize() > slot) {
                SimpleScheduler addItem = new SimpleScheduler(javaPlugin, 1);
                int finalSlot = slot;
                addItem.addScheduleAction(bukkitTask -> {
                    //inventory.setItem(finalSlot, getSlots().get(finalSlot).getItemStack());
                    return true;
                });
                addItem.runSchedulerLater(true);
            } else break;
        }
        // Ending Items load
        SimpleInventoryView simpleInventoryView = new SimpleInventoryView(player, inventory);
        // todo actions
        return simpleInventoryView;
    }

}