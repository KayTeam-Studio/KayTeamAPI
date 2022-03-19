package org.kayteam.api.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.api.simple.yaml.SimpleYaml;

import java.awt.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscordUtil {

    private final JavaPlugin javaPlugin;

    public DiscordUtil(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public WebhookClient getWebhookClient(String webhookUrl) {
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token
        return builder.build();
    }

    public WebhookEmbed getWebhookEmbedFromFile(SimpleYaml file, String path){
        OffsetDateTime offsetDateTime = null;
        Integer color = null;
        String description = null;
        String thumbnailUrl = null;
        String imageUrl = null;
        WebhookEmbed.EmbedFooter footer = null;
        WebhookEmbed.EmbedTitle title = null;
        WebhookEmbed.EmbedAuthor author = null;
        List<WebhookEmbed.EmbedField> fields = new ArrayList<>();
        if(file.getBoolean(path+".timestamp")){
            offsetDateTime = OffsetDateTime.now();
        }
        if(file.contains(path+".color")){
            color = Integer.parseInt(file.getString(path+".color").replaceFirst("#", ""), 16);
        }
        if(file.contains(path+".description")){
            description = file.getString(path+".description");
        }
        if(file.contains(path+".thumbnailUrl")){
            thumbnailUrl = file.getString(path+".thumbnailUrl");
        }
        if(file.contains(path+".imageUrl")){
            imageUrl = file.getString(path+".imageUrl");
        }
        if(file.contains(path+".footerText")){
            if(file.contains(path+".footerIcon")){
                footer = new WebhookEmbed.EmbedFooter(file.getString(path+".footerText"), file.getString(path+".footerIcon"));
            }else{
                footer = new WebhookEmbed.EmbedFooter(file.getString(path+".footerText"), null);
            }
        }
        if(file.contains(path+".title")){
            if(file.contains(path+".titleUrl")){
                title = new WebhookEmbed.EmbedTitle(file.getString(path+".title"), file.getString(path+".titleUrl"));
            }else{
                title = new WebhookEmbed.EmbedTitle(file.getString(path+".title"), null);
            }
        }
        if(file.contains(path+".authorName")){
            String authorIcon = null;
            String authorUrl = null;
            if(file.contains(path+".authorIcon")){
                authorIcon = file.getString(path+".authorIcon");
            }
            if(file.contains(path+".authorUrl")){
                authorUrl = file.getString(path+".authorUrl");
            }
            author = new WebhookEmbed.EmbedAuthor(file.getString(path+".authorName"), authorIcon, authorUrl);
        }
        if(file.contains(path+".fields")){
            for(String fieldKey : Objects.requireNonNull(file.getConfigurationSection(path + ".fields")).getKeys(false)){
                boolean inline = false;
                String name = null;
                String value = null;
                if(file.contains(path+".fields."+fieldKey+".inline")){
                    inline = file.getBoolean(path+".fields."+fieldKey+".inline");
                }
                if(file.contains(path+".fields."+fieldKey+".name")){
                    name = file.getString(path+".fields."+fieldKey+".name");
                }
                if(file.contains(path+".fields."+fieldKey+".value")){
                    value = file.getString(path+".fields."+fieldKey+".value");
                }
                WebhookEmbed.EmbedField embedField = new WebhookEmbed.EmbedField(inline, name, value);
                fields.add(embedField);
            }
        }
        return new WebhookEmbed(offsetDateTime, color, description, thumbnailUrl, imageUrl, footer, title, author, fields);
    }

    public WebhookEmbed getWebhookEmbedFromFile(SimpleYaml file, String path, String[][] replacements){
        OffsetDateTime offsetDateTime = null;
        Integer color = null;
        String description = null;
        String thumbnailUrl = null;
        String imageUrl = null;
        WebhookEmbed.EmbedFooter footer = null;
        WebhookEmbed.EmbedTitle title = null;
        WebhookEmbed.EmbedAuthor author = null;
        List<WebhookEmbed.EmbedField> fields = new ArrayList<>();
        if(file.getBoolean(path+".timestamp")){
            offsetDateTime = OffsetDateTime.now();
        }
        if(file.contains(path+".color")){
            color = Integer.parseInt(file.getString(path+".color", replacements).replaceFirst("#", ""), 16);
        }
        if(file.contains(path+".description")){
            description = file.getString(path+".description", replacements);
        }
        if(file.contains(path+".thumbnailUrl")){
            thumbnailUrl = file.getString(path+".thumbnailUrl", replacements);
        }
        if(file.contains(path+".imageUrl")){
            imageUrl = file.getString(path+".imageUrl", replacements);
        }
        if(file.contains(path+".footerText")){
            if(file.contains(path+".footerIcon")){
                footer = new WebhookEmbed.EmbedFooter(file.getString(path+".footerText", replacements), file.getString(path+".footerIcon", replacements));
            }else{
                footer = new WebhookEmbed.EmbedFooter(file.getString(path+".footerText", replacements), null);
            }
        }
        if(file.contains(path+".title")){
            if(file.contains(path+".titleUrl")){
                title = new WebhookEmbed.EmbedTitle(file.getString(path+".title", replacements), file.getString(path+".titleUrl", replacements));
            }else{
                title = new WebhookEmbed.EmbedTitle(file.getString(path+".title", replacements), null);
            }
        }
        if(file.contains(path+".authorName")){
            String authorIcon = null;
            String authorUrl = null;
            if(file.contains(path+".authorIcon")){
                authorIcon = file.getString(path+".authorIcon", replacements);
            }
            if(file.contains(path+".authorUrl")){
                authorUrl = file.getString(path+".authorUrl", replacements);
            }
            author = new WebhookEmbed.EmbedAuthor(file.getString(path+".authorName", replacements), authorIcon, authorUrl);
        }
        if(file.contains(path+".fields")){
            for(String fieldKey : Objects.requireNonNull(file.getConfigurationSection(path + ".fields")).getKeys(false)){
                boolean inline = false;
                String name = null;
                String value = null;
                if(file.contains(path+".fields."+fieldKey+".inline")){
                    inline = file.getBoolean(path+".fields."+fieldKey+".inline");
                }
                if(file.contains(path+".fields."+fieldKey+".name")){
                    name = file.getString(path+".fields."+fieldKey+".name", replacements);
                }
                if(file.contains(path+".fields."+fieldKey+".value")){
                    value = file.getString(path+".fields."+fieldKey+".value", replacements);
                }
                WebhookEmbed.EmbedField embedField = new WebhookEmbed.EmbedField(inline, name, value);
                fields.add(embedField);
            }
        }
        return new WebhookEmbed(offsetDateTime, color, description, thumbnailUrl, imageUrl, footer, title, author, fields);
    }

    public EmbedBuilder getEmbedFromFile(SimpleYaml file, String path){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if(file.contains(path+".authorName")){
            String authorIcon = null;
            String authorUrl = null;
            if(file.contains(path+".authorIcon")){
                authorIcon = file.getString(path+".authorIcon");
            }
            if(file.contains(path+".authorUrl")){
                authorUrl = file.getString(path+".authorUrl");
            }
            embedBuilder.setAuthor(file.getString(path+".authorName"), authorUrl, authorIcon);
        }
        if(file.contains(path+".color")){
            embedBuilder.setColor(hexToColor(file.getString(path+".color")));
        }
        if(file.contains(path+".imageUrl")){
            embedBuilder.setImage(file.getString(path+".imageUrl"));
        }
        if(file.contains(path+".thumbnailUrl")){
            embedBuilder.setThumbnail(file.getString(path+".thumbnailUrl"));
        }
        if(file.contains(path+".title")){
            if(file.contains(path+".titleUrl")){
                embedBuilder.setTitle(file.getString(path+".title"), file.getString(path+".titleUrl"));
            }else{
                embedBuilder.setTitle(file.getString(path+".title"));
            }
        }
        if(file.contains(path+".description")){
            embedBuilder.setDescription(file.getString(path+".description"));
        }
        if(file.contains(path+".footerText")){
            if(file.contains(path+".footerIcon")){
                embedBuilder.setFooter(file.getString(path+".footerText"), file.getString(path+".footerIcon"));
            }else{
                embedBuilder.setFooter(file.getString(path+".footerText"));
            }
        }
        if(file.getBoolean(path+".timestamp")){
            embedBuilder.setTimestamp(Instant.now());
        }
        if(file.contains(path+".fields")){
            for(String fieldKey : Objects.requireNonNull(file.getConfigurationSection(path + ".fields")).getKeys(false)){
                boolean inline = false;
                String name = null;
                String value = null;
                if(file.contains(path+".fields."+fieldKey+".inline")){
                    inline = file.getBoolean(path+".fields."+fieldKey+".inline");
                }
                if(file.contains(path+".fields."+fieldKey+".name")){
                    name = file.getString(path+".fields."+fieldKey+".name");
                }
                if(file.contains(path+".fields."+fieldKey+".value")){
                    value = file.getString(path+".fields."+fieldKey+".value");
                }
                embedBuilder.addField(name, value, inline);
            }
        }
        return embedBuilder;
    }

    public EmbedBuilder getEmbedFromFile(SimpleYaml file, String path, String[][] replacements){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if(file.contains(path+".authorName")){
            String authorIcon = null;
            String authorUrl = null;
            if(file.contains(path+".authorIcon")){
                authorIcon = file.getString(path+".authorIcon", replacements);
            }
            if(file.contains(path+".authorUrl")){
                authorUrl = file.getString(path+".authorUrl", replacements);
            }
            embedBuilder.setAuthor(file.getString(path+".authorName", replacements), authorUrl, authorIcon);
        }
        if(file.contains(path+".color")){
            embedBuilder.setColor(hexToColor(file.getString(path+".color", replacements)));
        }

        if(file.contains(path+".imageUrl")){
            embedBuilder.setImage(file.getString(path+".imageUrl", replacements));
        }
        if(file.contains(path+".thumbnailUrl")){
            embedBuilder.setThumbnail(file.getString(path+".thumbnailUrl", replacements));
        }
        if(file.contains(path+".title")){
            if(file.contains(path+".titleUrl")){
                embedBuilder.setTitle(file.getString(path+".title", replacements), file.getString(path+".titleUrl", replacements));
            }else{
                embedBuilder.setTitle(file.getString(path+".title", replacements));
            }
        }
        if(file.contains(path+".description")){
            embedBuilder.setDescription(file.getString(path+".description", replacements));
        }
        if(file.contains(path+".footerText")){
            if(file.contains(path+".footerIcon")){
                embedBuilder.setFooter(file.getString(path+".footerText", replacements), file.getString(path+".footerIcon", replacements));
            }else{
                embedBuilder.setFooter(file.getString(path+".footerText", replacements));
            }
        }
        if(file.getBoolean(path+".timestamp")){
            embedBuilder.setTimestamp(Instant.now());
        }
        if(file.contains(path+".fields")){
            for(String fieldKey : Objects.requireNonNull(file.getConfigurationSection(path + ".fields")).getKeys(false)){
                boolean inline = false;
                String name = null;
                String value = null;
                if(file.contains(path+".fields."+fieldKey+".inline")){
                    inline = file.getBoolean(path+".fields."+fieldKey+".inline");
                }
                if(file.contains(path+".fields."+fieldKey+".name")){
                    name = file.getString(path+".fields."+fieldKey+".name", replacements);
                }
                if(file.contains(path+".fields."+fieldKey+".value")){
                    value = file.getString(path+".fields."+fieldKey+".value", replacements);
                }
                embedBuilder.addField(name, value, inline);
            }
        }
        return embedBuilder;
    }

    public static final Color hexToColor(String value) {
        String digits;
        if (value.startsWith("#")) {
            digits = value.substring(1, Math.min(value.length(), 7));
        } else {
            digits = value;
        }
        String hstr = "0x" + digits;
        Color c;
        try {
            c = Color.decode(hstr);
        } catch (NumberFormatException nfe) {
            c = null;
        }
        return c;
    }
}