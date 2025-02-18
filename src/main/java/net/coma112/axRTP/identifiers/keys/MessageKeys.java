package net.coma112.axrtp.identifiers.keys;

import lombok.Getter;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public enum MessageKeys {
    RELOAD("messages.reload"),
    HELP("messages.help"),

    MISSING_ARGUMENT("messages.missing-argument"),
    PLAYER_REQUIRED("messages.player-required"),

    SUCCESS_RESET_LOCKDOWN("messages.success-reset-lockdown"),
    SUCCESS_RESET_COOLDOWN("messages.success-reset-cooldown"),
    SUCCESS_SET_CENTER("messages.success-center-set"),

    NO_PERMISSION("messages.no-permission"),
    NO_PLACE("messages.no-place"),
    NOT_ON_COOLDOWN("messages.not-on-cooldown"),
    NOT_ENOUGH_MONEY("messages.not-enough-money"),

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
