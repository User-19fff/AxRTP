package net.coma112.axrtp.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.coma112.axrtp.AxRTP;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class PlaceholderAPI {
    public static boolean isRegistered = false;

    public static void registerHook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderIntegration().register();
            isRegistered = true;
        }
    }

    private static class PlaceholderIntegration extends PlaceholderExpansion {
        @Override
        public @NotNull String getIdentifier() {
            return "axrtp";
        }

        @Override
        public @NotNull String getAuthor() {
            return "coma";
        }

        @Override
        public @NotNull String getVersion() {
            return AxRTP.getInstance().getDescription().getVersion();
        }

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public boolean persist() {
            return true;
        }
    }
}
