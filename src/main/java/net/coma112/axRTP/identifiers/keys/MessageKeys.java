package net.coma112.axrtp.identifiers.keys;

import lombok.Getter;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public enum MessageKeys {
    RELOAD("messages.reload"),

    NO_PERMISSION("messages.no-permission"),
    NO_PLACE("messages.no-place"),

    LOCKDOWN("messages.lockdown"),
    COOLDOWN("messages.cooldown"),

    BLACKLISTED_WORLD("messages.blacklisted-world"),

    CANT_MOVE("messages.cant-move");

    private final String path;

    MessageKeys(@NotNull final String path) {
        this.path = path;
    }

    public @NotNull String getMessage() {
        return MessageProcessor.process(AxRTP.getInstance().getLanguage().getHandler().getString(path))
                .replace("%prefix%", MessageProcessor.process(AxRTP.getInstance().getLanguage().getHandler().getString("prefix")));
    }

    public List<String> getMessages() {
        return AxRTP.getInstance().getLanguage().getHandler().getList(path)
                .stream()
                .toList();
    }
}
