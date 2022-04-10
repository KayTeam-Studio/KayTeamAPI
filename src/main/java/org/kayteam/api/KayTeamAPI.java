package org.kayteam.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.api.bStats.Metrics;
import org.kayteam.api.discord.DiscordUtil;

public final class KayTeamAPI extends JavaPlugin {

    private DiscordUtil discordUtil;

    @Override
    public void onEnable() {
        bStats();
        enableDiscordUtil();
    }

    @Override
    public void onDisable() {
        BrandSender.sendBrandMessage(this, "&cDisabled");
    }

    private void bStats() {
        int pluginId = 13953;
        Metrics metrics = new Metrics(this, pluginId);
    }

    private void enableDiscordUtil() {
        try {
            Class.forName("net.dv8tion.jda.api.JDA");
            discordUtil = new DiscordUtil(this);
        } catch (ClassNotFoundException ignored) {}
    }

    public DiscordUtil getDiscordUtil() {
        return discordUtil;
    }

}