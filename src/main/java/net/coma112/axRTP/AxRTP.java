package net.coma112.axrtp;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import lombok.Getter;
import net.coma112.axrtp.config.Config;
import net.coma112.axrtp.handlers.config.ConfigurationHandler;
import net.coma112.axrtp.handlers.utils.CooldownHandler;
import net.coma112.axrtp.handlers.utils.LockdownHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.language.Language;
import net.coma112.axrtp.utils.RegisterUtils;
import revxrsal.zapper.ZapperJavaPlugin;
import java.util.HashMap;

public final class AxRTP extends ZapperJavaPlugin {
    @Getter private static AxRTP instance;
    @Getter private TaskScheduler scheduler;
    @Getter private Language language;
    @Getter private LockdownHandler lockdownHandler;
    @Getter private CooldownHandler cooldownHandler;
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

        lockdownHandler = new LockdownHandler(new HashMap<>(), ConfigKeys.TELEPORT_COOLDOWN_LOCK_AFTER.getInt());
        cooldownHandler = new CooldownHandler();
    }

    public Config getConfiguration() {
        return config;
    }

    private void initializeComponents() {
        config = new Config();

        ConfigurationHandler.saveResourceIfNotExists("messages.yml");
        ConfigurationHandler.saveResourceIfNotExists("config.yml");

        language = new Language();
    }
}
