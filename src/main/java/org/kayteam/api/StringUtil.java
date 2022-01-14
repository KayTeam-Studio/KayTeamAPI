package org.kayteam.api;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StringUtil {

    public static String replace(String text, String[][] replacements) {
        return replace(text, null, replacements);
    }

    public static String replace(String text, Player player, String[][] replacements) {
        String result = text;
        for (String[] replacement:replacements) {
            String from = replacement[0];
            String to = replacement[1];
            result = result.replaceAll(from, to);
        }
        if (player != null) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                result = PlaceholderAPI.setPlaceholders(player, result);
            }
        }
        return result;
    }

}