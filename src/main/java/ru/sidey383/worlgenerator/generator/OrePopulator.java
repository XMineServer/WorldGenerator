package ru.sidey383.worlgenerator.generator;

import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class OrePopulator extends BlockPopulator {
    private final Material oreType;
    private final int minVeinSize;
    private final int maxVeinSize;
    private final int minY;
    private final int maxY;
    private final double veinsPerChunk;
    private final Material replaceMaterial;

    public OrePopulator(Material oreType, int minVeinSize, int maxVeinSize,
                                    int minY, int maxY, double veinsPerChunk, Material replaceMaterial) {
        this.oreType = oreType;
        this.minVeinSize = minVeinSize;
        this.maxVeinSize = maxVeinSize;
        this.minY = minY;
        this.maxY = maxY;
        this.veinsPerChunk = veinsPerChunk;
        this.replaceMaterial = replaceMaterial;
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random,
                         int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        // Определяем базовые координаты чанка
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;

        // Создаем генератор шума для плавных форм
        SimplexNoiseGenerator noise = new SimplexNoiseGenerator(worldInfo.getSeed());

        // Рассчитываем количество жил в этом чанке
        int veinCount = (int) veinsPerChunk;
        double fractionalPart = veinsPerChunk - veinCount;
        if (random.nextDouble() < fractionalPart) {
            veinCount++;
        }

        // Генерируем все жилы для этого чанка
        for (int i = 0; i < veinCount; i++) {
            generateSingleVein(worldInfo, random, baseX, baseZ, noise, limitedRegion);
        }
    }

    private void generateSingleVein(WorldInfo worldInfo, Random random, int baseX, int baseZ,
                                    SimplexNoiseGenerator noise, LimitedRegion limitedRegion) {
        // Выбираем случайную точку в расширенной области (включая соседние чанки)
        int centerX = baseX + random.nextInt(32) - 8;
        int centerZ = baseZ + random.nextInt(32) - 8;
        int centerY = minY + random.nextInt(maxY - minY + 1);

        // Определяем размер жилы
        int veinSize = minVeinSize + random.nextInt(maxVeinSize - minVeinSize + 1);

        // Генерируем блоки жилы
        for (int j = 0; j < veinSize; j++) {
            // Используем 3D сферическое распределение
            double theta = random.nextDouble() * Math.PI * 2;
            double phi = random.nextDouble() * Math.PI;
            double radius = random.nextDouble() * 6;

            int x = (int) (centerX + radius * Math.sin(phi) * Math.cos(theta));
            int z = (int) (centerZ + radius * Math.sin(phi) * Math.sin(theta));
            int y = (int) (centerY + radius * Math.cos(phi));

            // Проверяем границы мира
            if (y < worldInfo.getMinHeight() || y >= worldInfo.getMaxHeight()) continue;

            // Проверяем доступность блока для изменения
            if (!limitedRegion.isInRegion(x, y, z)) continue;

            // Применяем шум для естественной формы
            double noiseValue = noise.noise(x * 0.15, y * 0.15, z * 0.15);
            if (noiseValue > 0.2) {
                // Проверяем заменяемый материал
                if (limitedRegion.getType(x, y, z) == replaceMaterial) {
                    // Иногда добавляем вариации (например, сырые металлические блоки)
                    if (random.nextDouble() < 0.05 && oreType != Material.COAL_ORE) {
                        Material rawBlock = getRawBlockVariant(oreType);
                        if (rawBlock != null) {
                            limitedRegion.setType(x, y, z, rawBlock);
                            continue;
                        }
                    }
                    limitedRegion.setType(x, y, z, oreType);
                }
            }
        }
    }

    private Material getRawBlockVariant(Material oreType) {
        return switch (oreType) {
            case IRON_ORE -> Material.RAW_IRON_BLOCK;
            case COPPER_ORE -> Material.RAW_COPPER_BLOCK;
            case GOLD_ORE -> Material.RAW_GOLD_BLOCK;
            default -> null;
        };
    }

}
