package net.coma112.axrtp.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class WorldGuard {
    public static boolean isInWorldGuardRegion(@NotNull Location location) {
        WorldGuardPlugin wgPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (wgPlugin == null) return false;

        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testState(BukkitAdapter.adapt(location), null);
    }
}
