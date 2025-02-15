package net.coma112.axRTP.language;

import lombok.Getter;
import net.coma112.axRTP.AxRTP;
import net.coma112.axRTP.handlers.ConfigurationHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class Language  {
    private final ConfigurationHandler handler;

    public Language(@NotNull String name) {
        handler = ConfigurationHandler.of(AxRTP.getInstance().getDataFolder().getPath() + File.separator + "locales", name);
        handler.save();
    }
}
