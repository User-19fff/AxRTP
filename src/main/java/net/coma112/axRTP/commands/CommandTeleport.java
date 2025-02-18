package net.coma112.axrtp.commands;

import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.handlers.utils.TeleportHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.identifiers.keys.MessageKeys;
import net.coma112.axrtp.utils.LocationUtils;
import net.coma112.axrtp.utils.PlayerFeedbackUtils;
import net.coma112.axrtp.utils.TeleportUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.orphan.OrphanCommand;

public class CommandTeleport implements OrphanCommand {
    @CommandPlaceholder
    public void defaultCommand(@NotNull Player player, @Optional @Nullable World world) {
        rtp(player, world);
    }

    @Subcommand("reload")
    @CommandPermission("axrtp.reload")
    @Usage("/alias reload")
    public void reload(@NotNull CommandSender sender) {
        AxRTP.getInstance().getLanguage().getHandler().reload();
        AxRTP.getInstance().getConfiguration().getHandler().reload();
        TeleportUtils.reloadBlacklistedBiomes();
        PlayerFeedbackUtils.reloadPrices();
        sender.sendMessage(MessageKeys.RELOAD.getMessage());
    }

    @Subcommand("rtp")
    @CommandPermission("axrtp.rtp")
    @CommandPlaceholder
    @Usage("/alias rtp <world>")
    public void rtp(@NotNull Player player, @Optional @Nullable World world) {
        if (world == null) world = player.getWorld();

        if (ConfigKeys.BLACKLISTED_WORLDS.getList().contains(world.getName())) {
            player.sendMessage(MessageKeys.BLACKLISTED_WORLD.getMessage());
            return;
        }

        if (!player.hasPermission("axrtp." + world.getName() + ".world")) {
            player.sendMessage(MessageKeys.NO_PERMISSION.getMessage());
            return;
        }

        TeleportHandler.rtp(player, world);
    }

    @Subcommand("reset lockdown")
    @CommandPermission("axrtp.reset.lockdown")
    @Usage("/alias reset lockdown (target)")
    public void resetLockdown(@NotNull CommandSender sender, @NotNull Player target) {
        AxRTP.getInstance().getLockdownHandler().resetPlayerUsage(target);
        sender.sendMessage(MessageKeys.SUCCESS_RESET_LOCKDOWN.getMessage());
    }

    @Subcommand("reset cooldown")
    @CommandPermission("axrtp.reset.cooldown")
    @Usage("/alias reset cooldown (target)")
    public void resetCooldown(@NotNull CommandSender sender, @NotNull Player target) {
        if (!AxRTP.getInstance().getCooldownHandler().isOnCooldown(target)) {
            sender.sendMessage(MessageKeys.NOT_ON_COOLDOWN.getMessage());
            return;
        }

        AxRTP.getInstance().getCooldownHandler().removeCooldown(target);
        sender.sendMessage(MessageKeys.SUCCESS_RESET_COOLDOWN.getMessage());
    }

    @Subcommand("set center")
    @CommandPermission("axrtp.set.center")
    public void setCenter(@NotNull Player player) {
        AxRTP.getInstance().getConfiguration().getHandler().set("centers." + player.getWorld().getName(), LocationUtils.convertLocationToString(player.getLocation()));
        AxRTP.getInstance().getConfiguration().getHandler().save();
        player.sendMessage(MessageKeys.SUCCESS_SET_CENTER.getMessage());
    }

    @Subcommand("help")
    public void help(@NotNull CommandSender sender) {
        MessageKeys.HELP.getMessages().forEach(sender::sendMessage);
    }
}