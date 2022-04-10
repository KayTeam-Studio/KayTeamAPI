package org.kayteam.api.simple.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.api.scheduler.SimpleScheduler;
import org.kayteam.api.simple.inventory.action.Action;

import java.util.HashMap;
import java.util.UUID;

public class SimpleInventoryManager implements Listener {

    private final JavaPlugin javaPlugin;
    private final HashMap<String, SimpleInventoryBuilder> simpleInventories = new HashMap<>();
    private final HashMap<UUID, SimpleInventoryView> openInventories = new HashMap<>();

    public SimpleInventoryManager(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public void openInventory(Player player, String name) {
        try {
            player.closeInventory();
        } catch (IllegalStateException ignored) {
        } finally {
            UUID uuid = player.getUniqueId();
            if (simpleInventories.containsKey(name)) {
                SimpleInventoryBuilder simpleInventoryBuilder = new SimpleInventoryBuilder(simpleInventories.get(name));
                SimpleInventoryView simpleInventoryView = simpleInventoryBuilder.generateInventoryView(javaPlugin, player);
                openInventories.put(uuid, simpleInventoryView);
                // Open Inventory
                SimpleScheduler openInventoryScheduler = new SimpleScheduler(javaPlugin, 1);
                openInventoryScheduler.addScheduleAction(bukkitTask -> {
                    player.openInventory(simpleInventoryView.getInventory());
                    return true;
                });
                openInventoryScheduler.runSchedulerLater();
                // Inventory Updater
                if (simpleInventoryBuilder.getUpdateInterval() > 0) {
                    SimpleScheduler inventoryUpdateScheduler = new SimpleScheduler(javaPlugin, 1);
                    inventoryUpdateScheduler.addScheduleAction(new SimpleInventoryUpdater(this, player, simpleInventoryView));
                    inventoryUpdateScheduler.runSchedulerTimer();
                }
            }
        }
    }

    public void openInventory(Player player, SimpleInventoryBuilder simpleInventoryBuilder) {
        try {
            player.closeInventory();
        } catch (IllegalStateException ignored) {
        } finally {
            if (!simpleInventories.containsKey(simpleInventoryBuilder.getName())) simpleInventories.put(simpleInventoryBuilder.getName(), simpleInventoryBuilder);
            openInventory(player, simpleInventoryBuilder.getName());
        }
    }

    public boolean containInventoryBuilder(SimpleInventoryBuilder simpleInventoryBuilder) {
        return openInventories.containsValue(simpleInventoryBuilder);
    }

    public boolean containInventoryView(Player player) {
        return openInventories.containsKey(player.getUniqueId());
    }

    public SimpleInventoryBuilder getInventoryBuilder(String name) {
        return simpleInventories.get(name);
    }

    public SimpleInventoryView getInventoryView(Player player) {
        return openInventories.get(player.getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (openInventories.containsKey(uuid)) {
            SimpleInventoryView simpleInventoryView = openInventories.get(uuid);
            if (event.getView().getTitle().equals(simpleInventoryView.getInventory().getTitle())) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                if (simpleInventoryView.getItems().containsKey(slot)) {
                    switch (event.getClick()) {
                        case LEFT: {
                            for (Action action:simpleInventoryView.getItems().get(slot).getLeftActions()) action.execute(player, simpleInventoryView);
                            break;
                        }
                        case SHIFT_LEFT: {
                            for (Action action:simpleInventoryView.getItems().get(slot).getLeftShiftActions()) action.execute(player, simpleInventoryView);
                            break;
                        }
                        case MIDDLE: {
                            for (Action action:simpleInventoryView.getItems().get(slot).getMiddleActions()) action.execute(player, simpleInventoryView);
                            break;
                        }
                        case RIGHT: {
                            for (Action action:simpleInventoryView.getItems().get(slot).getRightActions()) action.execute(player, simpleInventoryView);
                            break;
                        }
                        case SHIFT_RIGHT: {
                            for (Action action:simpleInventoryView.getItems().get(slot).getRightShiftActions()) action.execute(player, simpleInventoryView);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (openInventories.containsKey(uuid)) {
            SimpleInventoryView simpleInventoryView = openInventories.get(uuid);
            if (event.getView().getTitle().equals(simpleInventoryView.getInventory().getTitle())) {
                openInventories.remove(uuid);
                for (Action action:simpleInventoryView.getCloseActions()) action.execute(player, simpleInventoryView);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        openInventories.remove(event.getPlayer().getUniqueId());
    }

}