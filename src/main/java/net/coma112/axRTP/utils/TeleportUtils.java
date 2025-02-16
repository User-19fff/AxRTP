package net.coma112.axrtp.utils;

import io.papermc.lib.PaperLib;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;


import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeleportUtils {
    private static final int PLAYER_HEIGHT = 2;
    private static final double CENTER_OFFSET = 0.5;

    public static @NotNull CompletableFuture<Location> findRandomSafeLocationAsync(@NotNull World world, @NotNull Location center, @NotNull Set<Material> blacklistedBlocks) {
        return findRandomSafeLocationAsync(world, center, blacklistedBlocks, 0);
    }

    private static @NotNull CompletableFuture<Location> findRandomSafeLocationAsync(@NotNull World world, @NotNull Location center, @NotNull Set<Material> blacklistedBlocks, int attempt) {
        if (attempt >= ConfigKeys.TELEPORT_MAXIMUM_ATTEMPTS.getInt()) return CompletableFuture.completedFuture(world.getSpawnLocation());

        Random random = ThreadLocalRandom.current();
        int radius = getWorldRadius(world);
        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();

        int x = centerX + random.nextInt(-radius, radius + 1);
        int z = centerZ + random.nextInt(-radius, radius + 1);

        return PaperLib.getChunkAtAsync(world, x >> 4, z >> 4, true).thenCompose(chunk -> {
            int highestY = chunk.getWorld().getHighestBlockYAt(x, z);
            Location candidate = new Location(world, x + CENTER_OFFSET, highestY + 1, z + CENTER_OFFSET, center.getYaw(), center.getPitch());

            if (isLocationSafe(chunk, x, highestY + 1, z, blacklistedBlocks)) return CompletableFuture.completedFuture(candidate);
            else return findRandomSafeLocationAsync(world, center, blacklistedBlocks, attempt + 1);
        });
    }

    private static boolean isLocationSafe(@NotNull Chunk chunk, int x, int y, int z, @NotNull Set<Material> blacklistedBlocks) {
        Block groundBlock = chunk.getBlock(x & 15, y - 1, z & 15);
        if (isBlockUnsafe(groundBlock, blacklistedBlocks, true)) return false;

        for (int i = 0; i < PLAYER_HEIGHT; i++) {
            Block currentBlock = chunk.getBlock(x & 15, y + i, z & 15);
            if (isBlockUnsafe(currentBlock, blacklistedBlocks, false)) return false;
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

    public static @NotNull @UnmodifiableView Set<Material> parseMaterialSet(@NotNull Iterable<String> materialNames) {
        Set<Material> materials = new HashSet<>();
        for (String name : materialNames) {
            try {
                materials.add(Material.valueOf(name.toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
        return Collections.unmodifiableSet(materials);
    }
}