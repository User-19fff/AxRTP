package net.coma112.axRTP.utils;

import lombok.experimental.UtilityClass;
import net.coma112.axRTP.AxRTP;
import net.coma112.axRTP.commands.CommandTeleport;
import net.coma112.axRTP.handlers.CommandExceptionHandler;
import net.coma112.axRTP.identifiers.keys.ConfigKeys;
import net.coma112.axRTP.listeners.DelayListener;
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
