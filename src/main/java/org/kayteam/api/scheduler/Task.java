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

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public abstract class Task{

    private final JavaPlugin plugin;
    private BukkitTask task;
    private final long ticks;

    public Task(JavaPlugin plugin, long ticks){
        this.plugin = plugin;
        this.ticks = ticks;
    }

    public void startScheduler() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        task = scheduler.runTaskTimer(plugin, this::actions, 0L, ticks);
    }

    public void stopScheduler(){
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.cancelTask(task.getTaskId());
        stopActions();
    }

    public abstract void actions();

    public void stopActions(){}

    public boolean isRunning()
    {
        return plugin.getServer().getScheduler().isCurrentlyRunning(task.getTaskId());
    }
}
