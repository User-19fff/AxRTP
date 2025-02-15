package net.coma112.axrtp.config;

import lombok.Getter;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.handlers.ConfigurationHandler;

@Getter
public class Config {
    private final ConfigurationHandler handler;

    public Config() {
        handler = ConfigurationHandler.of(AxRTP.getInstance().getDataFolder().getPath(), "config");
        handler.save();
    }
}
