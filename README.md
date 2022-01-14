# KayTeamAPI

## Maven
```XML
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```XML
<dependency>
    <groupId>com.github.KayTeam-Studio</groupId>
    <artifactId>KayTeamAPI</artifactId>
    <version>VERSION</version>
</dependency>
```
## Gradle
```groovy
repositories {
    maven {url'https://jitpack.io' }
}
```

```groovy
dependencies {
    implementation 'com.github.KayTeam-Studio:KayTeamAPI:VERSION'
}
```
Check the last released version in Releases section

###How to use the API
####YAML Usage:

```java
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.api.yaml.Yaml;

public class pluginExample extends JavaPlugin {

    // Create Yaml
    private final Yaml file;

    @Override
    public void onEnable() {
        file = new Yaml(this, "nameWithoutExtension");
        // Register FileConfiguration
        file.registerFileConfiguration();
        // Reload FileConfiguration
        file.reloadFileConfiguration();
        // Get FileConfiguration
        file.getFileConfiguration();
        // Delete FileConfiguration
        file.deleteFileConfiguration();
        // Save FileConfiguration
        file.saveFileConfiguration();
        
        // Set values
        file.set("string", "text");
        file.set("integer", 1);
        file.set("double", 1.0);
        file.set("boolean", true);
        // and more
        
        // Get String
        file.getString("path");
        // Get String
        file.getString("path", "defaultValue");
    }

}
```