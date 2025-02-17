package net.coma112.axrtp.utils;

import lombok.experimental.UtilityClass;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.hooks.Vault;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.identifiers.keys.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@UtilityClass
@SuppressWarnings("deprecation")
public class PlayerFeedbackUtils {
    private final int POINTS = 50;
    private final int VERTICAL_STEPS = 6;
    private final double RADIUS = 1.5;
    private final List<Location> cachedOffsets = new ArrayList<>();

    private static final ConcurrentMap<Integer, Set<String>> PRICES = new ConcurrentHashMap<>();


    static {
        reloadPrices();

        for (int i = 0; i < POINTS; i++) {
            double theta = Math.toRadians(i * (360.0 / POINTS));
            for (int j = -90; j <= 90; j += (180 / VERTICAL_STEPS)) {
                double phi = Math.toRadians(j);
                double x = RADIUS * Math.cos(phi) * Math.cos(theta);
                double y = RADIUS * Math.sin(phi);
                double z = RADIUS * Math.cos(phi) * Math.sin(theta);

                cachedOffsets.add(new Location(null, x, y, z));
            }
        }
    }

    private static void reloadPrices() {
        List<String> entries = AxRTP.getInstance().getConfiguration().getHandler().getList("blacklisted-biomes");

        PRICES.clear();
        entries.stream()
                .map(entry -> entry.split(":"))
                .filter(parts -> parts.length == 2)
                .forEach(parts -> PRICES
                        .computeIfAbsent(Integer.valueOf(parts[1]), k -> ConcurrentHashMap.newKeySet())
                        .add(parts[0].toUpperCase()));
    }

    public void showParticles(@NotNull Player player) {
        if (!ConfigKeys.PARTICLE_ENABLED.getBoolean()) return;

        String particleName = ConfigKeys.PARTICLE_DISPLAY.getString();
        Particle particle = getParticleSafe(particleName);
        if (particle == null) return;

        Location baseLocation = player.getLocation().add(0, 1, 0);

        CompletableFuture.runAsync(() -> {
            cachedOffsets.forEach(offset -> {
                Location particleLoc = baseLocation.clone().add(offset.getX(), offset.getY(), offset.getZ());
                player.getWorld().spawnParticle(particle, particleLoc, 1);
            });
        });
    }

    private @Nullable Particle getParticleSafe(@NotNull String particleName) {
        try {
            return Particle.valueOf(particleName);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public void sendTitle(@NotNull Player player, boolean isEnabled, @Nullable String title, @Nullable String subtitle) {
        if (!isEnabled) return;

        player.sendTitle(title, subtitle);
    }

    public void playSounds(@NotNull Player player) {
        if (!ConfigKeys.SOUND_ENABLED.getBoolean()) return;

        ConfigKeys.SOUND_LIST.getList().forEach(sound -> player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1.0f));
    }

    public void playSound(@NotNull Player player, @Nullable String sound) {
        if (sound == null) return;

        player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1.0f);
    }

    public void applyEffects(@NotNull Player player) {
        if (!ConfigKeys.EFFECTS_ENABLED.getBoolean()) return;

        List<String> effects = ConfigKeys.EFFECTS_LIST.getList();
        for (String effect : effects) {
            String[] parts = effect.split(":");

            if (parts.length == 3) {
                String effectName = parts[0];
                int duration = Integer.parseInt(parts[1]) * 20;
                int amplifier = Integer.parseInt(parts[2]);

                try {
                    PotionEffectType effectType = PotionEffectType.getByName(effectName);
                    if (effectType != null) player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }

    public void runCommands(@NotNull Player player) {
        if (!ConfigKeys.COMMAND_ON_RTP_ENABLED.getBoolean()) return;

        List<String> commands = ConfigKeys.EFFECTS_LIST.getList();

        for (String command : commands) {
            String replacedCommand = command.replace("%player%", player.getName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacedCommand);
        }
    }

    public static boolean deductMoney(@NotNull Player player, @NotNull World world) {
        if (!ConfigKeys.PRICES_ENABLED.getBoolean()) {
            return true;
        }

        String worldName = world.getName();
        List<String> priceEntries = ConfigKeys.PRICES_LIST.getList();

        for (String entry : priceEntries) {
            String[] parts = entry.split(":");

            if (parts.length == 2 && parts[1].equalsIgnoreCase(worldName)) {
                int price = Integer.parseInt(parts[0]);

                    if (Vault.hasEnoughMoney(player, price)) {
                        Vault.deductMoney(player, price);
                        return true;
                    } else {
                        player.sendMessage(MessageKeys.NOT_ENOUGH_MONEY.getMessage());
                        return false;
                    }
                }
            }

        return true;
    }
}
