package org.kayteam.api.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class SimpleScheduler {

    private final JavaPlugin javaPlugin;

    private BukkitTask bukkitTask;

    private final int ticks;

    List<ScheduleAction> actions = new ArrayList<>();

    public SimpleScheduler(JavaPlugin javaPlugin, int ticks) {
        this.javaPlugin = javaPlugin;
        this.ticks = ticks;
    }

    public void addScheduleAction(ScheduleAction scheduleAction) {
        actions.add(scheduleAction);
    }

    public void removeScheduleAction(int index) {
        actions.remove(index);
    }

    public ScheduleAction getScheduleAction(int index) {
        return actions.get(index);
    }

    public int getActionsSize() {
        return actions.size();
    }

    public List<ScheduleAction> getSchedulerActions() {
        return actions;
    }

    public void runSchedulerTimer() {
        runSchedulerTimer(false);
    }

    public void runSchedulerTimer(boolean async) {
        if (bukkitTask != null) {
            stopScheduler();
        }
        if (async) {
            bukkitTask = javaPlugin.getServer().getScheduler().runTaskTimerAsynchronously(javaPlugin, this::actions, 0, ticks);
        } else {
            bukkitTask = javaPlugin.getServer().getScheduler().runTaskTimer(javaPlugin, this::actions, 0, ticks);
        }
    }

    public void runSchedulerLater() {
        runSchedulerLater(false);
    }

    public void runSchedulerLater(boolean async) {
        if (bukkitTask != null) {
            stopScheduler();
        }
        if (async) {
            bukkitTask = javaPlugin.getServer().getScheduler().runTaskLaterAsynchronously(javaPlugin, this::actions, ticks);
        } else {
            bukkitTask = javaPlugin.getServer().getScheduler().runTaskLater(javaPlugin, this::actions, ticks);
        }
    }

    public void stopScheduler() {
        if(bukkitTask != null) {
            bukkitTask.cancel();
        }
    }

    private void actions() {
        if (actions.isEmpty()) {
            stopScheduler();
        } else {
            actions.removeIf(scheduleAction -> scheduleAction.action(bukkitTask));
        }
    }

}