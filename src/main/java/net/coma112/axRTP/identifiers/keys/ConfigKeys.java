package net.coma112.axrtp.identifiers.keys;

import lombok.Getter;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.config.Config;
import net.coma112.axrtp.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public enum ConfigKeys {
    ALIASES("aliases"),

    TELEPORT_MAXIMUM_ATTEMPTS("teleport.maximum-attempts"),

    TELEPORT_ON_FIRST_JOIN_ENABLED("teleport.teleport-on-first-join.enabled"),
    TELEPORT_ON_FIRST_JOIN_WORLD("teleport.teleport-on-first-join.world"),

    TELEPORT_COOLDOWN_ENABLED("teleport.cooldown.enabled"),
    TELEPORT_COOLDOWN_LOCK_AFTER("teleport.cooldown.lock-after"),
    TELEPORT_COOLDOWN_TIME("teleport.cooldown.time"),

    TELEPORT_DELAY_ENABLED("teleport.delay.enabled"),
    TELEPORT_DELAY_TIME("teleport.delay.time"),
    TELEPORT_DELAY_SOUND("teleport.delay.sound"),
    TELEPORT_DELAY_CANCEL_ON_MOVE("teleport.delay.cancel-on-move"),
    TELEPORT_DELAY_TITLE_ENABLED("teleport.delay.title.enabled"),
    TELEPORT_DELAY_TITLE_TEXT("teleport.delay.title.title"),
    TELEPORT_DELAY_TITLE_SUBTITLE("teleport.delay.title.subtitle"),

    BLACKLISTED_BLOCKS("blacklisted-blocks"),
    BLACKLISTED_WORLDS("blacklisted-worlds"),
    BLACKLISTED_BIOMES("blacklisted-biomes"),

    TITLE_ENABLED("title.enabled"),
    TITLE_TEXT("title.title"),
    TITLE_SUBTITLE("title.subtitle"),

    SOUND_ENABLED("sound.enabled"),
    SOUND_LIST("sound.list"),

    PARTICLE_ENABLED("particle.enabled"),
    PARTICLE_PATTERN("particle.pattern"),

    EFFECTS_ENABLED("effects.enabled"),
    EFFECTS_LIST("effects.list"),

    COMMAND_ON_RTP_ENABLED("commands-on-rtp.enabled"),
    COMMAND_ON_RTP_LIST("commands-on-rtp.list"),

    PRICES_ENABLED("prices.enabled"),
    PRICES_LIST("prices.list"),

    RESPECT_WORLDGUARD("respect.worldguard"),

    WORLDS("world-radius");

    private final String path;
    private static final Config config = AxRTP.getInstance().getConfiguration();

    ConfigKeys(@NotNull String path) {
        this.path = path;
    }

    public @NotNull String getString() {
        return MessageProcessor.process(config.getHandler().getString(path));
    }

    public static @NotNull String getString(@NotNull String path) {
        return config.getHandler().getString(path);
    }

    public boolean getBoolean() {
        return config.getHandler().getBoolean(path);
    }

    public int getInt() {
        return config.getHandler().getInt(path);
    }

    public List<String> getList() {
        return config.getHandler().getList(path);
    }
}
