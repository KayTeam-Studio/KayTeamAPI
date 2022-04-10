package org.kayteam.api.simple.inventory;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kayteam.api.simple.inventory.action.Action;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private final int slot;
    private final int priority;
    private final boolean update;
    private final String displayName;
    private final List<String> lore;
    private ItemStack itemStack;

    private final List<Action> leftActions = new ArrayList<>();
    private final List<Action> leftShiftActions = new ArrayList<>();
    private final List<Action> middleActions = new ArrayList<>();
    private final List<Action> rightActions = new ArrayList<>();
    private final List<Action> rightShiftActions = new ArrayList<>();

    public Item(int slot, int priority, boolean update, String displayName, List<String> lore) {
        this.slot = slot;
        this.priority = priority;
        this.update = update;
        this.displayName = displayName;
        this.lore = lore;
    }

    public int getSlot() {
        return slot;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<Action> getLeftActions() {
        return leftActions;
    }

    public List<Action> getLeftShiftActions() {
        return leftShiftActions;
    }

    public List<Action> getMiddleActions() {
        return middleActions;
    }

    public List<Action> getRightActions() {
        return rightActions;
    }

    public List<Action> getRightShiftActions() {
        return rightShiftActions;
    }

    public void updateItem(Player player) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        // display_name
        String newDisplayName = new String(displayName);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) newDisplayName = PlaceholderAPI.setPlaceholders(player, newDisplayName);
        newDisplayName = ChatColor.translateAlternateColorCodes('&', newDisplayName);
        itemMeta.setDisplayName(newDisplayName);
        // lore
        List<String> newLore = new ArrayList<>();
        for (String line:lore) {
            if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                line = PlaceholderAPI.setPlaceholders(player, line);
            }
            line = ChatColor.translateAlternateColorCodes('&', line);
            newLore.add(line);
        }
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);
    }

}