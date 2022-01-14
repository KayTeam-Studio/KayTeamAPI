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

package org.kayteam.api;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BrandSender {

    public static void sendBrandMessage(JavaPlugin javaPlugin, String state){
        List<String> message = new ArrayList<>();
        message.add("");
        message.add("&f>>");
        message.add("&f>> &6" + javaPlugin.getDescription().getName()+" "+state);
        message.add("&f>> &6Version &f" + javaPlugin.getDescription().getVersion());
        message.add("&f>>");
        message.add("&f>> &6Developed by KayTeam © 2020 - " + Calendar.getInstance().get(Calendar.YEAR) + ". All rights reserved.");
        message.add("&f>> &f" + javaPlugin.getDescription().getWebsite());
        message.add("&f>>");
        message.add("");
        for(String line : message){
            javaPlugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

}