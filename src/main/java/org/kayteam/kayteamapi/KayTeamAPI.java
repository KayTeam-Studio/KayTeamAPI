package org.kayteam.kayteamapi;

import org.bukkit.plugin.java.JavaPlugin;

public final class KayTeamAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        BrandSender.sendBrandMessage(this, "&aEnabled");
    }

    @Override
    public void onDisable() {
        BrandSender.sendBrandMessage(this, "&cDisabled");
    }

}