package net.coma112.axrtp.utils;

import lombok.experimental.UtilityClass;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.commands.CommandTeleport;
import net.coma112.axrtp.handlers.CommandExceptionHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.listeners.DelayListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.orphan.Orphans;

@UtilityClass
public class RegisterUtils {
    public void registerListeners() {
        LoggerUtils.info("### Registering listeners... ###");

        Bukkit.getPluginManager().registerEvents(new DelayListener(), AxRTP.getInstance());

        LoggerUtils.info("### Successfully registered 2 listener. ###");
    }

    public static void registerCommands() {
        LoggerUtils.info("### Registering commands... ###");

        var lamp = BukkitLamp.builder(AxRTP.getInstance())
                .exceptionHandler(new CommandExceptionHandler())
                .suggestionProviders(providers -> {
                    providers.addProvider(String.class, context -> Bukkit.getWorlds().stream()
                            .map(World::getName)
                            .toList());
                })
                .build();

        lamp.register(Orphans.path(ConfigKeys.ALIASES.getList().toArray(String[]::new)).handler(new CommandTeleport()));
        LoggerUtils.info("### Successfully registered exception handlers... ###");
    }

}
