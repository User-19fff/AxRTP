package net.coma112.axrtp.processor;

import net.coma112.axrtp.AxRTP;
import net.coma112.axrtp.data.ParticlePattern;
import net.coma112.axrtp.handlers.config.ConfigurationHandler;
import net.coma112.axrtp.identifiers.keys.ConfigKeys;
import net.coma112.axrtp.utils.LoggerUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParticleProcessor {

    private static final AxRTP PLUGIN = AxRTP.getInstance();

    public static void showParticles(@NotNull Player player) {
        if (!isParticleEnabled()) return;

        ConfigurationHandler config = PLUGIN.getConfiguration().getHandler();
        String patternName = ConfigKeys.PARTICLE_PATTERN.getString();
        ParticlePattern pattern = getParticlePattern(config, patternName);

        if (pattern == null) {
            LoggerUtils.warn("Invalid or missing particle pattern: " + patternName);
            return;
        }

        spawnParticles(player, pattern);
    }

    private static boolean isParticleEnabled() {
        return ConfigKeys.PARTICLE_ENABLED.getBoolean();
    }

    private static @Nullable ParticlePattern getParticlePattern(@NotNull ConfigurationHandler config, String patternName) {
        String formulaX = config.getString("particle.patterns." + patternName + ".formula-x");
        String formulaY = config.getString("particle.patterns." + patternName + ".formula-y");
        String formulaZ = config.getString("particle.patterns." + patternName + ".formula-z");
        int points = config.getInt("particle.patterns." + patternName + ".points");
        double radius = config.getDouble("particle.patterns." + patternName + ".radius");
        String particleName = ConfigKeys.getString("particle.patterns." + patternName + ".particle");

        if (formulaX == null || formulaY == null || formulaZ == null) return null;

        Particle particle = Particle.valueOf(particleName);

        return new ParticlePattern(formulaX, formulaY, formulaZ, points, radius, particle);
    }

    private static void spawnParticles(@NotNull Player player, @NotNull ParticlePattern pattern) {
        Location baseLocation = player.getLocation().add(0, 1, 0);

        for (int i = 0; i < pattern.points(); i++) {
            double T = i * (Math.PI * 2 / pattern.points());
            double x = evaluateFormula(pattern.formulaX(), T, pattern.radius());
            double y = evaluateFormula(pattern.formulaY(), T, pattern.radius());
            double z = evaluateFormula(pattern.formulaZ(), T, pattern.radius());

            Location particleLoc = baseLocation.clone().add(x, y, z);
            player.getWorld().spawnParticle(pattern.particle(), particleLoc, 1);
        }
    }

    private static double evaluateFormula(String formula, double t, double radius) {
        try {
            // rad & t
            formula = formula.replace("RADIUS", String.valueOf(radius));
            formula = formula.replace("T", String.valueOf(t));

            // different results with different x formulas

            if (formula.contains("Math.cos")) {
                String angleStr = formula.replace("Math.cos(", "").replace(")", ""); // get the angle value
                double angle = Double.parseDouble(angleStr); // numerical representation of the value of an angle
                return radius * Math.cos(angle); // the product of the radius and the cosine, which gives the x-coordinate at a given point on the circle
            } else if (formula.contains("Math.sin")) {
                String angleStr = formula.replace("Math.sin(", "").replace(")", "");
                double angle = Double.parseDouble(angleStr);
                return radius * Math.sin(angle);
            } else if (formula.contains("*")) {
                String[] parts = formula.split("\\*");
                double a = Double.parseDouble(parts[0].trim());
                double b = Double.parseDouble(parts[1].trim());
                return a * b;
            } else return Double.parseDouble(formula);
        } catch (Exception ignored) {
            return 0;
        }
    }
}
