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

package org.kayteam.api.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.api.yaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class SimpleInventoryBuilder extends InventoryBuilder {

    private final HashMap<Integer, List<String>> clickActions = new HashMap<>();
    private final HashMap<Integer, List<String>> leftClickActions = new HashMap<>();
    private final HashMap<Integer, List<String>> leftShiftClickActions = new HashMap<>();
    private final HashMap<Integer, List<String>> middleClickActions = new HashMap<>();
    private final HashMap<Integer, List<String>> rightClickActions = new HashMap<>();
    private final HashMap<Integer, List<String>> rightShiftClickActions = new HashMap<>();

    private final Yaml yaml;
    private final String path;
    private final String from;
    private final Player player;

    public SimpleInventoryBuilder(Yaml yaml, String path, String from, Player player) {
        super(yaml.getString( path + ".title"), yaml.getInt(path + ".rows"));
        this.yaml = yaml;
        this.path = path;
        this.from = from;
        this.player = player;
        int updateInterval = 0;
        if (yaml.contains(path + ".title.update-interval") && yaml.isBoolean(path + ".title.update-interval")) {
            updateInterval = yaml.getInt(path + ".title.update-interval");
        }
        if (yaml.contains(path + ".items") && yaml.getFileConfiguration().isConfigurationSection(path + ".items")) {
            for (String itemName: Objects.requireNonNull(yaml.getFileConfiguration().getConfigurationSection(path + ".items")).getKeys(false)) {
                ItemStack itemStack = yaml.getItemStack(path + ".items." + itemName);
                if (yaml.contains(path + ".items." + itemName + ".slots")) {
                    List<Integer> slots = getSlots(yaml.getStringList(path + ".items." + itemName + ".slots"));
                    for (Integer slot:slots) {
                        addItem(slot, () -> Yaml.replace(itemStack, player, getReplacements()));

                        if (yaml.contains(path + ".items." + itemName + ".update") && yaml.isBoolean(path + ".items." + itemName + ".update")) {
                            if (yaml.getBoolean(path + ".items." + itemName + ".update")) {
                                setUpdatable(slot, true);
                                setUpdateInterval(slot, updateInterval);
                            }
                        }

                        if (yaml.contains(path + ".items." + itemName + ".left-click-actions") || yaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (yaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addLeftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".left-click-actions");
                                leftClickActions.put(slot, clickActionList);
                                addLeftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.leftClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (yaml.contains(path + ".items." + itemName + ".left-shift-click-actions") || yaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (yaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addLeftShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".left-shift-click-actions");
                                leftShiftClickActions.put(slot, clickActionList);
                                addLeftShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.leftShiftClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (yaml.contains(path + ".items." + itemName + ".middle-click-actions") || yaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (yaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addMiddleAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".middle-click-actions");
                                middleClickActions.put(slot, clickActionList);
                                addMiddleAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.middleClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (yaml.contains(path + ".items." + itemName + ".right-click-actions") || yaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (yaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addRightAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".right-click-actions");
                                rightClickActions.put(slot, clickActionList);
                                addRightAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.rightClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (yaml.contains(path + ".items." + itemName + ".right-shift-click-actions") || yaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (yaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addRightShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = yaml.getStringList(path + ".items." + itemName + ".right-shift-click-actions");
                                rightShiftClickActions.put(slot, clickActionList);
                                addRightShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.rightShiftClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                    }
                }
            }
        }
    }

    public Yaml getYaml() {
        return yaml;
    }

    public String getPath() {
        return path;
    }

    public String getFrom() {
        return from;
    }

    public Player getPlayer() {
        return player;
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