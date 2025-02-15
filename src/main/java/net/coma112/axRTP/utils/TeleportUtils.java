package net.coma112.axrtp.utils;

import io.papermc.lib.PaperLib;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeleportUtils {
    private static final int PLAYER_HEIGHT = 2;
    private static final double CENTER_OFFSET = 0.5;

    public static @NotNull CompletableFuture<Location> findRandomSafeLocationAsync(@NotNull World world, @NotNull Location center, @NotNull Set<Material> blacklistedBlocks) {
        return CompletableFuture.supplyAsync(() -> {
            Random random = ThreadLocalRandom.current();
            int radius = getWorldRadius(world);
            int centerX = center.getBlockX();
            int centerZ = center.getBlockZ();

            for (int i = 0; i < ConfigKeys.TELEPORT_MAXIMUM_ATTEMPTS.getInt(); i++) {
                int x = centerX + random.nextInt(-radius, radius + 1);
                int z = centerZ + random.nextInt(-radius, radius + 1);

                CompletableFuture<Location> future = PaperLib.getChunkAtAsync(world, x >> 4, z >> 4).thenApply(chunk -> {
                    int y = world.getHighestBlockYAt(x, z);
                    Location candidate = new Location(world, x + CENTER_OFFSET, y + 1, z + CENTER_OFFSET, center.getYaw(), center.getPitch());

                    if (isLocationSafe(candidate, blacklistedBlocks)) return candidate;
                    return null;
                });

                try {
                    Location location = future.get();
                    if (location != null) return location;
                } catch (Exception exception) {
                    LoggerUtils.error(exception.getMessage());
                }
            }

            return world.getSpawnLocation();
        });
    }

    private static int getWorldRadius(@NotNull World world) {
        return AxRTP.getInstance().getConfiguration().getHandler().getInt("world-radius." + world.getName());
    }

    private static boolean isLocationSafe(@NotNull Location location, @NotNull Set<Material> blacklistedBlocks) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int baseY = location.getBlockY();

        for (int i = 0; i <= PLAYER_HEIGHT; i++) {
            Block currentBlock = world.getBlockAt(x, baseY + i, location.getBlockZ());
            if (isBlockUnsafe(currentBlock, blacklistedBlocks, i == 0)) return false;
        }

        return true;
    }

    private static boolean isBlockUnsafe(@NotNull Block block, @NotNull Set<Material> blacklistedBlocks, boolean isGround) {
        Material material = block.getType();
        return blacklistedBlocks.contains(material) || (isGround != material.isSolid());
    }

    public static @NotNull @UnmodifiableView Set<Material> parseMaterialSet(@NotNull Iterable<String> materialNames) {
        return Collections.unmodifiableSet(materialNames instanceof Set<String> names ? convertNamesToMaterials(names) : convertIterableToMaterials(materialNames)
        );
    }

    private static @NotNull @UnmodifiableView Set<Material> convertIterableToMaterials(@NotNull Iterable<String> materialNames) {
        Set<Material> materials = new HashSet<>();
        materialNames.forEach(name -> materials.add(Material.valueOf(name.toUpperCase())));
        return Collections.unmodifiableSet(materials);
    }

    private static Set<Material> convertNamesToMaterials(@NotNull Set<String> names) {
        return names.stream()
                .map(name -> Material.valueOf(name.toUpperCase()))
                .collect(Collectors.toUnmodifiableSet());
    }
}