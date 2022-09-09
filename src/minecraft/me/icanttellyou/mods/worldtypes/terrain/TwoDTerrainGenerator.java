package me.icanttellyou.mods.worldtypes.terrain;

import me.icanttellyou.mods.worldtypes.terrain.noise.NoiseGeneratorOctavesIndev;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;

import java.util.Random;

public class TwoDTerrainGenerator implements ITerrainGenerator {
    private final World thisWorld;
    private final Random rand;
    private final NoiseGeneratorOctavesIndev noiseGen1 = new NoiseGeneratorOctavesIndev(16);
    private final NoiseGeneratorOctavesIndev noiseGen2 = new NoiseGeneratorOctavesIndev(16);
    private final NoiseGeneratorOctavesIndev noiseGen3 = new NoiseGeneratorOctavesIndev(8);
    private final NoiseGeneratorOctavesIndev noiseGen4 = new NoiseGeneratorOctavesIndev(4);
    private final NoiseGeneratorOctavesIndev noiseGen5 = new NoiseGeneratorOctavesIndev(4);
    private final NoiseGeneratorOctavesIndev noiseGen6 = new NoiseGeneratorOctavesIndev(5);

    public TwoDTerrainGenerator(World world, long seed) {
        this.thisWorld = world;
        this.rand = new Random(seed);
        new NoiseGeneratorOctavesIndev(3);
        new NoiseGeneratorOctavesIndev(3);
        new NoiseGeneratorOctavesIndev(3);
    }

    @Override
    public void generateTerrain(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4, double[] d5) {
        long i3 = (long) i1 << 4;
        long i14 = (long) i2 << 4;
        int i4 = 0;

        for(long i5 = i3; i5 < i3 + 16; ++i5) {
            for(long i6 = i14; i6 < i14 + 16; ++i6) {
                long i7 = i5 / 1024;
                long i8 = i6 / 1024;
                double f9 = (this.noiseGen1.generateNoise((i5 / 0.03125F), 0.0D, (i6 / 0.03125F)) - this.noiseGen2.generateNoise(((double)i5 / 0.015625F), 0.0D, ((double) i6 / 0.015625F))) / 512.0F / 4.0F;
                double f10 = this.noiseGen5.generateNoise((i5 / 4.0F), (i6 / 4.0F));
                double f11 = this.noiseGen6.generateNoise((i5 / 8.0F), (i6 / 8.0F)) / 8.0F;
                f10 = f10 > 0.0F ? (this.noiseGen3.generateNoise(((double)i5 * 0.25714284F * 2.0F), ((double)i6 * 0.25714284F * 2.0F)) * f11 / 4.0D) : (this.noiseGen4.generateNoise((i5 * 0.25714284F), ((double)i6 * 0.25714284F)) * f11);
                long i15 = (int)(f9 + 64F + f10);
                if(this.noiseGen5.generateNoise((double)i5, (double)i6) < 0.0F) {
                    i15 = i15 / 2 << 1;
                    if(this.noiseGen5.generateNoise((double)(i5 / 5), (double)(i6 / 5)) < 0.0F) {
                        i15++;
                    }
                }

                for(int i16 = 0; i16 < 128; ++i16) {
                    long i17 = 0;

                    //lowered the sea level from y65 to y64 to prevent weird issues related with water
                    if(i16 <= 63) {
                        if (thisWorld.getWorldChunkManager().getBiomeGenAt((int) i5, (int) i6).getEnableSnow() && i16 == 63) {
                            i17 = Block.ice.blockID;
                        } else {
                            i17 = Block.waterStill.blockID;
                        }
                    }

                    if(i16 <= i15 - 2 || i16 == i15 && i15 >= 64 || i16 <= i15) {
                        i17 = Block.stone.blockID;
                    }

                    this.rand.setSeed((i7 + i8 * 13871));

                    if(i17 < 0) {
                        i17 = 0;
                    }
                    b3[i4++] = (byte) i17;
                }
            }
        }
    }

    @Override
    public void populate(IChunkProvider iChunkProvider1, int chunkx, int chunkz) {}

    @Override
    public IGeneratorSettings getSettings() {
        return null;
    }
}
