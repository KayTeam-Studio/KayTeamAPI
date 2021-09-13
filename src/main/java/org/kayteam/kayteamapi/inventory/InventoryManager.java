package org.kayteam.kayteamapi.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class InventoryManager implements Listener {

    private final JavaPlugin javaPlugin;
    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public InventoryManager(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    private final HashMap<UUID, InventoryBuilder> inventories = new HashMap<>();

    public void openInventory(Player player, InventoryBuilder inventoryBuilder) {
        try {
            player.closeInventory();
        } catch (IllegalStateException ignored) {
        } finally {
            UUID uuid = player.getUniqueId();
            inventories.put(uuid, inventoryBuilder);
            Inventory inventory = Bukkit.createInventory(null, inventoryBuilder.getRows() * 9, ChatColor.translateAlternateColorCodes('&', inventoryBuilder.getTitle()));
            for (int slot = 0; slot < (inventoryBuilder.getRows() * 9); slot++) {
                if (inventory.getSize() > slot) {
                    inventory.setItem(slot, inventoryBuilder.getItem(slot).getItem());
                }
            }
            inventoryBuilder.setInventory(inventory);
            Bukkit.getScheduler().runTaskLater(javaPlugin, () -> player.openInventory(inventory), 1);
            InventoryUpdater inventoryUpdater = new InventoryUpdater(this, inventoryBuilder);
            inventoryUpdater.startScheduler();
        }
    }

    public boolean containInventoryBuilder(InventoryBuilder inventoryBuilder) {
        return inventories.containsValue(inventoryBuilder);
    }
    public boolean containInventoryBuilder(Player player) {
        return inventories.containsKey(player.getUniqueId());
    }

    public InventoryBuilder getInventoryBuilder(Player player) {
        return inventories.get(player.getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (inventories.containsKey(uuid)) {
            InventoryBuilder inventoryBuilder = inventories.get(uuid);
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', inventoryBuilder.getTitle()))) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                switch (event.getClick()) {
                    case LEFT:
                        inventoryBuilder.getLeftAction(slot).action(player, slot);
                        break;
                    case RIGHT:
                        inventoryBuilder.getRightAction(slot).action(player, slot);
                        break;
                    case MIDDLE:
                        inventoryBuilder.getMiddleAction(slot).action(player, slot);
                        break;
                    case SHIFT_LEFT:
                        inventoryBuilder.getLeftShiftAction(slot).action(player, slot);
                        break;
                    case SHIFT_RIGHT:
                        inventoryBuilder.getRightShiftAction(slot).action(player, slot);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (inventories.containsKey(uuid)) {
            InventoryBuilder inventoryBuilder = inventories.get(uuid);
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', inventoryBuilder.getTitle()))) {
                inventoryBuilder.getCloseAction().action(player);
                inventories.remove(uuid);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        inventories.remove(event.getPlayer().getUniqueId());
    }

}