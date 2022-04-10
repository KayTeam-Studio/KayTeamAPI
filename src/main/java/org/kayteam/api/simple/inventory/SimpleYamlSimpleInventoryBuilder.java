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

import org.bukkit.inventory.ItemStack;
import org.kayteam.api.simple.yaml.SimpleYaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SimpleYamlSimpleInventoryBuilder extends SimpleInventoryBuilder {

    private final SimpleYaml simpleYaml;


    public SimpleYamlSimpleInventoryBuilder(SimpleYaml simpleYaml) {
        super(simpleYaml.getName(), simpleYaml.getString("title", "Default title"), simpleYaml.getInt("rows", 3));
        this.simpleYaml = simpleYaml;
        int updateInterval = simpleYaml.getInt("updateInterval", 0);
        /*
        if (simpleYaml.contains("items") && simpleYaml.isConfigurationSection("items")) {
            for (String itemName: Objects.requireNonNull(simpleYaml.getConfigurationSection("items")).getKeys(false)) {
                ItemStack itemStack = simpleYaml.getItemStack("items." + itemName);
                if (simpleYaml.contains("items." + itemName + ".slots")) {
                    List<Integer> slots = getSlots(simpleYaml.getStringList( "items." + itemName + ".slots"));
                    for (Integer slotNumber:slots) {
                        Slot slot = new Slot(slotNumber);
                        if (getSlots().containsKey(slotNumber)) slot = getSlots().get(slotNumber);
                        
                        addItem(slot, () -> SimpleYaml.replace(itemStack, player, getReplacements()));

                        if (simpleYaml.contains(path + ".items." + itemName + ".update") && simpleYaml.isBoolean(path + ".items." + itemName + ".update")) {
                            if (simpleYaml.getBoolean(path + ".items." + itemName + ".update")) {
                                setUpdatable(slot, true);
                                setUpdateInterval(slot, updateInterval);
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".left-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addLeftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".left-click-actions");
                                leftClickActions.put(slot, clickActionList);
                                addLeftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.leftClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".left-shift-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addLeftShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".left-shift-click-actions");
                                leftShiftClickActions.put(slot, clickActionList);
                                addLeftShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.leftShiftClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".middle-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addMiddleAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".middle-click-actions");
                                middleClickActions.put(slot, clickActionList);
                                addMiddleAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.middleClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".right-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addRightAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".right-click-actions");
                                rightClickActions.put(slot, clickActionList);
                                addRightAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.rightClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".right-shift-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addRightShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".right-shift-click-actions");
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

         */
    }

    public SimpleYamlSimpleInventoryBuilder(SimpleYaml simpleYaml, String path) {
        super(path, simpleYaml.getString(path + ".title", "Default title"), simpleYaml.getInt(path + ".rows", 3));
        this.simpleYaml = simpleYaml;
        int updateInterval = 0;
        if (simpleYaml.contains(path + ".title.update-interval") && simpleYaml.isBoolean(path + ".title.update-interval")) {
            updateInterval = simpleYaml.getInt(path + ".title.update-interval");
        }
        /*
        if (simpleYaml.contains(path + ".items") && simpleYaml.isConfigurationSection(path + ".items")) {
            for (String itemName: Objects.requireNonNull(simpleYaml.getConfigurationSection(path + ".items")).getKeys(false)) {
                ItemStack itemStack = simpleYaml.getItemStack(path + ".items." + itemName);
                if (simpleYaml.contains(path + ".items." + itemName + ".slots")) {
                    List<Integer> slots = getSlots(simpleYaml.getStringList(path + ".items." + itemName + ".slots"));
                    for (Integer slot:slots) {
                        addItem(slot, () -> SimpleYaml.replace(itemStack, player, getReplacements()));

                        if (simpleYaml.contains(path + ".items." + itemName + ".update") && simpleYaml.isBoolean(path + ".items." + itemName + ".update")) {
                            if (simpleYaml.getBoolean(path + ".items." + itemName + ".update")) {
                                setUpdatable(slot, true);
                                setUpdateInterval(slot, updateInterval);
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".left-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addLeftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".left-click-actions");
                                leftClickActions.put(slot, clickActionList);
                                addLeftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.leftClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".left-shift-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addLeftShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".left-shift-click-actions");
                                leftShiftClickActions.put(slot, clickActionList);
                                addLeftShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.leftShiftClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".middle-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addMiddleAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".middle-click-actions");
                                middleClickActions.put(slot, clickActionList);
                                addMiddleAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.middleClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".right-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addRightAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".right-click-actions");
                                rightClickActions.put(slot, clickActionList);
                                addRightAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.rightClickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            }
                        }

                        if (simpleYaml.contains(path + ".items." + itemName + ".right-shift-click-actions") || simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                            if (simpleYaml.contains(path + ".items." + itemName + ".click-actions")) {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".click-actions");
                                clickActions.put(slot, clickActionList);
                                addRightShiftAction(slot, (player1, slot1) -> {
                                    List<String> clickActions = this.clickActions.get(slot);
                                    for (String clickAction:clickActions) {
                                        preProcessAction(clickAction, player1);
                                    }
                                });
                            } else {
                                List<String> clickActionList = simpleYaml.getStringList(path + ".items." + itemName + ".right-shift-click-actions");
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

         */
    }

    public SimpleYaml getSimpleYaml() {
        return simpleYaml;
    }

    public abstract String[][] getReplacements();

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