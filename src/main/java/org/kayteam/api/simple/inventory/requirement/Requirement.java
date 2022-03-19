package org.kayteam.api.simple.inventory.requirement;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Requirement {

    private final String name;
    private final String type;
    private final String[] values;

    public Requirement(String name, String type, String[] values) {
        this.name = name;
        this.type = type;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String[] getValues() {
        return values;
    }

    public abstract boolean verify(Player player);

}