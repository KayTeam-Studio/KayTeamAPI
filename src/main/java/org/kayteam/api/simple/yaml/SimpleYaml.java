package org.kayteam.api.simple.yaml;

import com.cryptomorin.xseries.XMaterial;
import com.loohp.yamlconfiguration.ConfigurationSection;
import com.loohp.yamlconfiguration.YamlConfiguration;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class SimpleYaml {

    private final static HashMap<String, String> globalReplacements = new HashMap<>();
    private final HashMap<String, String> replacements = new HashMap<>();

    private JavaPlugin javaPlugin = null;

    private final String directory;
    private final String name;
    private File file;
    private YamlConfiguration yamlConfiguration;

    public SimpleYaml(JavaPlugin javaPlugin, String directory, String name) {
        this.javaPlugin  = javaPlugin;
        this.directory = javaPlugin.getDataFolder().getPath() + File.separator + directory;
        this.name = name;
    }

    public SimpleYaml(JavaPlugin javaPlugin, String name) {
        this.javaPlugin  = javaPlugin;
        directory = javaPlugin.getDataFolder().getPath();
        this.name = name;
    }

    public SimpleYaml(String directory, String name) {
        this.directory = directory;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }

    public void registerYamlFile() {
        if (yamlConfiguration == null) {
            reloadYamlFile();
        }
    }

    public void reloadYamlFile() {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        file = new File(directory, name + ".yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    if (javaPlugin != null) {
                        if (javaPlugin.getResource(name + ".yml") != null) {
                            if(file.length() == 0){
                                javaPlugin.saveResource(name + ".yml", true);
                            }
                        }
                    }
                }
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        try {
            yamlConfiguration = new YamlConfiguration(file);
            yamlConfiguration.reload();
            // load global replacements
            if (contains("replacements.global") && yamlConfiguration.isConfigurationSection("replacements.global")) {
                for (String key:getConfigurationSection("replacements.global").getKeys(false)) {
                    SimpleYaml.globalReplacements.put(key, getString("replacements.global." + key));
                }
            }
            // load local replacements
            if (contains("replacements.local") && yamlConfiguration.isConfigurationSection("replacements.local")) {
                for (String key:getConfigurationSection("replacements.local").getKeys(false)) {
                    replacements.put(key, getString("replacements.local." + key));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveYamlFile() {
        yamlConfiguration.save();
    }

    public YamlConfiguration getYamlConfiguration() {
        if (yamlConfiguration == null) reloadYamlFile();
        return yamlConfiguration;
    }

    public boolean deleteFile() {
        File file = new File(directory, name + ".yml");
        if (file.exists()) return file.delete();
        return false;
    }

    public boolean existFile() {
        return new File(directory, name + ".yml").exists();
    }

    public void generateBackup() {
        SimpleYaml backup = new SimpleYaml(directory, name + "-backup");
        backup.registerYamlFile();
        backup.saveWithOtherFileConfiguration(yamlConfiguration);
    }

    public void saveWithOtherFileConfiguration(YamlConfiguration yamlConfiguration) {
        this.yamlConfiguration = yamlConfiguration;
        saveYamlFile();
    }

    public List<SimpleYaml> getYamlFiles(JavaPlugin javaPlugin, String directory) {
        return getYamlFiles(javaPlugin.getDataFolder() + File.separator + directory);
    }

    public static List<SimpleYaml> getYamlFiles(String directory) {
        List<SimpleYaml> simpleYamlList = new ArrayList<>();
        File dir = new File(directory);
        if (dir.exists()) {
            File[] files = dir.listFiles((dir1, name) -> name.endsWith(".yml"));
            if (files != null) for (File file:files) simpleYamlList.add(new SimpleYaml(directory, file.getName().replaceAll(".yml", "")));
        }
        return simpleYamlList;
    }

    public String getAboveComment(String path) {
        return yamlConfiguration.getAboveComment(path);
    }

    public void setAboveComment(String path, String comment) {
        yamlConfiguration.setAboveComment(path, comment);
    }

    public String getInlineComment(String path) {
        return yamlConfiguration.getInlineComment(path);
    }

    public void setInlineComment(String path, String comment) {
        yamlConfiguration.setInlineComment(path, comment);
    }

    public boolean isConfigurationSection(String path) {
        return yamlConfiguration.isConfigurationSection(path);
    }
    public ConfigurationSection getConfigurationSection(String path) {
        if (yamlConfiguration.contains(path) && isConfigurationSection(path)) return yamlConfiguration.getConfigurationSection(path);
        return null;
    }

    public ConfigurationSection getConfigurationSection(String path, ConfigurationSection def) {
        if (yamlConfiguration.contains(path) && isConfigurationSection(path)) return yamlConfiguration.getConfigurationSection(path);
        return def;
    }

    public boolean contains(String path) {
        return yamlConfiguration.contains(path);
    }

    public void set(String path, Object value) {
        yamlConfiguration.set(path, value);
    }

    public Object get(String path) {
        if (yamlConfiguration.contains(path)) return yamlConfiguration.get(path);
        return null;
    }

    public Object get(String path, Object def) {
        if (yamlConfiguration.contains(path)) return yamlConfiguration.get(path);
        return def;
    }

    public boolean isBoolean(String path) {
        return yamlConfiguration.isBoolean(path);
    }

    public boolean getBoolean(String path) {
        if (contains(path) && isBoolean(path)) return yamlConfiguration.getBoolean(path);
        return false;
    }

    public boolean getBoolean(String path, boolean def) {
        if (contains(path) && isBoolean(path)) return yamlConfiguration.getBoolean(path);
        return def;
    }

    public boolean isInt(String path) {
        return yamlConfiguration.isInt(path);
    }

    public int getInt(String path) {
        if (contains(path) && isInt(path)) return yamlConfiguration.getInt(path);
        return -1;
    }

    public int getInt(String path, int def) {
        if (contains(path) && isInt(path)) return yamlConfiguration.getInt(path);
        return def;
    }

    public boolean isLong(String path) {
        return yamlConfiguration.isLong(path);
    }

    public long getLong(String path) {
        if (contains(path) && isLong(path)) return yamlConfiguration.getLong(path);
        return -1;
    }

    public long getLong(String path, long def) {
        if (contains(path) && isLong(path)) return yamlConfiguration.getLong(path);
        return def;
    }

    public boolean isDouble(String path) {
        return yamlConfiguration.isDouble(path);
    }

    public double getDouble(String path) {
        if (contains(path) && isDouble(path)) return yamlConfiguration.getDouble(path);
        return -1.0;
    }

    public double getDouble(String path, double def) {
        if (contains(path) && isDouble(path)) return yamlConfiguration.getDouble(path);
        return def;
    }

    public boolean isString(String path) {
        return yamlConfiguration.isString(path);
    }

    public String getString(String path) {
        if (contains(path) && isString(path)) return yamlConfiguration.getString(path);
        return "";
    }

    public String getString(String path, String[][] replacements) {
        return getString(path, "", replacements);
    }

    public String getString(String path, String def) {
        if (contains(path) && isString(path)) return yamlConfiguration.getString(path);
        return def;
    }

    public String getString(String path, String def,String[][] replacements) {
        String string = getString(path, def);
        for (String[] replacement:replacements) string = StringUtils.replace(string, replacement[0], replacement[1]);
        return string;
    }

    public boolean isStringList(String path) {
        if (contains(path)) return yamlConfiguration.getList(path).get(0).getClass().getSimpleName().equals("String");
        return false;
    }

    public List<String> getStringList(String path) {
        if (contains(path) && isList(path)) return yamlConfiguration.getStringList(path);
        return new ArrayList<>();
    }

    public List<String> getStringList(String path, String[][] replacements) {
        return getStringList(path, new ArrayList<>(),replacements);
    }

    public List<String> getStringList(String path, List<String> def) {
        if (contains(path) && isList(path)) return yamlConfiguration.getStringList(path);
        return def;
    }

    public List<String> getStringList(String path, List<String> def, String[][] replacements) {
        List<String> strings = getStringList(path, def);
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            for (String[] replacement:replacements) string = StringUtils.replace(string, replacement[0], replacement[1]);
            strings.set(i, string);
        }
        return strings;
    }

    public boolean isList(String path) {
        return yamlConfiguration.isList(path);
    }

    public List<Object> getList(String path) {
        if (contains(path) && isList(path)) return yamlConfiguration.getList(path);
        return new ArrayList<>();
    }

    public List<Object> getList(String path, List<Object> def) {
        if (contains(path) && isList(path)) return yamlConfiguration.getList(path);
        return def;
    }

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
            if (world == null) return null;
        }
        if (contains(path + ".x") && isDouble(path + ".x")) {
            x = getDouble(path + ".x");
        } else return null;
        if (contains(path + ".y") && isDouble(path + ".y")) {
            y = getDouble(path + ".y");
        } else return null;
        if (contains(path + ".z") && isDouble(path + ".z")) {
            z = getDouble(path + ".z");
        } else return null;
        if (contains(path + ".yaw") && isDouble(path + ".yaw")) yaw = (long) getDouble(path + ".yaw");
        if (contains(path + ".pitch") && isDouble(path + ".pitch")) pitch = (long) getDouble(path + ".pitch");
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

    public void setLocation(String path, Location location, boolean yawAndPitch) {
        set(path + ".world", Objects.requireNonNull(location.getWorld()).getName());
        set(path + ".x", location.getX());
        set(path + ".y", location.getY());
        set(path + ".z", location.getZ());
        if (yawAndPitch) {
            set(path + ".yaw", location.getYaw());
            set(path + ".pitch", location.getPitch());
        }
    }

    public ItemStack getItemStack(String path) {
        return getItemStack(path, new String[][] {});
    }

    public ItemStack getItemStack(String path, String[][] replacements) {
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
                Set<String> names = Objects.requireNonNull(getConfigurationSection(path + ".enchantments")).getValues(false).keySet();
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
                NBTItem nbtItem = new NBTItem(itemStack);
                for(String key : Objects.requireNonNull(getConfigurationSection(path + ".nbt")).getKeys(false)){
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
            if (isSkull) {
                if (contains(path + ".base64-texture")) {
                    String value = getString(path + ".base64-texture");
                    return getCustomTextureHead(value);
                }
            }
            if (contains(path + ".head-owner")){
                if (isString(path + ".head-owner")){
                    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                    assert skullMeta != null;
                    skullMeta.setOwner(getString(path + ".head-owner"));
                    itemStack.setItemMeta(skullMeta);
                }
            }
            return replace(itemStack, replacements);
        } else {
            ItemStack error = new ItemStack(Material.DIRT);
            ItemMeta errorMeta = error.getItemMeta();
            if (errorMeta != null) {
                errorMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cERROR"));
            }
            error.setItemMeta(errorMeta);
            return error;
        }
    }

    public ItemStack getCustomTextureHead(String value) {
        ItemStack head;
        try {
            head = new ItemStack(Material.PLAYER_HEAD);
        } catch (Throwable var2) {
            head = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        }
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
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
            NBTItem nbtItem = new NBTItem(item);
            for(String key : nbtItem.getKeys()){
                if(nbtItem.getType(key).equals(NBTType.NBTTagString)){
                    set(path + ".nbt." + key, nbtItem.getString(key));
                }else if(nbtItem.getType(key).equals(NBTType.NBTTagInt)){
                    set(path + ".nbt." + key, nbtItem.getInteger(key));
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
    }

    public void sendMessage(CommandSender commandSender, String path) {
        sendMessage(commandSender, path, new String[][]{});
    }

    public void sendMessage(CommandSender commandSender, String path, String[][] replacements) {
        List<String> messages = new ArrayList<>();
        if (isList(path)) {
            messages = getStringList(path, replacements);
        } else if (isString(path)){
            messages.add(getString(path, replacements));
        }
        for (String message : messages) {
            // Local replacements
            for (String text : this.replacements.keySet()) {
                message = StringUtils.replace(message, "%" + text + "%", this.replacements.get(text));
            }
            // Global replacements
            for (String text : SimpleYaml.globalReplacements.keySet()) {
                message = StringUtils.replace(message, "%" + text + "%", SimpleYaml.globalReplacements.get(text));
            }
            // PlaceholderAPI
            if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                message = PlaceholderAPI.setPlaceholders((Player) commandSender, message);
            }
            // Add color
            message = ChatColor.translateAlternateColorCodes('&', message);
            // Send message
            commandSender.sendMessage(message);
        }
    }

    public static void sendMessage(CommandSender commandSender, Object message) {
        sendMessage(commandSender, message, new String[][] {});
    }

    public static void sendMessage(CommandSender commandSender, Object message, String[][] replacements) {
        List<String> messages = new ArrayList<>();
        if (message.getClass().getSimpleName().equals("ArrayList")) {
            messages = (List<String>) message;
        } else if (message.getClass().getSimpleName().equals("String")){
            messages.add((String) message);
        }
        for (String m : messages) {
            // Replacements
            for (String[] replacement : replacements) {
                m = StringUtils.replace(m, replacement[0], replacement[1]);
            }
            // Global replacements
            for (String text : SimpleYaml.globalReplacements.keySet()) {
                m = StringUtils.replace(m, "%" + text + "%", SimpleYaml.globalReplacements.get(text));
            }
            // PlaceholderAPI
            if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                m = PlaceholderAPI.setPlaceholders((Player) commandSender, m);
            }
            // Add color
            m = ChatColor.translateAlternateColorCodes('&', m);
            // Send message
            commandSender.sendMessage(m);
        }
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
            try{
                if(item.getType().toString().equals("PLAYER_HEAD")){
                    SkullMeta skullMeta = (SkullMeta) meta;
                    String skullOwner = skullMeta.getOwner();
                    for(String[] values:replacements){
                        skullOwner = skullOwner.replaceAll(values[0], values[1]);
                    }
                    if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                        skullOwner = PlaceholderAPI.setPlaceholders(player, skullOwner);
                    }
                    skullMeta.setOwner(skullOwner);
                }
            }catch (Exception e){
                if(item.getType().toString().equals("LEGACY_SKULL_ITEM")){
                    SkullMeta skullMeta = (SkullMeta) meta;
                    String skullOwner = skullMeta.getOwner();
                    for(String[] values:replacements){
                        skullOwner = skullOwner.replaceAll(values[0], values[1]);
                    }
                    if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                        skullOwner = PlaceholderAPI.setPlaceholders(player, skullOwner);
                    }
                    skullMeta.setOwner(skullOwner);
                }
            }
        }
        item.setItemMeta(meta);
        return item;
    }

}