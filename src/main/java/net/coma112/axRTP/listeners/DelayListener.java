package net.coma112.axrtp.listeners;

import net.coma112.axrtp.handlers.utils.TeleportHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class DelayListener implements Listener {
    @EventHandler
    public void onMove(final @NotNull PlayerMoveEvent event) {
        if (!ConfigKeys.TELEPORT_DELAY_CANCEL_ON_MOVE.getBoolean()) return;

        Player player = event.getPlayer();

        if (TeleportHandler.isPlayerInTeleport(player)) TeleportHandler.cancelTeleport(player);
    }
}
