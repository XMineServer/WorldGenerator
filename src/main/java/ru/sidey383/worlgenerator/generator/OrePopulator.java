package ru.sidey383.worlgenerator.generator;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.jetbrains.annotations.NotNull;
import ru.sidey383.worlgenerator.generator.math.GaussianRandom;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OrePopulator extends BlockPopulator {
    private final Material oreType;
    private final int minVeinSize;
    private final int maxVeinSize;
    private final GaussianRandom  heightRandom;
    private final double veinsPerChunk;
    private final double rawChance;
    private final Vector[] directions = new Vector[] {
            new Vector(1, 0, 0),
            new Vector(0, 1, 0),
            new Vector(0, 0, 1)
    };

    public OrePopulator(Material oreType, int minVeinSize, int maxVeinSize,
                                    int center, int spread, double veinsPerChunk, double rawChance) {
        this.oreType = oreType;
        this.minVeinSize = minVeinSize;
        this.maxVeinSize = maxVeinSize;
        this.heightRandom = new GaussianRandom(center, spread);
        this.veinsPerChunk = veinsPerChunk;
        this.rawChance = rawChance;
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
            double xr = random.nextInt(0, 16) + baseX;
            double zr = random.nextInt(0, 16) + baseZ;
            double yr = Math.max(worldInfo.getMinHeight(), Math.min(worldInfo.getMaxHeight(), heightRandom.nextGaussian(random)));
            generateSingleVein(worldInfo, limitedRegion, (int) xr, (int) yr, (int) zr, random);
        }
    }

    private void generateSingleVein(WorldInfo worldInfo, LimitedRegion limitedRegion, int x, int y, int z, Random random) {
        Vector pose = new Vector(x, y, z);
        int size = random.nextInt(minVeinSize, maxVeinSize + 1);
        Material rawBlock = getRawBlockVariant(oreType);
        Set<Vector> availableBlock = new HashSet<>();
        while (size > 0) {
            boolean isRaw = rawBlock != null && random.nextDouble() < rawChance && size > 3;
            Material originalMaterial = limitedRegion.getType(pose.getBlockX(), pose.getBlockY(), pose.getBlockZ());
            boolean isOreReplaceable = Tag.STONE_ORE_REPLACEABLES.isTagged(originalMaterial);
            boolean isDeepsleateReplaceable = Tag.DEEPSLATE_ORE_REPLACEABLES.isTagged(originalMaterial);
            boolean isReplaceable = isOreReplaceable || isDeepsleateReplaceable;
            if (isReplaceable) {
                Material oreMaterial = oreType;
                if (isDeepsleateReplaceable) oreMaterial = getDeepslateBlockVariant(oreMaterial);
                if (isRaw) oreMaterial = rawBlock;
                limitedRegion.setType(pose.getBlockX(), pose.getBlockY(), pose.getBlockZ(), oreMaterial);
            }
            if (isRaw) {
                size-=4;
            } else {
                --size;
            }
            Vector v = pose.clone().add(directions[0]);
            if (limitedRegion.isInRegion(v.getBlockX(), v.getBlockY(), v.getBlockZ())) {
                availableBlock.add(v);
            }
            v = pose.clone().add(directions[1]);
            if (limitedRegion.isInRegion(v.getBlockX(), v.getBlockY(), v.getBlockZ())) {
                availableBlock.add(v);
            }
            v = pose.clone().add(directions[2]);
            if (limitedRegion.isInRegion(v.getBlockX(), v.getBlockY(), v.getBlockZ())) {
                availableBlock.add(v);
            }
            pose = availableBlock.stream().skip(random.nextInt(availableBlock.size())).findFirst().orElse(null);
            if (pose == null) break;
            availableBlock.remove(pose);
        }

    }

    private Material getRawBlockVariant(Material oreType) {
        return switch (oreType) {
            case IRON_ORE -> Material.RAW_IRON_BLOCK;
            case COPPER_ORE -> Material.RAW_COPPER_BLOCK;
            case GOLD_ORE -> Material.RAW_GOLD_BLOCK;
            case DIAMOND_ORE -> Material.DIAMOND_BLOCK;
            case COAL_ORE -> Material.COAL_BLOCK;
            case EMERALD_ORE -> Material.EMERALD_BLOCK;
            case LAPIS_ORE -> Material.LAPIS_BLOCK;
            case REDSTONE_ORE -> Material.REDSTONE_BLOCK;
            default -> null;
        };
    }

    private Material getDeepslateBlockVariant(Material oreType) {
        return switch (oreType) {
            case IRON_ORE -> Material.DEEPSLATE_IRON_ORE;
            case COPPER_ORE -> Material.DEEPSLATE_COPPER_ORE;
            case GOLD_ORE -> Material.DEEPSLATE_GOLD_ORE;
            case DIAMOND_ORE -> Material.DEEPSLATE_DIAMOND_ORE;
            case COAL_ORE -> Material.DEEPSLATE_COAL_ORE;
            case EMERALD_ORE -> Material.DEEPSLATE_EMERALD_ORE;
            case LAPIS_ORE -> Material.DEEPSLATE_LAPIS_ORE;
            case REDSTONE_ORE -> Material.DEEPSLATE_REDSTONE_ORE;
            default -> oreType;
        };
    }

}
