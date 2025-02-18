package net.coma112.axrtp.utils;

import io.papermc.lib.PaperLib;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.hooks.WorldGuard;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeleportUtils {
    private static final ConcurrentMap<String, Set<String>> BLACKLISTED_BIOMES = new ConcurrentHashMap<>();
    private static final int PLAYER_HEIGHT = 2;
    private static final double CENTER_OFFSET = 0.5;
    //private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static {
        reloadBlacklistedBiomes();
    }

    public static void reloadBlacklistedBiomes() {
        List<String> entries = ConfigKeys.BLACKLISTED_BIOMES.getList();

        BLACKLISTED_BIOMES.clear();
        entries.stream()
                .map(entry -> entry.split(":"))
                .filter(parts -> parts.length == 2)
                .forEach(parts -> BLACKLISTED_BIOMES
                        .computeIfAbsent(parts[1], k -> ConcurrentHashMap.newKeySet())
                        .add(parts[0].toUpperCase()));
    }

    public static CompletableFuture<Location> findRandomSafeLocationAsync(@NotNull World world, @NotNull Location center, Set<Material> blacklistedBlocks) {
        return findRandomSafeLocationAsync(world, center, blacklistedBlocks, 0);
    }

    public static @NotNull @UnmodifiableView Set<Material> parseMaterialSet(@NotNull Iterable<String> materialNames) {
        Set<Material> materials = new HashSet<>();
        for (String name : materialNames) {
            try {
                materials.add(Material.valueOf(name.toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
        return Collections.unmodifiableSet(materials);
    }

    private static CompletableFuture<Location> findRandomSafeLocationAsync(@NotNull World world, @NotNull Location center, @Nullable Set<Material> blacklistedBlocks, int attempt) {
        if (attempt >= ConfigKeys.TELEPORT_MAXIMUM_ATTEMPTS.getInt()) return CompletableFuture.completedFuture(world.getSpawnLocation());

        Random random = ThreadLocalRandom.current();
        int radius = getWorldRadius(world);
        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();

        int x = centerX + random.nextInt(-radius, radius + 1);
        int z = centerZ + random.nextInt(-radius, radius + 1);

        return PaperLib.getChunkAtAsync(world, x >> 4, z >> 4, true).thenCompose(chunk -> {
            int highestY = chunk.getWorld().getHighestBlockYAt(x, z);
            Location candidate = new Location(world, x + CENTER_OFFSET, highestY + 1, z + CENTER_OFFSET);

            if (isLocationSafe(candidate, blacklistedBlocks)) return CompletableFuture.completedFuture(candidate);
            else return findRandomSafeLocationAsync(world, center, blacklistedBlocks, attempt + 1);
        });
    }

    private static boolean isLocationSafe(@NotNull Location location, Set<Material> blacklistedBlocks) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block groundBlock = world.getBlockAt(x, y - 1, z);
        if (isBlockUnsafe(groundBlock, blacklistedBlocks, true)) return false;

        String worldName = world.getName();
        String biomeName = world.getBiome(x, y, z).name();

        if (BLACKLISTED_BIOMES.getOrDefault(worldName, Collections.emptySet()).contains(biomeName.toUpperCase())) return false;
        if (ConfigKeys.RESPECT_WORLDGUARD.getBoolean() && WorldGuard.isInWorldGuardRegion(location)) return false;

        for (int i = 0; i < PLAYER_HEIGHT; i++) {
            Block block = world.getBlockAt(x, y + i, z);
            if (isBlockUnsafe(block, blacklistedBlocks, false)) return false;
        }

        return true;
    }

    private static int getWorldRadius(@NotNull World world) {
        return AxRTP.getInstance().getConfiguration().getHandler().getInt("world-radius." + world.getName());
    }

    private static boolean isBlockUnsafe(@NotNull Block block, @NotNull Set<Material> blacklistedBlocks, boolean isGround) {
        Material material = block.getType();
        return blacklistedBlocks.contains(material) || (isGround != material.isSolid());
    }
}