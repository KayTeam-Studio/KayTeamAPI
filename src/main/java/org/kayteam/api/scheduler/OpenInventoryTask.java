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

package org.kayteam.api.scheduler;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class OpenInventoryTask extends Task {

    private final Player player;
    private final Inventory inventory;

    public OpenInventoryTask(JavaPlugin plugin, Player player, Inventory inventory, long ticks) {
        super(plugin, ticks);
        this.player = player;
        this.inventory = inventory;
    }

    @Override
    public void actions() {
        if (player.isOnline()) {
            player.openInventory(inventory);
        }
        stopScheduler();
    }

}