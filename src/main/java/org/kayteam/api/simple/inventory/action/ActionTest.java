package org.kayteam.api.simple.inventory.action;

import java.util.ArrayList;
import java.util.List;

public class ActionTest {

    public static void main(String[] args) {
        List<String> actions = new ArrayList<>();
        actions.add("[close]");
        actions.add("[open_last_menu]");
        actions.add("[open_menu] classic");
        actions.add("[player] warp pvp");
        actions.add("[operator] gamemode creative");
        actions.add("[console] op %player_name%");
        actions.add("[message] Congratulation you are the best!");
        actions.add("[broadcast] This is an announce!");
        actions.add("[sound] VILLAGER_NO 1 1");
        actions.add("[sounds] VILLAGER_NO 1 1");
        for (String stringAction:actions) {
            Action action = new Action(stringAction);
            System.out.println("ActionType: " + action.getActionType());
            System.out.println("Value: " + action.getValue());
            System.out.println("------------------------------------");
        }
    }

}