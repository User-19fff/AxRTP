package net.coma112.axRTP.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.coma112.axRTP.AxRTP;
import net.coma112.axRTP.identifiers.keys.ConfigKeys;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Random;
import java.util.Set;
import java.util.Objects;
import java.util.HashSet;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeleportUtils {
    private static final int PLAYER_HEIGHT = 2;
    private static final double CENTER_OFFSET = 0.5;

    public static @NotNull CompletableFuture<Location> findRandomSafeLocationAsync(@NotNull World world, @NotNull Location center, @NotNull Set<Material> blacklistedBlocks) {
        return CompletableFuture.supplyAsync(() -> findRandomSafeLocation(world, center, getWorldRadius(world), blacklistedBlocks));
    }

    public static @NotNull Location findRandomSafeLocation(@NotNull World world, @NotNull Location center, int radius, @NotNull Set<Material> blacklistedBlocks) {
        validateParameters(world, center, blacklistedBlocks);

        final Random random = ThreadLocalRandom.current();
        final int centerX = center.getBlockX();
        final int centerZ = center.getBlockZ();

        for (int i = 0; i < ConfigKeys.TELEPORT_MAXIMUM_ATTEMPTS.getInt(); i++) {
            int x = centerX + random.nextInt(-radius, radius + 1);
            int z = centerZ + random.nextInt(-radius, radius + 1);
            int y = world.getHighestBlockYAt(x, z);

            Location candidate = new Location(world, x, y, z);

            if (isLocationSafe(candidate, blacklistedBlocks)) return finalizeLocation(candidate, center);
        }

        return world.getSpawnLocation();
    }

    private static int getWorldRadius(@NotNull World world) {
        return AxRTP.getInstance().getConfiguration().getHandler().getInt("world-radius." + world.getName());
    }

    private static void validateParameters(@NotNull World world, @NotNull Location center, @NotNull Set<Material> blacklistedBlocks) {
        Objects.requireNonNull(world, "World cannot be null");
        Objects.requireNonNull(center, "Center location cannot be null");
        Objects.requireNonNull(blacklistedBlocks, "Blacklisted blocks set cannot be null");

        if (center.getWorld() == null || !center.getWorld().equals(world)) throw new IllegalArgumentException("Center location must be in the specified world");
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

    private static @NotNull Location finalizeLocation(@NotNull Location location, @NotNull Location original) {
        return new Location(
                location.getWorld(),
                location.getX() + CENTER_OFFSET,
                location.getY() + 1,
                location.getZ() + CENTER_OFFSET,
                original.getYaw(),
                original.getPitch()
        );
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
                .parallel()
                .map(name -> Material.valueOf(name.toUpperCase()))
                .collect(Collectors.toUnmodifiableSet());
    }
}