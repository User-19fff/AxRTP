package net.coma112.axrtp;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import lombok.Getter;
import net.coma112.axrtp.config.Config;
import net.coma112.axrtp.handlers.ConfigurationHandler;
import net.coma112.axrtp.handlers.LockAfterHandler;
import net.coma112.axrtp.hooks.PlaceholderAPI;
import net.coma112.axrtp.identifiers.LanguageTypes;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.language.Language;
import net.coma112.axrtp.utils.RegisterUtils;
import revxrsal.zapper.ZapperJavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

public final class AxRTP extends ZapperJavaPlugin {
    @Getter private static AxRTP instance;
    @Getter private TaskScheduler scheduler;
    @Getter private Language language;
    @Getter private LockAfterHandler lockAfterHandler;
    private Config config;

    @Override
    public void onLoad() {
        instance = this;
        scheduler = UniversalScheduler.getScheduler(this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeComponents();

        RegisterUtils.registerCommands();
        RegisterUtils.registerListeners();
        PlaceholderAPI.registerHook();

        lockAfterHandler = new LockAfterHandler(new HashMap<>(), ConfigKeys.TELEPORT_COOLDOWN_LOCK_AFTER.getInt());
    }

    public Config getConfiguration() {
        return config;
    }

    private void initializeComponents() {
        config = new Config();

        Arrays.stream(LanguageTypes.values()).forEach(type -> ConfigurationHandler.saveResourceIfNotExists("locales/messages_" + type.name().toLowerCase() + ".yml"));
        ConfigurationHandler.saveResourceIfNotExists("config.yml");

        language = new Language("messages_" + LanguageTypes.valueOf(ConfigKeys.LANGUAGE.getString().toUpperCase()).name().toLowerCase());
    }
}
