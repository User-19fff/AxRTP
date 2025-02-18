package net.coma112.axrtp.language;

import lombok.Getter;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.handlers.config.ConfigurationHandler;

@Getter
public class Language  {
    private final ConfigurationHandler handler;

    public Language() {
        handler = ConfigurationHandler.of(AxRTP.getInstance().getDataFolder().getPath(), "messages");
        handler.save();
    }
}
