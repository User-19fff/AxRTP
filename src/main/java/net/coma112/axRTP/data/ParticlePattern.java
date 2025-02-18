package net.coma112.axrtp.data;

import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public record ParticlePattern(@NotNull String formulaX,
                              @NotNull String formulaY,
                              @NotNull String formulaZ,
                              int points,
                              double radius,
                              @NotNull Particle particle) {}
