package net.coma112.axRTP.commands;

import net.coma112.axRTP.AxRTP;
import net.coma112.axRTP.handlers.TeleportHandler;
import net.coma112.axRTP.identifiers.keys.MessageKeys;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.orphan.OrphanCommand;

public class CommandTeleport implements OrphanCommand {
    @Subcommand("reload")
    @CommandPermission("axrtp.reload")
    public void reload(@NotNull CommandSender sender) {
        AxRTP.getInstance().getLanguage().getHandler().reload();
        AxRTP.getInstance().getConfiguration().getHandler().reload();
        sender.sendMessage(MessageKeys.RELOAD.getMessage());
    }

    @Subcommand("rtp")
    @CommandPermission("axrtp.rtp")
    public void rtp(@NotNull Player player, @NotNull World world) {
        TeleportHandler.rtp(player, world);
    }
}