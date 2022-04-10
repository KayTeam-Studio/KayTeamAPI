package org.kayteam.api.simple.inventory;

import org.bukkit.inventory.ItemStack;

public interface GetItem {

    public ItemStack getItem();

    public boolean isAsync();

}