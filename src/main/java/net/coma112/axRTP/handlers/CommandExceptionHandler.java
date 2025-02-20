package net.coma112.axrtp.handlers;

import net.coma112.axrtp.identifiers.keys.MessageKeys;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.locales.LocaleReader;

import java.util.Locale;

@SuppressWarnings("deprecation")
public class CommandExceptionHandler implements LocaleReader {
    @Override
    public boolean containsKey(@NotNull String string) {
        return true;
    }

    @Override
    public String get(@NotNull String string) {
        switch (string) {
            case "missing-argument" -> MessageKeys.MISSING_ARGUMENT.getMessage();
            case "no-permission" -> MessageKeys.NO_PERMISSION.getMessage();
            case "must-be-player" -> MessageKeys.PLAYER_REQUIRED.getMessage();
        }

        return "";
    }

    private final Locale LOCALE = new Locale("en", "US");

    @Override
    public Locale getLocale() {
        return LOCALE;
    }
}
