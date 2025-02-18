package net.coma112.axrtp.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@UtilityClass
public class LocationUtils {
    private final String DEFAULT_SERIALIZED_LOCATION = Bukkit.getWorlds().getFirst().getName() + ";0;0;0;0.0;0.0";
    private final Location DEFAULT_DESERIALIZED_LOCATION = new Location(Bukkit.getWorlds().getFirst(), 0.0, 0.0, 0.0, 0.0F, 0.0F);

    public String convertLocationToString(@Nullable Location location) {
        return location == null ? DEFAULT_SERIALIZED_LOCATION : Objects.requireNonNull(location.getWorld()).getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
    }

    public Location convertStringToLocation(@Nullable String string) {
        if (string == null) return null;
        String[] split = string.split(";");

        return split.length != 6 ? DEFAULT_DESERIALIZED_LOCATION : new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
