package me.icanttellyou.mods.worldtypes.chunkmanager;

import me.icanttellyou.mods.worldtypes.terrain.BiospheresTerrainGenerator;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

import java.util.Random;

public class WorldChunkManagerBiospheres extends WorldChunkManager {
    private World world;
    private Random rnd;

    public WorldChunkManagerBiospheres(World world) {
        this.world = world;
        this.rnd = new Random(world.getRandomSeed());
    }

    private double getTemp(int integer2, int integer3) {
        int integer4 = integer2 >> 4;
        int integer5 = integer3 >> 4;
        int integer6 = (integer4 - (int)Math.floor(Math.IEEEremainder(integer4, BiospheresTerrainGenerator.GRID_SIZE)) << 4) + 8;
        int integer7 = (integer5 - (int)Math.floor(Math.IEEEremainder(integer5, BiospheresTerrainGenerator.GRID_SIZE)) << 4) + 8;
        if (this.world != null) {
            this.rnd.setSeed(this.world.getRandomSeed());
            long long8 = this.rnd.nextLong() / 2L * 2L + 1L;
            long long10 = this.rnd.nextLong() / 2L * 2L + 1L;
            this.rnd.setSeed((integer6 * long8 + integer7 * long10) * 7215145L ^ this.world.getRandomSeed());
        }
        return (this.rnd.nextDouble() + this.rnd.nextDouble()) / 2.0;
    }

    private double getHumidity(int integer2, int integer3) {
        int integer4 = integer2 >> 4;
        int integer5 = integer3 >> 4;
        int integer6 = (integer4 - (int)Math.floor(Math.IEEEremainder(integer4, BiospheresTerrainGenerator.GRID_SIZE)) << 4) + 8;
        int integer7 = (integer5 - (int)Math.floor(Math.IEEEremainder(integer5, BiospheresTerrainGenerator.GRID_SIZE)) << 4) + 8;
        if (this.world != null) {
            this.rnd.setSeed(this.world.getRandomSeed());
            long long8 = this.rnd.nextLong() / 2L * 2L + 1L;
            long long10 = this.rnd.nextLong() / 2L * 2L + 1L;
            this.rnd.setSeed((integer6 * long8 + integer7 * long10) * 1551617761L ^ this.world.getRandomSeed());
        }
        return (this.rnd.nextDouble() + this.rnd.nextDouble()) / 2.0;
    }

    @Override
    public double getTemperature(int integer2, int integer3) {
        return this.getTemp(integer2, integer3);
    }

    @Override
    public double[] getTemperatures(double[] arr, int integer3, int integer4, int integer5, int integer6) {
        if (arr == null || arr.length < integer5 * integer6) {
            arr = new double[integer5 * integer6];
        }
        double double7 = this.getTemp(integer3, integer4);
        int integer9 = 0;
        for (int j = 0; j < integer5; ++j) {
            for (int k = 0; k < integer6; ++k) {
                arr[integer9] = double7;
                ++integer9;
            }
        }
        return arr;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] arr, int integer3, int integer4, int integer5, int integer6) {
        if (arr == null || arr.length < integer5 * integer6) {
            arr = new BiomeGenBase[integer5 * integer6];
        }
        this.temperature = new double[integer5 * integer5];
        this.humidity = new double[integer5 * integer5];
        double double7 = this.getTemp(integer3, integer4);
        double double9 = this.getHumidity(integer3, integer4);
        boolean boolean11 = this.rnd.nextInt(50) == 0;
        BiomeGenBase biomeGenBase12;
        if (boolean11) {
            biomeGenBase12 = BiomeGenBase.hell;
        }
        else {
            biomeGenBase12 = BiomeGenBase.getBiomeFromLookup(double7, double9);
        }
        int integer13 = 0;
        for (int j = 0; j < integer5; ++j) {
            for (int k = 0; k < integer6; ++k) {
                this.temperature[integer13] = double7;
                this.humidity[integer13] = double9;
                arr[integer13++] = biomeGenBase12;
            }
        }
        return arr;
    }
}