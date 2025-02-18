package net.coma112.axrtp.handlers;

import net.coma112.axrtp.identifiers.keys.MessageKeys;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.BukkitExceptionHandler;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;
import revxrsal.commands.exception.MissingArgumentException;
import revxrsal.commands.exception.NoPermissionException;
import revxrsal.commands.node.ParameterNode;

public class CommandExceptionHandler extends BukkitExceptionHandler {
    @Override
    public void onNoPermission(@NotNull NoPermissionException exception, @NotNull BukkitCommandActor actor) {
        actor.error(MessageKeys.NO_PERMISSION.getMessage());
    }

    @Override
    public void onMissingArgument(@NotNull MissingArgumentException exception, @NotNull BukkitCommandActor actor, @NotNull ParameterNode<BukkitCommandActor, ?> parameter) {
        actor.error(MessageKeys.MISSING_ARGUMENT.getMessage().replace("{usage}", parameter.command().usage()));
    }

    @Override
    public void onSenderNotPlayer(SenderNotPlayerException exception, @NotNull BukkitCommandActor actor) {
        actor.error(MessageKeys.PLAYER_REQUIRED.getMessage());
    }
}
