package net.coma112.axrtp.hooks;

import lombok.Getter;
import net.coma112.axrtp.AxRTP;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class Vault {
    @Getter
    private static Economy economy = null;

    private Vault() {}

    private static void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = AxRTP.getInstance().getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp != null) economy = rsp.getProvider();
    }

    static {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) setupEconomy();
    }

    public static boolean hasEnoughMoney(@NotNull Player player, double amount) {
        if (economy == null) return true;
        return economy.getBalance(player) >= amount;
    }

    public static void deductMoney(@NotNull Player player, double amount) {
        economy.withdrawPlayer(player, amount);
    }
}
