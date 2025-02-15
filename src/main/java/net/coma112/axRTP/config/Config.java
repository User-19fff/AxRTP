package net.coma112.axRTP.config;

import lombok.Getter;
import net.coma112.axRTP.AxRTP;
import net.coma112.axRTP.handlers.ConfigurationHandler;

@Getter
public class Config {
    private final ConfigurationHandler handler;

    public Config() {
        handler = ConfigurationHandler.of(AxRTP.getInstance().getDataFolder().getPath(), "config");
        handler.save();
    }
}
