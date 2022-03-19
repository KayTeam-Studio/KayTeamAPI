package org.kayteam.api.simple.inventory.action;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Action {

    private final ActionType actionType;
    private final String value;

    public Action(String actionString) {
        actionType = getActionTypeFromActionString(actionString);
        value = getValueFromActionString(actionString);
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getValue() {
        return value;
    }

    public static ActionType getActionTypeFromActionString(String actionString) {
        String stringType;
        if (actionString.contains(" ")) {
            stringType = actionString.split(" ")[0];
        } else {
            stringType = actionString;
        }
        stringType = stringType.replaceAll("\\[", "");
        stringType = stringType.replaceAll("]", "");
        try {
            return ActionType.valueOf(stringType);
        } catch (IllegalArgumentException ignore) {
            return null;
        }
    }

    public static String getValueFromActionString(String actionString) {
        StringBuilder stringValue = new StringBuilder("");
        if (actionString.contains(" ")) {
            String[] args = actionString.split(" ");
            for (int i = 1; i < args.length; i++) {
                String arg = args[i];
                stringValue.append(arg);
                stringValue.append(" ");
            }
            stringValue.deleteCharAt(stringValue.length() - 1);
        }
        return stringValue.toString();
    }

    public void runAction(JavaPlugin javaPlugin, Player player) {
        //TODO Completar runAction
    }

}