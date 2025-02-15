package net.coma112.axrtp.utils;

import lombok.experimental.UtilityClass;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@UtilityClass
@SuppressWarnings("deprecation")
public class PlayerFeedbackUtils {
    private final int POINTS = 50;
    private final int VERTICAL_STEPS = 6;
    private final double RADIUS = 1.5;
    private final List<Location> cachedOffsets = new ArrayList<>();

    static {
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

    private @Nullable Particle getParticleSafe(String particleName) {
        try {
            return Particle.valueOf(particleName);
        } catch (IllegalArgumentException e) {
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
}
