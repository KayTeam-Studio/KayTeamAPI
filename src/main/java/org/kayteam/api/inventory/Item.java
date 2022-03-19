package org.kayteam.api.inventory;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private ItemStack itemStack;
    private final boolean update;
    private final String displayName;
    private final List<String> lore;

    public Item(ItemStack itemStack, boolean update, String displayName, List<String> lore) {
        this.itemStack = itemStack;
        this.update = update;
        this.displayName = displayName;
        this.lore = lore;
    }

    public ItemStack getItemStack() {
        return itemStack;
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

    public void updateItem(Player player) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> newLore = new ArrayList<>();
        for (String line:lore) {
            if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                line = PlaceholderAPI.setPlaceholders(player, line);
            }
            line = ChatColor.translateAlternateColorCodes('&', line);
            newLore.add(line);
        }
    }

}