package net.coma112.axrtp.handlers;

import net.coma112.axrtp.identifiers.keys.MessageKeys;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.BukkitExceptionHandler;
import revxrsal.commands.exception.NoPermissionException;

public class CommandExceptionHandler extends BukkitExceptionHandler {
    @Override
    public void onNoPermission(@NotNull NoPermissionException exception, @NotNull BukkitCommandActor actor) {
        actor.error(MessageKeys.NO_PERMISSION.getMessage());
    }
}
