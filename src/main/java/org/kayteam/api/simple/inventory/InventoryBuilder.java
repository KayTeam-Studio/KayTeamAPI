/*
 * Copyright (C) 2021  SirOswaldo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.kayteam.api.simple.inventory;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.kayteam.api.StringUtil;
import org.kayteam.api.simple.inventory.action.Action;
import org.kayteam.api.simple.inventory.requirement.Requirement;
import org.kayteam.api.simple.yaml.SimpleYaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class InventoryBuilder {

    private final String title;
    private final int rows;
    private final int updateInterval;
    private final List<Requirement> openRequirements;

    private final HashMap<Integer, Slot> slots = new HashMap<>();

    public InventoryBuilder(SimpleYaml yaml, String path) {
        title = yaml.getString(path + ".title", "Default title");
        rows = yaml.getInt(path + ".rows", 3);
        updateInterval = yaml.getInt(path + ".update-interval", 0);

        openRequirements = loadRequirements(yaml, path + ".open-requirements");

        if (yaml.contains(path + ".items") && yaml.isConfigurationSection(path + ".items")) {
            for (String itemName: Objects.requireNonNull(yaml.getConfigurationSection(path + ".items")).getKeys(false)) {
                ItemStack itemStack = yaml.getItemStack(path + ".items." + itemName);
                if (yaml.contains(path + ".items." + itemName + ".slots")) {
                    for (Integer slotNumber:getSlots(yaml.getStringList(path + ".items." + itemName + ".slots"))) {
                        if (!slots.containsKey(slotNumber)) slots.put(slotNumber, new Slot(slotNumber));
                        Slot slot = slots.get(slotNumber);
                        boolean update = yaml.getBoolean(path + ".items." + itemName + ".update", false);
                        int priority = yaml.getInt(path + ".items." + itemName + ".priority", 0);
                        String displayName = yaml.getString(path + ".items." + itemName + ".name", "");
                        List<String> lore = yaml.getStringList(path + ".items." + itemName + ".name", new ArrayList<>());
                        Item item = new Item(itemStack, update, displayName, lore, priority);

                        item.setViewRequirements(loadRequirements(yaml, path + ".items." + itemName + ".view-requirements"));

                        item.setClickRequirements(loadRequirements(yaml, path + ".items." + itemName + ".click-requirements"));
                        item.setClickActions(loadActions(yaml, path + ".items." + itemName + ".click-actions"));

                        item.setLeftClickRequirements(loadRequirements(yaml, path + ".items." + itemName + ".left-click-requirements"));
                        item.setLeftClickActions(loadActions(yaml, path + ".items." + itemName + ".left-click-actions"));

                        item.setLeftShiftClickRequirements(loadRequirements(yaml, path + ".items." + itemName + ".left-shift-click-requirements"));
                        item.setLeftShiftClickActions(loadActions(yaml, path + ".items." + itemName + ".left-shift-click-actions"));

                        item.setMiddleClickRequirements(loadRequirements(yaml, path + ".items." + itemName + ".middle-click-requirements"));
                        item.setMiddleClickActions(loadActions(yaml, path + ".items." + itemName + ".middle-click-actions"));

                        item.setRightClickRequirements(loadRequirements(yaml, path + ".items." + itemName + ".right-click-requirements"));
                        item.setRightClickActions(loadActions(yaml, path + ".items." + itemName + ".right-click-actions"));

                        item.setRightShiftClickRequirements(loadRequirements(yaml, path + ".items." + itemName + ".right-shift-click-requirements"));
                        item.setRightShiftClickActions(loadActions(yaml, path + ".items." + itemName + ".right-shift-click-actions"));

                        slot.addItem(item);
                    }
                }
            }
        }
    }

    public List<Requirement> loadRequirements(SimpleYaml yaml, String path) {
        List<Requirement> requirements = new ArrayList<>();
        if (yaml.contains(path)) {
            if (yaml.isConfigurationSection(path)) {
                for (String requirementName:yaml.getConfigurationSection(path).getValues(false).keySet()) {
                    if (yaml.contains(path + "." + requirementName + ".type")) {
                        if (yaml.isString(path + "." + requirementName + ".type")) {
                            String requirementType = yaml.getString(path + "." + requirementName + ".type");
                            switch (requirementType) {
                                case "has permission": {
                                    if (yaml.contains(path + "." + requirementName + ".permission")) {
                                        if (yaml.isString(path + "." + requirementName + ".permission")) {
                                            String permission = yaml.getString(path + "." + requirementName + ".permission");
                                            Requirement requirement = new Requirement(requirementName, requirementType, new String[]{permission}) {
                                                @Override
                                                public boolean verify(Player player) {
                                                    String permission = getValues()[0];
                                                    permission = StringUtil.applyPlaceholderAPI(permission, player);
                                                    return player.hasPermission(permission);
                                                }
                                            };
                                            requirements.add(requirement);
                                        }
                                    }
                                    break;
                                }
                                case "has money": {
                                    if (yaml.contains(path + "." + requirementName + ".amount")) {
                                        if (yaml.isString(path + "." + requirementName + ".amount")) {
                                            String amount = yaml.getString(path + "." + requirementName + ".amount");
                                            Requirement requirement = new Requirement(requirementName, requirementType, new String[]{amount}) {
                                                @Override
                                                public boolean verify(Player player) {
                                                    if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
                                                        RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
                                                        if (registeredServiceProvider != null) {
                                                            Economy economy = registeredServiceProvider.getProvider();
                                                            if (economy != null) {
                                                                String amountString = getValues()[0];
                                                                amountString = StringUtil.applyPlaceholderAPI(amountString, player);
                                                                try {
                                                                    double balance = economy.getBalance(player);
                                                                    double amount = Double.parseDouble(amountString);
                                                                    return (balance > amount);
                                                                } catch (NumberFormatException ignored) { }
                                                            }
                                                        }
                                                    }
                                                    return false;
                                                }
                                            };
                                            requirements.add(requirement);
                                        }
                                    }
                                    break;
                                }
                                case "equals": {
                                    String input = "";
                                    String compare = "";
                                    if (yaml.contains(path + "." + requirementName + ".input")) {
                                        if (yaml.isString(path + "." + requirementName + ".input")) {
                                            input = yaml.getString(path + "." + requirementName + ".input");
                                        }
                                    }
                                    if (yaml.contains(path + "." + requirementName + ".compare")) {
                                        if (yaml.isString(path + "." + requirementName + ".compare")) {
                                            input = yaml.getString(path + "." + requirementName + ".compare");
                                        }
                                    }
                                    Requirement requirement = new Requirement(requirementName, requirementType, new String[]{input, compare}) {
                                        @Override
                                        public boolean verify(Player player) {
                                            String input = StringUtil.applyPlaceholderAPI(getValues()[0], player);
                                            String compare = StringUtil.applyPlaceholderAPI(getValues()[1], player);
                                            return input.equals(compare);
                                        }
                                    };
                                    requirements.add(requirement);
                                    break;
                                }
                                case "equals ignore case": {
                                    String input = "";
                                    String compare = "";
                                    if (yaml.contains(path + "." + requirementName + ".input")) {
                                        if (yaml.isString(path + "." + requirementName + ".input")) {
                                            input = yaml.getString(path + "." + requirementName + ".input");
                                        }
                                    }
                                    if (yaml.contains(path + "." + requirementName + ".compare")) {
                                        if (yaml.isString(path + "." + requirementName + ".compare")) {
                                            input = yaml.getString(path + "." + requirementName + ".compare");
                                        }
                                    }
                                    Requirement requirement = new Requirement(requirementName, requirementType, new String[]{input, compare}) {
                                        @Override
                                        public boolean verify(Player player) {
                                            String input = StringUtil.applyPlaceholderAPI(getValues()[0], player);
                                            String compare = StringUtil.applyPlaceholderAPI(getValues()[1], player);
                                            return input.equalsIgnoreCase(compare);
                                        }
                                    };
                                    requirements.add(requirement);
                                    break;
                                }
                                case ">": {
                                    String input = "";
                                    String compare = "";
                                    if (yaml.contains(path + "." + requirementName + ".input")) {
                                        if (yaml.isString(path + "." + requirementName + ".input")) {
                                            input = yaml.getString(path + "." + requirementName + ".input");
                                        }
                                    }
                                    if (yaml.contains(path + "." + requirementName + ".compare")) {
                                        if (yaml.isString(path + "." + requirementName + ".compare")) {
                                            input = yaml.getString(path + "." + requirementName + ".compare");
                                        }
                                    }
                                    Requirement requirement = new Requirement(requirementName, requirementType, new String[]{input, compare}) {
                                        @Override
                                        public boolean verify(Player player) {
                                            String input = StringUtil.applyPlaceholderAPI(getValues()[0], player);
                                            String compare = StringUtil.applyPlaceholderAPI(getValues()[1], player);
                                            try {
                                                int inputNumber = Integer.parseInt(input);
                                                int compareNumber = Integer.parseInt(compare);
                                                return inputNumber > compareNumber;
                                            } catch (NumberFormatException e) {
                                                return false;
                                            }
                                        }
                                    };
                                    requirements.add(requirement);
                                    break;
                                }
                                case ">=": {
                                    String input = "";
                                    String compare = "";
                                    if (yaml.contains(path + "." + requirementName + ".input")) {
                                        if (yaml.isString(path + "." + requirementName + ".input")) {
                                            input = yaml.getString(path + "." + requirementName + ".input");
                                        }
                                    }
                                    if (yaml.contains(path + "." + requirementName + ".compare")) {
                                        if (yaml.isString(path + "." + requirementName + ".compare")) {
                                            input = yaml.getString(path + "." + requirementName + ".compare");
                                        }
                                    }
                                    Requirement requirement = new Requirement(requirementName, requirementType, new String[]{input, compare}) {
                                        @Override
                                        public boolean verify(Player player) {
                                            String input = StringUtil.applyPlaceholderAPI(getValues()[0], player);
                                            String compare = StringUtil.applyPlaceholderAPI(getValues()[1], player);
                                            try {
                                                int inputNumber = Integer.parseInt(input);
                                                int compareNumber = Integer.parseInt(compare);
                                                return inputNumber >= compareNumber;
                                            } catch (NumberFormatException e) {
                                                return false;
                                            }
                                        }
                                    };
                                    requirements.add(requirement);
                                    break;
                                }
                                case "==": {
                                    String input = "";
                                    String compare = "";
                                    if (yaml.contains(path + "." + requirementName + ".input")) {
                                        if (yaml.isString(path + "." + requirementName + ".input")) {
                                            input = yaml.getString(path + "." + requirementName + ".input");
                                        }
                                    }
                                    if (yaml.contains(path + "." + requirementName + ".compare")) {
                                        if (yaml.isString(path + "." + requirementName + ".compare")) {
                                            input = yaml.getString(path + "." + requirementName + ".compare");
                                        }
                                    }
                                    Requirement requirement = new Requirement(requirementName, requirementType, new String[]{input, compare}) {
                                        @Override
                                        public boolean verify(Player player) {
                                            String input = StringUtil.applyPlaceholderAPI(getValues()[0], player);
                                            String compare = StringUtil.applyPlaceholderAPI(getValues()[1], player);
                                            try {
                                                int inputNumber = Integer.parseInt(input);
                                                int compareNumber = Integer.parseInt(compare);
                                                return inputNumber == compareNumber;
                                            } catch (NumberFormatException e) {
                                                return false;
                                            }
                                        }
                                    };
                                    requirements.add(requirement);
                                    break;
                                }
                                case "<": {
                                    String input = "";
                                    String compare = "";
                                    if (yaml.contains(path + "." + requirementName + ".input")) {
                                        if (yaml.isString(path + "." + requirementName + ".input")) {
                                            input = yaml.getString(path + "." + requirementName + ".input");
                                        }
                                    }
                                    if (yaml.contains(path + "." + requirementName + ".compare")) {
                                        if (yaml.isString(path + "." + requirementName + ".compare")) {
                                            input = yaml.getString(path + "." + requirementName + ".compare");
                                        }
                                    }
                                    Requirement requirement = new Requirement(requirementName, requirementType, new String[]{input, compare}) {
                                        @Override
                                        public boolean verify(Player player) {
                                            String input = StringUtil.applyPlaceholderAPI(getValues()[0], player);
                                            String compare = StringUtil.applyPlaceholderAPI(getValues()[1], player);
                                            try {
                                                int inputNumber = Integer.parseInt(input);
                                                int compareNumber = Integer.parseInt(compare);
                                                return inputNumber < compareNumber;
                                            } catch (NumberFormatException e) {
                                                return false;
                                            }
                                        }
                                    };
                                    requirements.add(requirement);
                                    break;
                                }
                                case "<=": {
                                    String input = "";
                                    String compare = "";
                                    if (yaml.contains(path + "." + requirementName + ".input")) {
                                        if (yaml.isString(path + "." + requirementName + ".input")) {
                                            input = yaml.getString(path + "." + requirementName + ".input");
                                        }
                                    }
                                    if (yaml.contains(path + "." + requirementName + ".compare")) {
                                        if (yaml.isString(path + "." + requirementName + ".compare")) {
                                            input = yaml.getString(path + "." + requirementName + ".compare");
                                        }
                                    }
                                    Requirement requirement = new Requirement(requirementName, requirementType, new String[]{input, compare}) {
                                        @Override
                                        public boolean verify(Player player) {
                                            String input = StringUtil.applyPlaceholderAPI(getValues()[0], player);
                                            String compare = StringUtil.applyPlaceholderAPI(getValues()[1], player);
                                            try {
                                                int inputNumber = Integer.parseInt(input);
                                                int compareNumber = Integer.parseInt(compare);
                                                return inputNumber <= compareNumber;
                                            } catch (NumberFormatException e) {
                                                return false;
                                            }
                                        }
                                    };
                                    requirements.add(requirement);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return requirements;
    }

    public List<Action> loadActions(SimpleYaml yaml, String path) {
        List<Action> actions = new ArrayList<>();
        for (String actionString:yaml.getStringList(path)) actions.add(new Action(actionString));
        return actions;
    }

    private void preProcessAction(String action, Player player) {
        if (action.startsWith("[player]")) {
            String command = action.replaceFirst("[player] ", "");
            Bukkit.getServer().dispatchCommand(player, command);
        } else if (action.startsWith("[console]")) {
            String command = action.replaceFirst("[player] ", "");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        } else if (action.startsWith("[sound]")) {
            String sound = action.split(" ")[1];
            int volume = 1;
            int pitch = 1;
            if (action.split(" ").length > 2) {
                try {
                    volume = Integer.parseInt(action.split(" ")[2]);
                } catch (NumberFormatException ignored) {}
            }
            if (action.split(" ").length > 3) {
                try {
                    pitch = Integer.parseInt(action.split(" ")[3]);
                } catch (NumberFormatException ignored) {}
            }
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        } else if (action.startsWith("[close]")){
            player.closeInventory();
        }  else if (action.startsWith("[return]")){
            openLastInventory();
        } else {
            prosesAction(action, player);
        }
    }

    public abstract void openLastInventory();
    public abstract String[][] getReplacements();
    public abstract void prosesAction(String action, Player player);

    private List<Integer> getSlots(List<String> slotsString) {
        List<Integer> slots = new ArrayList<>();
        for (String slotString:slotsString) {
            if (slotString.contains("-")) {
                String startSlot = slotString.split("-")[0];
                String endSlot = slotString.split("-")[1];
                int startSlotInteger = Integer.parseInt(startSlot);
                int endSlotInteger = Integer.parseInt(endSlot);
                try {
                    for (int i = startSlotInteger; i <= endSlotInteger;i++) {
                        if (!slots.contains(i)) {
                            slots.add(i);
                        }
                    }
                } catch (NumberFormatException ignored) {}
            } else {
                try {
                    int slotInteger = Integer.parseInt(slotString);
                    if (!slots.contains(slotInteger)) {
                        slots.add(slotInteger);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return slots;
    }

}