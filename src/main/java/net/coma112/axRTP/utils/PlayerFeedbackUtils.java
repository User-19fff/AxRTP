package net.coma112.axRTP.utils;

import lombok.experimental.UtilityClass;
import net.coma112.axRTP.identifiers.keys.ConfigKeys;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
@SuppressWarnings("deprecation")
public class PlayerFeedbackUtils {
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
}
