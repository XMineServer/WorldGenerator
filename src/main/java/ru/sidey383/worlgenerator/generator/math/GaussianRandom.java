package ru.sidey383.worlgenerator.generator.math;

import java.util.Random;

public class GaussianRandom {
    private final double mean;
    private final double stdDev;

    public GaussianRandom(double mean, double stdDev) {
        this.mean = mean;
        this.stdDev = stdDev;
    }

    public double nextGaussian(Random random) {
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();
        double yr = Math.sqrt(-2 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
        return mean + stdDev * yr;
    }
}
