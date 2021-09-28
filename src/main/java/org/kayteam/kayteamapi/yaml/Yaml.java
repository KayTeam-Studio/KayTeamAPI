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

package org.kayteam.kayteamapi.yaml;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;


public class Yaml {

    private JavaPlugin javaPlugin;
    private final String dir;
    private final String name;
    private File file;
    private FileConfiguration fileConfiguration;

    public Yaml(JavaPlugin javaPlugin, String name) {
        this.javaPlugin = javaPlugin;
        this.dir = javaPlugin.getDataFolder().getPath();
        this.name = name;
    }

    public Yaml(JavaPlugin javaPlugin, String dir, String name) {
        this.javaPlugin = javaPlugin;
        this.dir = javaPlugin.getDataFolder().getPath() + File.separator + dir;
        this.name = name;
    }

    public Yaml(String dir, String name) {
        this.dir = dir;
        this.name = name;
    }

    public static List<File> getFolderFiles(String dir) {
        File folder = new File(dir);
        List<File> files = new ArrayList<>();
        if (folder.exists()) {
            File[] file = folder.listFiles(pathname -> pathname.getName().endsWith(".yml"));
            if (file != null) {
                Collections.addAll(files, file);
            }
        }
        return files;
    }
    public FileConfiguration getFileConfiguration() {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        return fileConfiguration;
    }
    public void reloadFileConfiguration() {
        if (fileConfiguration == null) {
            file = new File(dir, name + ".yml");
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                if (dirFile.mkdir()) {
                    javaPlugin.getLogger().info("The directory '" + dir +"' has been created.");
                }
            }
            try{
                if(!file.exists()){
                    if (file.createNewFile()) {
                        javaPlugin.getLogger().info("The file '" + name +".yml' has been created.");
                    }
                }
            } catch (IOException ignored){}
        }
        try{
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }catch (Exception ignored){}
        if(file.length() == 0){
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                fileConfiguration.setDefaults(defConfig);
                saveFileConfiguration();
                saveWithOtherFileConfiguration(defConfig);
            }
        }
    }
    public void saveFileConfiguration() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            javaPlugin.getLogger().log(Level.SEVERE, "No se pudo guardar el archivo.");
        }
    }
    public void registerFileConfiguration() {
        file = new File(dir, name + ".yml");
        if (!file.exists()) {
            getFileConfiguration().options().copyDefaults(true);
            saveFileConfiguration();
        } else {
            reloadFileConfiguration();
        }
    }
    public boolean deleteFileConfiguration() {
        file = new File(dir, name + ".yml");
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
    public boolean existFileConfiguration() {
        file = new File(dir, name + ".yml");
        return file.exists();
    }
    public void generateBackup() {

    }
    public void saveWithOtherFileConfiguration(FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            javaPlugin.getLogger().log(Level.SEVERE, "No se pudo guardar el archivo.");
        }
    }

    public void sendMessage(CommandSender commandSender, String path) {
        sendMessage(commandSender, path, new String[][]{}, "");
    }
    public void sendMessage(CommandSender commandSender, String path, String[][] replacements) {
        sendMessage(commandSender, path, replacements, "");
    }
    public void sendMessage(CommandSender commandSender, String path, String def) {
        sendMessage(commandSender, path, new String[][]{}, def);
    }
    public void sendMessage(CommandSender commandSender, String path, String[][] replacements, String def) {
        if (fileConfiguration == null) return;
        if (!fileConfiguration.contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isString(path)) {
                        fileConfiguration.set(path, defConfig.getString(path));
                    } else {
                        fileConfiguration.set(path, defConfig.getStringList(path));
                    }
                }
            } else {
                fileConfiguration.set(path, def);
            }
            saveFileConfiguration();
        }
        if (fileConfiguration.isList(path)) {
            List<String> messages = fileConfiguration.getStringList(path);
            for (String message : messages) {
                if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders((Player) commandSender, message);
                }
                if (fileConfiguration.contains("prefix") && fileConfiguration.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(fileConfiguration.getString("prefix")));
                }
                for (String[] values : replacements) {
                    message = message.replaceAll(values[0], values[1]);
                }
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            String message = fileConfiguration.getString(path);
            if (message != null) {
                if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders((Player) commandSender, message);
                }
                if (fileConfiguration.contains("prefix") && fileConfiguration.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(fileConfiguration.getString("prefix")));
                }
                for (String[] values : replacements) {
                    message = message.replaceAll(values[0], values[1]);
                }
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }


    public static void sendSimpleMessage(CommandSender commandSender, Object message) {
        sendSimpleMessage(commandSender, message, new String[][] {});
    }
    public static void sendSimpleMessage(CommandSender commandSender, Object message, String[][] replacements) {
        if (message.getClass().getSimpleName().equals("ArrayList")) {
            List<String> messages = (List<String>) message;
            for (String m:messages) {
                if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    m = PlaceholderAPI.setPlaceholders((Player) commandSender, m);
                }
                for (String[] values:replacements){
                    m = m.replaceAll(values[0], values[1]);
                }
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
            }
        } else if (message.getClass().getSimpleName().equals("String")){
            String m = (String) message;
            if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                m = PlaceholderAPI.setPlaceholders((Player) commandSender, m);
            }
            for (String[] values:replacements){
                m = m.replaceAll(values[0], values[1]);
            }
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
        }
    }


    // Default
    public void set(String path, Object value) {
        fileConfiguration.set(path, value);
    }
    public boolean contains(String path) { return fileConfiguration.contains(path); }
    // Boolean
    public boolean isBoolean(String path) { return fileConfiguration.isBoolean(path); }
    public boolean getBoolean(String path) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isBoolean(path)) {
                        fileConfiguration.set(path, defConfig.getBoolean(path));
                        saveFileConfiguration();
                    }
                }
            }
        }
        return fileConfiguration.getBoolean(path);
    }
    public boolean getBoolean(String path, boolean def) {
        if (!contains(path)) {
            set(path, def);
            saveFileConfiguration();
        }
        return fileConfiguration.getBoolean(path, def);
    }
    // Boolean
    public boolean isInt(String path) { return fileConfiguration.isInt(path); }
    public int getInt(String path) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isInt(path)) {
                        fileConfiguration.set(path, defConfig.getInt(path));
                        saveFileConfiguration();
                    }
                }
            }
        }
        return fileConfiguration.getInt(path);
    }
    public int getInt(String path, int def) {
        if (!contains(path)) {
            set(path, def);
            saveFileConfiguration();
        }
        return fileConfiguration.getInt(path, def);
    }
    // Long
    public boolean isLong(String path) { return fileConfiguration.isLong(path); }
    public long getLong(String path) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isInt(path)) {
                        fileConfiguration.set(path, defConfig.getInt(path));
                        saveFileConfiguration();
                    }
                }
            }
        }
        return fileConfiguration.getLong(path);
    }
    public long getLong(String path, long def) {
        if (!contains(path)) {
            set(path, def);
            saveFileConfiguration();
        }
        return fileConfiguration.getLong(path, def); }
    // String
    public boolean isString(String path) { return fileConfiguration.isString(path); }
    public String getString(String path) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isString(path)) {
                        fileConfiguration.set(path, defConfig.getString(path));
                        saveFileConfiguration();
                    }
                }
            }
        }
        return fileConfiguration.getString(path);
    }
    public String getString(String path, String[][] replacements) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isString(path)) {
                        fileConfiguration.set(path, defConfig.getString(path));
                        saveFileConfiguration();
                    }
                }
            }
        }
        return getString(path, "", replacements);
    }
    public String getString(String path, String def) { return fileConfiguration.getString(path, def); }
    public String getString(String path, String def, String[][] replacements) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isString(path)) {
                        fileConfiguration.set(path, defConfig.getString(path));
                        saveFileConfiguration();
                        String text = defConfig.getString(path);
                        for (String[] replacement:replacements) {
                            if (text.contains(replacement[0])) {
                                text = text.replaceAll(replacement[0], replacement[1]);
                            }
                        }
                        return text;
                    }
                }
            }
        }
        String text = fileConfiguration.getString(path, def);
        for (String[] replacement:replacements) {
            if (text.contains(replacement[0])) {
                text = text.replaceAll(replacement[0], replacement[1]);
            }
        }
        return text;
    }
    // Double
    public boolean isDouble(String path) { return fileConfiguration.isDouble(path); }
    public double getDouble(String path) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isDouble(path)) {
                        fileConfiguration.set(path, defConfig.getDouble(path));
                        saveFileConfiguration();
                    }
                }
            }
        }
        return fileConfiguration.getDouble(path);
    }
    public double getDouble(String path, double def) {
        if (!contains(path)) {
            set(path, def);
            saveFileConfiguration();
        }
        return fileConfiguration.getDouble(path, def); }

    // List
    public boolean isList(String path) {
        return fileConfiguration.isList(path);
    }
    public List<?> getList(String path) {
        return fileConfiguration.getList(path);
    }
    public List<?> getList(String path, List<?> def) {
        return fileConfiguration.getList(path, def);
    }

    // StringList
    public List<String> getStringList(String path) {
        if (!contains(path)) {
            if (javaPlugin.getResource(name + ".yml") != null){
                Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                if (defConfig.contains(path)) {
                    if (defConfig.isList(path)) {
                        fileConfiguration.set(path, defConfig.getStringList(path));
                        saveFileConfiguration();
                    }
                }
            }
        }
        return fileConfiguration.getStringList(path);
    }

    // Location
    public boolean isLocation(String path) {
        boolean result = false;
        if (contains(path + ".world") && isString(path + ".world")) {
            if (contains(path + ".x") &&  isDouble(path + ".x")) {
                if (contains(path + ".y") &&  isDouble(path + ".y")) {
                    if (contains(path + ".z") &&  isDouble(path + ".z")) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    public Location getLocation(String path) {
        World world = null;
        double x, y, z;
        float yaw = 0, pitch = 0;
        if (contains(path + ".world") && isString(path + ".world")) {
            world = javaPlugin.getServer().getWorld(getString(path + ".world"));
            if (world == null) {
                return null;
            }
        }
        if (contains(path + ".x") && isDouble(path + ".x")) {
            x = getDouble(path + ".x");
        } else {
            return null;
        }
        if (contains(path + ".y") && isDouble(path + ".y")) {
            y = getDouble(path + ".y");
        } else {
            return null;
        }
        if (contains(path + ".z") && isDouble(path + ".z")) {
            z = getDouble(path + ".z");
        } else {
            return null;
        }
        if (contains(path + ".yaw") && isDouble(path + ".yaw")) {
            yaw = (long) getDouble(path + ".yaw");
        }
        if (contains(path + ".pitch") && isDouble(path + ".pitch")) {
            pitch = (long) getDouble(path + ".pitch");
        }
        return new Location(world, x, y, z, yaw, pitch);
    }
    public void setLocation(String path, Location location) {
        set(path + ".world", Objects.requireNonNull(location.getWorld()).getName());
        set(path + ".x", location.getX());
        set(path + ".y", location.getY());
        set(path + ".z", location.getZ());
        set(path + ".yaw", location.getYaw());
        set(path + ".pitch", location.getPitch());
    }


    public ItemStack getItemStack(String path) {
        XMaterial xMaterial = XMaterial.matchXMaterial(getString(path + ".material")).orElse(null);
        assert xMaterial != null;
        Material material = xMaterial.parseMaterial();
        int amount = getInt(path + ".amount", 1);
        // MaterialData
        short data = -1;
        if (contains(path + ".data")) {
            if (isInt(path + ".data")) {
                data = (short) getInt(path + ".data");
            }
        }else{
            data = xMaterial.getData();
        }
        if (material != null) {
            ItemStack itemStack;
            if (data != -1) {
                itemStack = new ItemStack(material, amount, data);
            } else {
                itemStack = new ItemStack(material, amount);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                // DisplayName
                if (contains(path + ".name")) {
                    if (isString(path + ".name")) {
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getString(path + ".name")));
                    }
                }
                // Lore
                if (contains(path + ".lore")) {
                    if (isList(path + ".lore")) {
                        List<String> lore = getStringList(path + ".lore");
                        for (int i = 0; i < lore.size(); i++) {
                            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
                        }
                        itemMeta.setLore(lore);
                    }
                }
                // ItemFlag
                if (contains(path + ".flags")) {
                    if (isList(path + ".flags")) {
                        List<String> flags = getStringList(path + ".flags");
                        for (String flag:flags) {
                            ItemFlag itemFlag = ItemFlag.valueOf(flag);
                            itemMeta.addItemFlags(itemFlag);
                        }
                    }
                }
            }
            itemStack.setItemMeta(itemMeta);
            // Enchantments
            if (contains(path + ".enchantments")) {
                Set<String> names = Objects.requireNonNull(fileConfiguration.getConfigurationSection(path + ".enchantments")).getValues(false).keySet();
                for (String name:names) {
                    Enchantment enchantment = Enchantment.getByName(name);
                    if (enchantment != null) {
                        itemStack.addUnsafeEnchantment(enchantment, getInt(path + ".enchantments." + name));
                    }
                }
            }
            // Durability
            if (contains(path + ".durability")) {
                if (isInt(path + ".durability")) {
                    itemStack.setDurability((short) getInt(path + ".durability"));
                }
            }
            // ITEM NBTAPI
            if (contains(path + ".nbt")){
                if (Bukkit.getPluginManager().isPluginEnabled("NBTAPI")){
                    NBTItem nbtItem = new NBTItem(itemStack);
                    for(String key : Objects.requireNonNull(getFileConfiguration().getConfigurationSection(path + ".nbt")).getKeys(false)){
                        try{
                            if(isString(path + ".nbt." + key)){
                                nbtItem.setString(key, getString(path + ".nbt." + key));
                            }else if(isInt(path + ".nbt." + key)){
                                nbtItem.setInteger(key, getInt(path + ".nbt." + key));
                            }else{
                                javaPlugin.getLogger().log(Level.SEVERE, "An error has occurred trying load NBT: "+key+". Please enter a valid type: STRING/INTEGER.");
                            }
                        }catch (Exception e){
                            javaPlugin.getLogger().log(Level.SEVERE, "An error has occurred trying load NBT: "+key);
                        }
                    }
                    itemStack = nbtItem.getItem();
                }else{
                    Bukkit.getLogger().log(Level.INFO, "To enable NBT item options, please download the plugin NBTAPI from Spigot.");
                }
            }
            // SKULL ITEM
            boolean isSkull = false;
            try {
                Material m = Material.valueOf("PLAYER_HEAD");
                isSkull = true;
            } catch (IllegalArgumentException e) {
                try {
                    Material m = Material.valueOf("LEGACY_SKULL_ITEM");
                    isSkull = true;
                } catch (IllegalArgumentException ignored) {

                }
            }
            if (contains(path + ".head-owner")){
                if (isString(path + ".head-owner")){
                    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                    assert skullMeta != null;
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(getString(path + ".head-owner")));
                    itemStack.setItemMeta(skullMeta);
                }
            }
            return itemStack;
        } else {
            ItemStack error = new ItemStack(Material.DIRT);
            ItemMeta errorMeta = error.getItemMeta();
            if (errorMeta != null) {
                errorMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cERROR"));
            }
            error.setItemMeta(errorMeta);
            return new ItemStack(Material.DIRT);
        }
    }
    public void setItemStack(String path, ItemStack item) {
        set(path + ".material", item.getType().toString());
        set(path + ".amount", item.getAmount());
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            // DisplayName
            if (itemMeta.hasDisplayName()) {
                set(path + ".name", itemMeta.getDisplayName());
            }
            // Lore
            if (itemMeta.hasLore()) {
                set(path + ".lore", itemMeta.getLore());
            }
            // ItemFlag
            if (!itemMeta.getItemFlags().isEmpty()) {
                List<String> flags = new ArrayList<>();
                for (ItemFlag flag:itemMeta.getItemFlags()) {
                    flags.add(flag.toString());
                }
                set(path + ".flags", flags);
            }
            // Enchantments
            if (!item.getEnchantments().isEmpty()) {
                for (Enchantment enchantment:item.getEnchantments().keySet()) {
                    set(path + ".enchantments." + enchantment.getName(), item.getEnchantments().get(enchantment));
                }
            }
            if(!Objects.equals(Objects.requireNonNull(item.getData()).toString(), "0")){
                set(path + ".data", item.getData().getData());
            }
            if(item.getType().getMaxDurability() != item.getDurability()){
                set(path + ".durability", item.getDurability());
            }
            // ITEM NBTAPI
            if (Bukkit.getPluginManager().isPluginEnabled("NBTAPI")){
                NBTItem nbtItem = new NBTItem(item);
                for(String key : nbtItem.getKeys()){
                    if(nbtItem.getType(key).equals(NBTType.NBTTagString)){
                        set(path + ".nbt." + key, nbtItem.getString(key));
                    }else if(nbtItem.getType(key).equals(NBTType.NBTTagInt)){
                        set(path + ".nbt." + key, nbtItem.getInteger(key));
                    }
                }
            }
            // SKULL TYPE
            try{
                if(item.getType().toString().equals("PLAYER_HEAD")){
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                    if (skullMeta.hasOwner()) {
                        set(path + ".head-owner", Objects.requireNonNull(skullMeta.getOwningPlayer()).getName());
                    }
                }
            }catch (NoSuchFieldError e){
                if(item.getType().toString().equals("LEGACY_SKULL_ITEM")) {
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                    if (skullMeta.hasOwner()) {
                        set(path + ".head-owner", Objects.requireNonNull(skullMeta.getOwningPlayer()).getName());
                    }
                }
            }
        }
        saveFileConfiguration();
    }


    public static ItemStack replace(ItemStack itemStack) {
        return replace(itemStack, null, new String[][] {});
    }
    public static ItemStack replace(ItemStack itemStack, Player player) {
        return replace(itemStack, player, new String[][] {});
    }
    public static ItemStack replace(ItemStack itemStack, String[][] replacements) {
        return replace(itemStack, null, replacements);
    }
    public static ItemStack replace(ItemStack itemStack, Player player, String[][] replacements) {
        ItemStack item = new ItemStack(itemStack);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasDisplayName()) {
                String displayName = meta.getDisplayName();
                for (String[] values:replacements){
                    displayName = displayName.replaceAll(values[0], values[1]);
                }
                if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    displayName = PlaceholderAPI.setPlaceholders(player, displayName);
                }
                displayName = ChatColor.translateAlternateColorCodes('&', displayName);
                meta.setDisplayName(displayName);
            }
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                List<String> newLore = new ArrayList<>();
                if (lore != null) {
                    for (String line:lore) {
                        for (String[] values:replacements){
                            line = line.replaceAll(values[0], values[1]);
                        }
                        if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            line = PlaceholderAPI.setPlaceholders(player, line);
                        }
                        line = ChatColor.translateAlternateColorCodes('&', line);
                        newLore.add(line);
                    }
                    meta.setLore(newLore);
                }
            }
        }
        item.setItemMeta(meta);
        return item;
    }

}