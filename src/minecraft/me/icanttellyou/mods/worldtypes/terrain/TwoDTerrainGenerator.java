package me.icanttellyou.mods.worldtypes.terrain;

import me.icanttellyou.mods.worldtypes.terrain.noise.NoiseGeneratorOctavesIndev;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;

import java.util.Random;

public class TwoDTerrainGenerator implements ITerrainGenerator {
    private final Random rand;
    private final NoiseGeneratorOctavesIndev noiseGen1 = new NoiseGeneratorOctavesIndev(16);
    private final NoiseGeneratorOctavesIndev noiseGen2 = new NoiseGeneratorOctavesIndev(16);
    private final NoiseGeneratorOctavesIndev noiseGen3 = new NoiseGeneratorOctavesIndev(8);
    private final NoiseGeneratorOctavesIndev noiseGen4 = new NoiseGeneratorOctavesIndev(4);
    private final NoiseGeneratorOctavesIndev noiseGen5 = new NoiseGeneratorOctavesIndev(4);
    private final NoiseGeneratorOctavesIndev noiseGen6 = new NoiseGeneratorOctavesIndev(5);

    public TwoDTerrainGenerator(World world, long seed) {
        this.rand = new Random(seed);
        new NoiseGeneratorOctavesIndev(3);
        new NoiseGeneratorOctavesIndev(3);
        new NoiseGeneratorOctavesIndev(3);
    }

    @Override
    public void generateTerrain(int chunkX, int chunkZ, byte[] blocks, BiomeGenBase[] biomes, double[] temps) {
        long blockX = (long) chunkX << 4;
        long blockZ = (long) chunkZ << 4;
        int index = 0;

        for(long x = blockX; x < blockX + 16; ++x) {
            for(long z = blockZ; z < blockZ + 16; ++z) {
                long seedX = x / 1024;
                long seedZ = z / 1024;
                double f9 = (this.noiseGen1.generateNoise((x / 0.03125F), 0.0D, (z / 0.03125F)) - this.noiseGen2.generateNoise(((double)x / 0.015625F), 0.0D, ((double) z / 0.015625F))) / 512.0F / 4.0F;
                double f10 = this.noiseGen5.generateNoise((x / 4.0F), (z / 4.0F));
                double f11 = this.noiseGen6.generateNoise((x / 8.0F), (z / 8.0F)) / 8.0F;
                f10 = f10 > 0.0F ? (this.noiseGen3.generateNoise(((double)x * 0.25714284F * 2.0F), ((double)z * 0.25714284F * 2.0F)) * f11 / 4.0D) : (this.noiseGen4.generateNoise((x * 0.25714284F), ((double)z * 0.25714284F)) * f11);
                long i15 = (int)(f9 + 64F + f10);
                if(this.noiseGen5.generateNoise((double)x, (double)z) < 0.0F) {
                    i15 = i15 / 2 << 1;
                    if(this.noiseGen5.generateNoise((double)(x / 5), (double)(z / 5)) < 0.0F) {
                        i15++;
                    }
                }

                for(int y = 0; y < 128; ++y) {
                    long blockID = 0;

                    //lowered the sea level from y65 to y64 to prevent weird issues related with water
                    double temperature = temps[(int) ((x - blockX) * 16 + (z - blockZ))];
                    if(y <= 63) {
                        if (temperature <= 0.5D && y == 63) {
                            blockID = Block.ice.blockID;
                        } else {
                            blockID = Block.waterStill.blockID;
                        }
                    }

                    if(y <= i15 - 2 || y == i15 && i15 >= 64 || y <= i15) {
                        blockID = Block.stone.blockID;
                    }

                    this.rand.setSeed((seedX + seedZ * 13871));

                    if(blockID < 0) {
                        blockID = 0;
                    }
                    blocks[index++] = (byte) blockID;
                }
            }
        }
    }

    @Override
    public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {}
}
