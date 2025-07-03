package ru.sidey383.worlgenerator.generator;

import org.bukkit.util.Vector;
import org.bukkit.util.noise.NoiseGenerator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SimpleIslandShapeGenerator implements IslandShapeGenerator {

    private final double horizontalRadius;
    private final double verticalRadius;
    private final Vector center;
    private final double formDistortion;


    public SimpleIslandShapeGenerator(double horizontalRadius, double verticalRadius, Vector center, double formDistortion) {
        this.horizontalRadius = horizontalRadius;
        this.verticalRadius = verticalRadius;
        this.center = center;
        this.formDistortion = Math.min(1, Math.abs(formDistortion));
    }

    public boolean hasIslandInChunk(int chunkX, int chunkZ) {
        double maxRadiusSquare = horizontalRadius * (1 + formDistortion) * horizontalRadius * (1 + formDistortion);

        double chunkCenterX = (chunkX << 4) + 8 - center.getX();
        double chunkCenterZ = (chunkZ << 4) + 8 - center.getZ();
        double closestX = Math.max(Math.abs(chunkCenterX) - 8, 0);
        double closestZ = Math.max(Math.abs(chunkCenterZ) - 8, 0);
        double closestDistSquare = closestX*closestX + closestZ*closestZ;

        return closestDistSquare < maxRadiusSquare;
    }

    @Nullable
    public Integer bodyBottom(int x, int z, NoiseGenerator noiseGenerator) {
        double xd = x - center.getX();
        double zd = z - center.getZ();
        double radius = horizontalRadius * (1 + calculateAngleNoise(xd, zd, noiseGenerator));
        double square = 1 - xd * xd / (radius * radius) - zd * zd / (radius * radius);
        if (square < 0) return null;
        double heightNoise = noiseGenerator.noise(x * 0.03, z * 0.03, 1,  0.5, 0.2);
        double rawY = Math.sqrt(square * (1 + heightNoise) * verticalRadius * verticalRadius);
        return (int) (center.getY() - rawY);
    }

    @NotNull
    public Integer bodyTop(int x, int z, NoiseGenerator noiseGenerator) {
        return (int) (center.getBlockY() + noiseGenerator.noise(x * 0.05, z * 0.05, 5, 1.2, 0.5) * 3);
    }

    private double angle(double x, double z) {
        // angel undefined
        if (x == 0 && z == 0) return 0;
        // Angle from -Pi/2 to Pi/2
        double angle = Math.atan(x / z);
        // Angle from 3*Pi/2 to -Pi/2
        if (z < 0) angle += Math.PI;
        return angle;
    }

    private double calculateAngleNoise(double x, double z, NoiseGenerator perlinNoiseGenerator) {
        double angle = angle(x, z);
        double scale = formDistortion / 10;
        double result = 0;
        for (int i = 0; i < 10; i++) {
            double move = Math.PI * i / 10;
            result += perlinNoiseGenerator.noise(Math.sin(angle + move) * 5, (double) i * 1000, 2, 0.5, 1.6, true) * scale;
        }
        return result;
    }

}
