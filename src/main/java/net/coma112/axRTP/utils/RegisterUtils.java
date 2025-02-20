package net.coma112.axrtp.utils;

import lombok.experimental.UtilityClass;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.commands.CommandTeleport;
import net.coma112.axrtp.handlers.CommandExceptionHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.listeners.DelayListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.orphan.Orphans;

import java.util.Locale;

@UtilityClass
@SuppressWarnings("deprecation")
public class RegisterUtils {
    public void registerListeners() {
        LoggerUtils.info("### Registering listeners... ###");

        Bukkit.getPluginManager().registerEvents(new DelayListener(), AxRTP.getInstance());

        LoggerUtils.info("### Successfully registered 1 listener. ###");
    }

    public static void registerCommands() {
        LoggerUtils.info("### Registering commands... ###");

        BukkitCommandHandler handler = BukkitCommandHandler.create(AxRTP.getInstance());

        handler.getAutoCompleter().registerParameterSuggestions(World.class, (args, sender, command) -> Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .toList());

        handler.getTranslator().add(new CommandExceptionHandler());
        handler.setLocale(new Locale("en", "US"));
        handler.register(Orphans.path(ConfigKeys.ALIASES.getList().toArray(String[]::new)).handler(new CommandTeleport()));
        handler.registerBrigadier();

        LoggerUtils.info("### Successfully registered exception handlers... ###");
    }

}
