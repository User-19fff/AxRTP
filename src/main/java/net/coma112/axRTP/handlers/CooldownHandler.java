package net.coma112.axrtp.handlers;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CooldownHandler {
    private static final ConcurrentHashMap<Player, Long> COOLDOWNS = new ConcurrentHashMap<>();

    public void setCooldown(@NotNull Player player, long cooldownTime, @NotNull TimeUnit timeUnit) {
        long cooldownEndTime = System.currentTimeMillis() + timeUnit.toMillis(cooldownTime);
        COOLDOWNS.put(player, cooldownEndTime);
    }

    public boolean isOnCooldown(@NotNull Player player) {
        Long cooldownEndTime = COOLDOWNS.get(player);
        if (cooldownEndTime == null) return false;
        return System.currentTimeMillis() < cooldownEndTime;
    }

    public long getRemainingCooldown(@NotNull Player player, @NotNull TimeUnit timeUnit) {
        Long cooldownEndTime = COOLDOWNS.get(player);
        if (cooldownEndTime == null) return 0;
        long remainingTime = cooldownEndTime - System.currentTimeMillis();
        return timeUnit.convert(remainingTime, TimeUnit.MILLISECONDS);
    }

    public void removeCooldown(@NotNull Player player) {
        COOLDOWNS.remove(player);
    }
}
