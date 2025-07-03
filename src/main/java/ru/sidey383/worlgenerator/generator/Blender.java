package ru.sidey383.worlgenerator.generator;

public class Blender {

    public static final double[][] BASE_WEIGHTS = {
            {0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.1f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f},
            {0.0f, 0.0f, 0.1f, 0.1f, 0.2f, 0.2f, 0.2f, 0.1f, 0.1f, 0.0f, 0.0f},
            {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.4f, 0.4f, 0.3f, 0.2f, 0.1f, 0.0f},
            {0.0f, 0.1f, 0.3f, 0.5f, 0.6f, 0.7f, 0.6f, 0.5f, 0.3f, 0.1f, 0.0f},
            {0.1f, 0.2f, 0.4f, 0.6f, 0.8f, 0.9f, 0.8f, 0.6f, 0.4f, 0.2f, 0.1f},
            {0.1f, 0.2f, 0.4f, 0.7f, 0.9f, 1.0f, 0.9f, 0.7f, 0.4f, 0.2f, 0.1f},
            {0.1f, 0.2f, 0.4f, 0.6f, 0.8f, 0.9f, 0.8f, 0.6f, 0.4f, 0.2f, 0.1f},
            {0.0f, 0.1f, 0.3f, 0.5f, 0.6f, 0.7f, 0.6f, 0.5f, 0.3f, 0.1f, 0.0f},
            {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.4f, 0.4f, 0.3f, 0.2f, 0.1f, 0.0f},
            {0.0f, 0.0f, 0.1f, 0.1f, 0.2f, 0.2f, 0.2f, 0.1f, 0.1f, 0.0f, 0.0f},
            {0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.1f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f}
    };

    public static final double WEIGHT_SUM = sum(BASE_WEIGHTS);

    private static double sum(double[][] weights) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            double[] w = weights[i];
            for (int j = 0; j < w.length; j++) {
                sum += w[j];
            }
        }
        return sum;
    }

}
