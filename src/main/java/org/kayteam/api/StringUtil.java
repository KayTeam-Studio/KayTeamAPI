package org.kayteam.api;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StringUtil {

    public static String replaceText(String text, String[][] replacements) {
        return replaceText(text, null, replacements);
    }

    public static String replaceText(String text, Player player, String[][] replacements) {
        for (String[] values:replacements){
            text = text.replaceAll(values[0], values[1]);
        }
        text = applyPlaceholderAPI(text, player);
        return text;
    }

    public static String applyPlaceholderAPI(String text, Player player) {
        if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            text = PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public static String addColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}