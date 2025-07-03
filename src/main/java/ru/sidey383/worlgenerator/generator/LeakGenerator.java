package ru.sidey383.worlgenerator.generator;

import org.bukkit.util.Vector;
import org.bukkit.util.noise.NoiseGenerator;

import java.util.Random;

public class LeakGenerator {

    private final IslandShapeGenerator shapeGenerator;
    private final int x;
    private final int z;

    public LeakGenerator(int x, int z, double placeRadius, double leakRadius, Random random) {
        double radian = random.nextDouble(placeRadius);
        double angle = random.nextDouble(0, Math.PI * 2);
        var place = new Vector(x + radian * Math.sin(angle), 100, z + radian * Math.cos(angle));
        this.x = place.getBlockX();
        this.z = place.getBlockZ();
        this.shapeGenerator = new SimpleIslandShapeGenerator(leakRadius, 10, place, 1);
    }

    public Vector getLeakPlace() {
        return new Vector(x, 100, z);
    }

    public Integer leakDeep(int x, int z, NoiseGenerator noiseGenerator) {
        Integer raw = shapeGenerator.bodyBottom(x, z, noiseGenerator);
        if (raw == null) return null;
        return 100 - raw;
    }

    public void nearestNoLeakBlock(int x, int z, NoiseGenerator noiseGenerator, Vector v) {
        Double distance = null;
        for (int x_ = -5; x_ <= 5; x_ ++) {
            for (int z_ = -5; z_ <= 5; z_ ++) {
                double distanceSquare = x_ * x_ + z_ * z_;
                if (distance != null && distance < distanceSquare) {
                    continue;
                }
                if (leakDeep(x + x_, z + z_, noiseGenerator) == null) {
                    v.setX(x + x_);
                    v.setZ(z + z_);
                    v.setY(0);
                    distance = distanceSquare;
                }
            }
        }
    }

    public void interpolateValue(int x, int z, NoiseGenerator noiseGenerator, DeepInterpolate interpolate) {
        interpolate.distance = null;
        interpolate.height = 0;
        for (int x_ = -5; x_ <= 5; x_ ++) {
            for (int z_ = -5; z_ <= 5; z_ ++) {
                double distanceSquare = x_ * x_ + z_ * z_;
                if (interpolate.distance != null && interpolate.distance < distanceSquare) {
                    continue;
                }
                Integer raw = leakDeep(x + x_, z + z_, noiseGenerator);
                if (raw != null) {
                    interpolate.distance = distanceSquare;
                    interpolate.height = raw;
                }
            }
        }
        if (interpolate.hasInterpolate()) {
            interpolate.distance = Math.sqrt(interpolate.distance);
        }
    }

    public static class DeepInterpolate {
        private Integer height = null;
        private Double distance = null;

        public DeepInterpolate() {}

        public boolean hasInterpolate() {
            return height != null && distance != null;
        }

        public Integer height() {
            return height;
        }

        public Double distance() {
            return distance;
        }

    }


}
