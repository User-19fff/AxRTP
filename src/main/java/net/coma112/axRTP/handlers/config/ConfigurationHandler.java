package net.coma112.axrtp.handlers.config;

import lombok.extern.slf4j.Slf4j;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.processor.MessageProcessor;
import net.coma112.axrtp.utils.LoggerUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class ConfigurationHandler {
    private YamlConfiguration yml;
    private final File config;

    public ConfigurationHandler(@NotNull YamlConfiguration yml, @NotNull File config) {
        this.yml = yml;
        this.config = config;
    }

    @Contract("_, _ -> new")
    public static @NotNull ConfigurationHandler of(@NotNull String dir, @NotNull String name) {
        Path directory = Path.of(dir);

        try {
            Files.createDirectories(directory);
        } catch (IOException exception) {
            LoggerUtils.error("Failed to create directories: " + exception.getMessage());
            throw new RuntimeException(exception);
        }

        Path configPath = directory.resolve(name + ".yml");
        File configFile = configPath.toFile();

        if (!configFile.exists()) {
            try {
                Files.createFile(configPath);
            } catch (IOException exception) {
                LoggerUtils.error("Failed to create config file: " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);

        yml.options().copyDefaults(true);
        return new ConfigurationHandler(yml, configFile);
    }

    public void reload() {
        yml = YamlConfiguration.loadConfiguration(config);
        yml.options().copyDefaults(true);
        save();
    }

    public void set(@NotNull String path, @NotNull Object value) {
        yml.set(path, value);
    }

    public void save() {
        try {
            yml.save(config);
        } catch (IOException exception) {
            LoggerUtils.error("Failed to save config: " + exception.getMessage());
        }
    }

    public List<String> getList(@NotNull String path) {
        return yml.getStringList(path).stream()
                .map(MessageProcessor::process)
                .toList();
    }

    public boolean getBoolean(@NotNull String path) {
        return yml.getBoolean(path);
    }

    public int getInt(@NotNull String path) {
        return yml.getInt(path);
    }

    public String getString(@NotNull String path) {
        return yml.getString(path);
    }

    public double getDouble(@NotNull String path) {
        return yml.getDouble(path);
    }

    public @Nullable ConfigurationSection getSection(@NotNull String path) {
        return yml.getConfigurationSection(path);
    }

    public static void saveResourceIfNotExists(@NotNull String resourcePath) {
        File targetFile = new File(AxRTP.getInstance().getDataFolder(), resourcePath);

        if (!targetFile.exists()) AxRTP.getInstance().saveResource(resourcePath, false);
    }
}

