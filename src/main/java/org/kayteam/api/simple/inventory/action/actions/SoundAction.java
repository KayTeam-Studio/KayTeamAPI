package org.kayteam.api.simple.inventory.action.actions;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.kayteam.api.simple.inventory.SimpleInventoryView;
import org.kayteam.api.simple.inventory.action.Action;
import org.kayteam.api.simple.inventory.action.ActionType;

public class SoundAction extends Action {

    private final String sound;
    private final int volume;
    private final int pitch;

    public SoundAction(String name, String sound) {
        super(name, ActionType.SOUND);
        this.sound = sound;
        this.volume = 1;
        this.pitch = 1;
    }

    public SoundAction(String name, String sound, int volume) {
        super(name, ActionType.SOUND);
        this.sound = sound;
        this.volume = volume;
        this.pitch = 1;
    }

    public SoundAction(String name, String sound, int volume, int pitch) {
        super(name, ActionType.SOUND);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(Player player, SimpleInventoryView simpleInventoryView) {
        player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
    }

}