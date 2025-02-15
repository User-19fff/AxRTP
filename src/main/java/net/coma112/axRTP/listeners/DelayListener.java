package net.coma112.axRTP.listeners;

import net.coma112.axRTP.handlers.TeleportHandler;
import net.coma112.axRTP.identifiers.keys.ConfigKeys;
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
