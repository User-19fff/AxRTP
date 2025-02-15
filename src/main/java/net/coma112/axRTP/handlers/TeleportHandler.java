package net.coma112.axRTP.handlers;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import net.coma112.axRTP.AxRTP;
import net.coma112.axRTP.identifiers.keys.ConfigKeys;
import net.coma112.axRTP.identifiers.keys.MessageKeys;
import net.coma112.axRTP.utils.PlayerFeedbackUtils;
import net.coma112.axRTP.utils.TeleportUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TeleportHandler {
    private static final ConcurrentHashMap<Player, Boolean> TELEPORTING_PLAYERS = new ConcurrentHashMap<>();
    private static MyScheduledTask TELEPORT_TASK;

    public static void rtp(@NotNull Player player, @NotNull World world) {
        if (!AxRTP.getInstance().getLockAfterHandler().canUseRTP(player)) return;

        if (ConfigKeys.TELEPORT_COOLDOWN_ENABLED.getBoolean() && CooldownHandler.isOnCooldown(player)) {
            long remainingCooldown = CooldownHandler.getRemainingCooldown(player, TimeUnit.SECONDS);
            player.sendMessage(MessageKeys.COOLDOWN.getMessage().replace("{time}", String.valueOf(remainingCooldown)));
            return;
        }

        Set<Material> blacklist = TeleportUtils.parseMaterialSet(ConfigKeys.BLACKLISTED_BLOCKS.getList());
        CompletableFuture<Location> safeLocationFuture = TeleportUtils.findRandomSafeLocationAsync(world, world.getSpawnLocation(), blacklist);
        final int[] timeLeft = {ConfigKeys.TELEPORT_DELAY_TIME.getInt()};

        if (ConfigKeys.TELEPORT_DELAY_CANCEL_ON_MOVE.getBoolean()) TELEPORTING_PLAYERS.put(player, true);

        TELEPORT_TASK = AxRTP.getInstance().getScheduler().runTaskTimer(() -> {
            if (timeLeft[0] > 0) {
                PlayerFeedbackUtils.sendTitle(player,
                        ConfigKeys.TELEPORT_DELAY_ENABLED.getBoolean(),
                        ConfigKeys.TELEPORT_DELAY_TITLE_TEXT.getString().replace("{time}", String.valueOf(timeLeft[0])),
                        ConfigKeys.TELEPORT_DELAY_TITLE_SUBTITLE.getString().replace("{time}", String.valueOf(timeLeft[0]))
                );
                PlayerFeedbackUtils.playSound(player, ConfigKeys.TELEPORT_DELAY_SOUND.getString());
                timeLeft[0]--;
            } else {
                safeLocationFuture.thenAccept(safeLocation -> {
                    if (safeLocation != null) {
                        AxRTP.getInstance().getScheduler().runTask(() -> player.teleport(safeLocation));
                        PlayerFeedbackUtils.sendTitle(player,
                                ConfigKeys.TITLE_ENABLED.getBoolean(),
                                ConfigKeys.TITLE_TEXT.getString(),
                                ConfigKeys.TITLE_SUBTITLE.getString());
                        PlayerFeedbackUtils.playSounds(player);

                        if (ConfigKeys.TELEPORT_COOLDOWN_ENABLED.getBoolean()) {
                            CooldownHandler.setCooldown(player, ConfigKeys.TELEPORT_COOLDOWN_TIME.getInt(), TimeUnit.SECONDS);
                        }
                    } else player.sendMessage(MessageKeys.NO_PLACE.getMessage());
                });

                TELEPORT_TASK.cancel();
                TELEPORTING_PLAYERS.remove(player);
            }
        }, 0, 20);
    }

    public static boolean isPlayerInTeleport(@NotNull Player player) {
        return TELEPORTING_PLAYERS.containsKey(player);
    }

    public static void cancelTeleport(@NotNull Player player) {
        if (TELEPORTING_PLAYERS.remove(player) != null) {
            player.sendMessage(MessageKeys.CANT_MOVE.getMessage());
            TELEPORT_TASK.cancel();
        }
    }
}
