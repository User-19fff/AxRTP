package net.coma112.axrtp.listeners;

import net.coma112.axrtp.handlers.TeleportHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class TeleportListener implements Listener {
    @EventHandler
    public void onJoin(final @NotNull PlayerJoinEvent event) {
        if (!ConfigKeys.TELEPORT_ON_FIRST_JOIN_ENABLED.getBoolean()) return;

        Player player = event.getPlayer();

        if (player.hasPlayedBefore()) return;

        World world = Bukkit.getWorld(ConfigKeys.TELEPORT_ON_FIRST_JOIN_WORLD.getString());

        if (world == null) return;

        TeleportHandler.rtp(player, world);
    }
}
