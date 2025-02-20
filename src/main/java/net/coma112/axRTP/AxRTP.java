package net.coma112.axrtp;

import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.dvs.versioning.BasicVersioning;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.dumper.DumperSettings;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.general.GeneralSettings;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.loader.LoaderSettings;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.updater.UpdaterSettings;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import lombok.Getter;
import com.artillexstudios.axapi.config.Config;
import net.coma112.axrtp.handlers.utils.CooldownHandler;
import net.coma112.axrtp.handlers.utils.LockdownHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.utils.RegisterUtils;
import revxrsal.zapper.ZapperJavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class AxRTP extends ZapperJavaPlugin {
    @Getter private static AxRTP instance;
    @Getter private TaskScheduler scheduler;
    @Getter private Config language;
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
        config = new Config(new File(getDataFolder(), "config.yml"), getResource("config.yml"), GeneralSettings
                .builder()
                .setUseDefaults(false)
                .build(),

                LoaderSettings
                        .builder()
                        .setAutoUpdate(true)
                        .build(), DumperSettings.DEFAULT,

                UpdaterSettings
                        .builder()
                        .setKeepAll(true)
                        .build());

        language = new Config(new File(getDataFolder(), "messages.yml"), getResource("messages.yml"), GeneralSettings
                .builder()
                .setUseDefaults(false)
                .build(),

                LoaderSettings
                        .builder()
                        .setAutoUpdate(true)
                        .build(), DumperSettings.DEFAULT,

                UpdaterSettings
                        .builder()
                        .setKeepAll(true)
                        .build());
    }
}
