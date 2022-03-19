package org.kayteam.api.simple.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kayteam.api.StringUtil;
import org.kayteam.api.simple.inventory.action.Action;
import org.kayteam.api.simple.inventory.requirement.Requirement;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private final ItemStack itemStack;
    private final boolean update;
    private final String displayName;
    private final List<String> lore;
    private final int priority;
    private List<Requirement> viewRequirements;
    private List<Requirement> clickRequirements;
    private List<Action> clickActions;
    private List<Requirement> leftClickRequirements;
    private List<Action> leftClickActions;
    private List<Requirement> leftShiftClickRequirements;
    private List<Action> leftShiftClickActions;
    private List<Requirement> middleClickRequirements;
    private List<Action> middleClickActions;
    private List<Requirement> rightClickRequirements;
    private List<Action> rightClickActions;
    private List<Requirement> rightShiftClickRequirements;
    private List<Action> rightShiftClickActions;

    public Item(ItemStack itemStack, boolean update, String displayName, List<String> lore, int priority) {
        this.itemStack = itemStack;
        this.update = update;
        this.displayName = displayName;
        this.lore = lore;
        this.priority = priority;
        this.viewRequirements = new ArrayList<>();
        this.clickRequirements = new ArrayList<>();
        this.clickActions = new ArrayList<>();
        this.leftClickRequirements = new ArrayList<>();
        this.leftClickActions = new ArrayList<>();
        this.leftShiftClickRequirements = new ArrayList<>();
        this.leftShiftClickActions = new ArrayList<>();
        this.middleClickRequirements = new ArrayList<>();
        this.middleClickActions = new ArrayList<>();
        this.rightClickRequirements = new ArrayList<>();
        this.rightClickActions = new ArrayList<>();
        this.rightShiftClickRequirements = new ArrayList<>();
        this.rightShiftClickActions = new ArrayList<>();
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

    public int getPriority() {
        return priority;
    }

    public List<Requirement> getViewRequirements() {
        return viewRequirements;
    }

    public void setViewRequirements(List<Requirement> viewRequirements) {
        this.viewRequirements = viewRequirements;
    }

    public List<Requirement> getClickRequirements() {
        return clickRequirements;
    }

    public void setClickRequirements(List<Requirement> clickRequirements) {
        this.clickRequirements = clickRequirements;
    }

    public List<Action> getClickActions() {
        return clickActions;
    }

    public void setClickActions(List<Action> clickActions) {
        this.clickActions = clickActions;
    }

    public List<Requirement> getLeftClickRequirements() {
        return leftClickRequirements;
    }

    public void setLeftClickRequirements(List<Requirement> leftClickRequirements) {
        this.leftClickRequirements = leftClickRequirements;
    }

    public List<Action> getLeftClickActions() {
        return leftClickActions;
    }

    public void setLeftClickActions(List<Action> leftClickActions) {
        this.leftClickActions = leftClickActions;
    }

    public List<Requirement> getLeftShiftClickRequirements() {
        return leftShiftClickRequirements;
    }

    public void setLeftShiftClickRequirements(List<Requirement> leftShiftClickRequirements) {
        this.leftShiftClickRequirements = leftShiftClickRequirements;
    }

    public List<Action> getLeftShiftClickActions() {
        return leftShiftClickActions;
    }

    public void setLeftShiftClickActions(List<Action> leftShiftClickActions) {
        this.leftShiftClickActions = leftShiftClickActions;
    }

    public List<Requirement> getMiddleClickRequirements() {
        return middleClickRequirements;
    }

    public void setMiddleClickRequirements(List<Requirement> middleClickRequirements) {
        this.middleClickRequirements = middleClickRequirements;
    }

    public List<Action> getMiddleClickActions() {
        return middleClickActions;
    }

    public void setMiddleClickActions(List<Action> middleClickActions) {
        this.middleClickActions = middleClickActions;
    }

    public List<Requirement> getRightClickRequirements() {
        return rightClickRequirements;
    }

    public void setRightClickRequirements(List<Requirement> rightClickRequirements) {
        this.rightClickRequirements = rightClickRequirements;
    }

    public List<Action> getRightClickActions() {
        return rightClickActions;
    }

    public void setRightClickActions(List<Action> rightClickActions) {
        this.rightClickActions = rightClickActions;
    }

    public List<Requirement> getRightShiftClickRequirements() {
        return rightShiftClickRequirements;
    }

    public void setRightShiftClickRequirements(List<Requirement> rightShiftClickRequirements) {
        this.rightShiftClickRequirements = rightShiftClickRequirements;
    }

    public List<Action> getRightShiftClickActions() {
        return rightShiftClickActions;
    }

    public void setRightShiftClickActions(List<Action> rightShiftClickActions) {
        this.rightShiftClickActions = rightShiftClickActions;
    }

    public ItemStack getItemStack(Player player, String[][] replacements) {
        ItemStack newItemStack = new ItemStack(itemStack);
        ItemMeta newItemMeta = newItemStack.getItemMeta();
        if (!displayName.equals("")) {
            String newDisplayName = new String(displayName);
            newDisplayName = StringUtil.replaceText(newDisplayName, player, replacements);
            newDisplayName = StringUtil.addColor(newDisplayName);
            newItemMeta.setDisplayName(displayName);
        }
        if (!lore.isEmpty()) {
            List<String> newLore = new ArrayList<>();
            for (String line:lore) {
                String newLine = new String(line);
                newLine = StringUtil.replaceText(newLine, player, replacements);
                newLine = StringUtil.addColor(newLine);
                newLore.add(newLine);
            }
            newItemMeta.setLore(newLore);
        }
        newItemStack.setItemMeta(newItemMeta);
        return newItemStack;
    }

}