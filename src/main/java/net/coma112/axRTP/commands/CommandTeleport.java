package net.coma112.axrtp.commands;

import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.handlers.CooldownHandler;
import net.coma112.axrtp.handlers.TeleportHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.identifiers.keys.MessageKeys;
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
        if (!player.hasPermission("axrtp." + world.getName() + ".world")) {
            player.sendMessage(MessageKeys.NO_PERMISSION.getMessage());
            return;
        }

        TeleportHandler.rtp(player, world);
    }

    @Subcommand("reset lockdown")
    @CommandPermission("axrtp.reset.lockdown")
    public void resetLockdown(@NotNull CommandSender sender, @NotNull Player target) {
        AxRTP.getInstance().getLockdownHandler().resetPlayerUsage(target);
        sender.sendMessage(MessageKeys.SUCCESS_RESET_LOCKDOWN.getMessage());
    }

    @Subcommand("reset cooldown")
    @CommandPermission("axrtp.reset.cooldown")
    public void resetCooldown(@NotNull CommandSender sender, @NotNull Player target) {
        if (!AxRTP.getInstance().getCooldownHandler().isOnCooldown(target)) {
            sender.sendMessage(MessageKeys.NOT_ON_COOLDOWN.getMessage());
            return;
        }

        AxRTP.getInstance().getCooldownHandler().removeCooldown(target);
        sender.sendMessage(MessageKeys.SUCCESS_RESET_COOLDOWN.getMessage());
    }
}