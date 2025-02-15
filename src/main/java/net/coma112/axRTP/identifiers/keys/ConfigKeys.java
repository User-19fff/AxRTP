package net.coma112.axrtp.identifiers.keys;

import lombok.Getter;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public enum ConfigKeys {
    LANGUAGE("language"),
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

    TITLE_ENABLED("title.enabled"),
    TITLE_TEXT("title.title"),
    TITLE_SUBTITLE("title.subtitle"),

    SOUND_ENABLED("sound.enabled"),
    SOUND_LIST("sound.list"),

    WORLDS("world-radius");

    private final String path;

    ConfigKeys(@NotNull final String path) {
        this.path = path;
    }

    public @NotNull String getString() {
        return MessageProcessor.process(AxRTP.getInstance().getConfiguration().getHandler().getString(path));
    }

    public boolean getBoolean() {
        return AxRTP.getInstance().getConfiguration().getHandler().getBoolean(path);
    }

    public int getInt() {
        return AxRTP.getInstance().getConfiguration().getHandler().getInt(path);
    }

    public List<String> getList() {
        return AxRTP.getInstance().getConfiguration().getHandler().getList(path);
    }
}
