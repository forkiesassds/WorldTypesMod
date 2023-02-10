package me.icanttellyou.mods.worldtypes.terrain;

import me.icanttellyou.mods.worldtypes.terrain.noise.NoiseGeneratorOctavesAlpha;
import net.minecraft.src.*;

import java.util.Random;

public class AlphaTerrainGenerator implements ITerrainGenerator {
    private Random rand;
    private World worldObj;
    private NoiseGeneratorOctavesAlpha noiseGen1;
    private NoiseGeneratorOctavesAlpha noiseGen2;
    private NoiseGeneratorOctavesAlpha noiseGen3;
    public NoiseGeneratorOctavesAlpha noiseGen6;
    public NoiseGeneratorOctavesAlpha noiseGen7;
    public NoiseGeneratorOctavesAlpha mobSpawnerNoise;
    private double[] noiseArray;
    double[] noise1;
    double[] noise2;
    double[] noise3;
    double[] noise6;
    double[] noise7;
    private double[] generatedTemperatures;

    public AlphaTerrainGenerator(World worldObj, long seed) {
        this.worldObj = worldObj;
        this.rand = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctavesAlpha(this.rand, 16);
        this.noiseGen2 = new NoiseGeneratorOctavesAlpha(this.rand, 16);
        this.noiseGen3 = new NoiseGeneratorOctavesAlpha(this.rand, 8);
        this.noiseGen6 = new NoiseGeneratorOctavesAlpha(this.rand, 10);
        this.noiseGen7 = new NoiseGeneratorOctavesAlpha(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctavesAlpha(this.rand, 8);
    }

    @Override
    public void generateTerrain(int chunkX, int chunkZ, byte[] blocks, BiomeGenBase[] biomes, double[] temps) {
        byte b4 = 4;
        byte b5 = 64;
        int i6 = b4 + 1;
        byte b7 = 17;
        int i8 = b4 + 1;
        this.noiseArray = this.initializeNoiseField(this.noiseArray, chunkX * b4, 0, chunkZ * b4, i6, b7, i8);

        for(int i9 = 0; i9 < b4; ++i9) {
            for(int i10 = 0; i10 < b4; ++i10) {
                for(int i11 = 0; i11 < 16; ++i11) {
                    double d12 = 0.125D;
                    double d14 = this.noiseArray[((i9 + 0) * i8 + i10 + 0) * b7 + i11 + 0];
                    double d16 = this.noiseArray[((i9 + 0) * i8 + i10 + 1) * b7 + i11 + 0];
                    double d18 = this.noiseArray[((i9 + 1) * i8 + i10 + 0) * b7 + i11 + 0];
                    double d20 = this.noiseArray[((i9 + 1) * i8 + i10 + 1) * b7 + i11 + 0];
                    double d22 = (this.noiseArray[((i9 + 0) * i8 + i10 + 0) * b7 + i11 + 1] - d14) * d12;
                    double d24 = (this.noiseArray[((i9 + 0) * i8 + i10 + 1) * b7 + i11 + 1] - d16) * d12;
                    double d26 = (this.noiseArray[((i9 + 1) * i8 + i10 + 0) * b7 + i11 + 1] - d18) * d12;
                    double d28 = (this.noiseArray[((i9 + 1) * i8 + i10 + 1) * b7 + i11 + 1] - d20) * d12;

                    for(int i30 = 0; i30 < 8; ++i30) {
                        double d31 = 0.25D;
                        double d33 = d14;
                        double d35 = d16;
                        double d37 = (d18 - d14) * d31;
                        double d39 = (d20 - d16) * d31;

                        for(int i41 = 0; i41 < 4; ++i41) {
                            int i42 = i41 + i9 * 4 << 11 | 0 + i10 * 4 << 7 | i11 * 8 + i30;
                            short s43 = 128;
                            double d44 = 0.25D;
                            double d46 = d33;
                            double d48 = (d35 - d33) * d44;

                            for(int i50 = 0; i50 < 4; ++i50) {
                                int i51 = 0;
                                double temperature = temps[(i9 * 4 + i41) * 16 + i10 * 4 + i50];
                                if(i11 * 8 + i30 < b5) {
                                    if(temperature <= 0.5D && i11 * 8 + i30 >= b5 - 1) {
                                        i51 = Block.ice.blockID;
                                    } else {
                                        i51 = Block.waterStill.blockID;
                                    }
                                }

                                if(d46 > 0.0D) {
                                    i51 = Block.stone.blockID;
                                }

                                blocks[i42] = (byte)i51;
                                i42 += s43;
                                d46 += d48;
                            }

                            d33 += d37;
                            d35 += d39;
                        }

                        d14 += d22;
                        d16 += d24;
                        d18 += d26;
                        d20 += d28;
                    }
                }
            }
        }
    }

    private double[] initializeNoiseField(double[] d1, int i2, int i3, int i4, int i5, int i6, int i7) {
        if(d1 == null) {
            d1 = new double[i5 * i6 * i7];
        }

        double scaleXZ = 684.412D;
        double scaleY = 684.412D;
        this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, (double)i2, (double)i3, (double)i4, i5, i6, i7, scaleXZ, scaleY, scaleXZ);
        this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, (double)i2, (double)i3, (double)i4, i5, i6, i7, scaleXZ, scaleY, scaleXZ);
        this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, (double)i2, (double)i3, (double)i4, i5, i6, i7, scaleXZ / 80.0D, scaleY / 160.0D, scaleXZ / 80.0D);
        this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, (double)i2, (double)i3, (double)i4, i5, 1, i7, 1.0D, 0.0D, 1.0D);
        this.noise7 = this.noiseGen7.generateNoiseOctaves(this.noise7, (double)i2, (double)i3, (double)i4, i5, 1, i7, 100.0D, 0.0D, 100.0D);
        int i12 = 0;
        int i13 = 0;

        for(int i14 = 0; i14 < i5; ++i14) {
            for(int i15 = 0; i15 < i7; ++i15) {
                double d16 = (this.noise6[i13] + 256.0D) / 512.0D;
                if(d16 > 1.0D) {
                    d16 = 1.0D;
                }

                double d18 = 0.0D;
                double d20 = this.noise7[i13] / 8000.0D;
                if(d20 < 0.0D) {
                    d20 = -d20;
                }

                d20 = d20 * 3.0D - 3.0D;
                if(d20 < 0.0D) {
                    d20 /= 2.0D;
                    if(d20 < -1.0D) {
                        d20 = -1.0D;
                    }

                    d20 /= 1.4D;
                    d20 /= 2.0D;
                    d16 = 0.0D;
                } else {
                    if(d20 > 1.0D) {
                        d20 = 1.0D;
                    }

                    d20 /= 6.0D;
                }

                d16 += 0.5D;
                d20 = d20 * (double)i6 / 16.0D;
                double d22 = (double)i6 / 2.0D + d20 * 4.0D;
                ++i13;

                for(int i24 = 0; i24 < i6; ++i24) {
                    double d25 = 0.0D;
                    double d27 = ((double)i24 - d22) * 12.0D / d16;
                    if(d27 < 0.0D) {
                        d27 *= 4.0D;
                    }

                    double d29 = this.noise1[i12] / 512.0D;
                    double d31 = this.noise2[i12] / 512.0D;
                    double d33 = (this.noise3[i12] / 10.0D + 1.0D) / 2.0D;
                    if(d33 < 0.0D) {
                        d25 = d29;
                    } else if(d33 > 1.0D) {
                        d25 = d31;
                    } else {
                        d25 = d29 + (d31 - d29) * d33;
                    }

                    d25 -= d27;
                    double d35;
                    if(i24 > i6 - 4) {
                        d35 = (double)((float)(i24 - (i6 - 4)) / 3.0F);
                        d25 = d25 * (1.0D - d35) + -10.0D * d35;
                    }

                    if((double)i24 < d18) {
                        d35 = (d18 - (double)i24) / 4.0D;
                        if(d35 < 0.0D) {
                            d35 = 0.0D;
                        }

                        if(d35 > 1.0D) {
                            d35 = 1.0D;
                        }

                        d25 = d25 * (1.0D - d35) + -10.0D * d35;
                    }

                    d1[i12] = d25;
                    ++i12;
                }
            }
        }

        return d1;
    }

    @Override
    public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
        BlockSand.fallInstantly = true;
        int i4 = chunkX * 16;
        int i5 = chunkZ * 16;
        this.rand.setSeed(this.worldObj.getRandomSeed());
        long j6 = this.rand.nextLong() / 2L * 2L + 1L;
        long j8 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)chunkX * j6 + (long)chunkZ * j8 ^ this.worldObj.getRandomSeed());
        double d10 = 0.25D;

        int i12;
        int i13;
        int i14;
        int i15;
        for(i12 = 0; i12 < 8; ++i12) {
            i13 = i4 + this.rand.nextInt(16) + 8;
            i14 = this.rand.nextInt(128);
            i15 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 10; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(128);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenClay(32)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 20; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(128);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.dirt.blockID, 32)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 10; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(128);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.gravel.blockID, 32)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 20; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(128);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 20; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(64);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 2; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(32);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 8; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(16);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        for(i12 = 0; i12 < 1; ++i12) {
            i13 = i4 + this.rand.nextInt(16);
            i14 = this.rand.nextInt(16);
            i15 = i5 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(this.worldObj, this.rand, i13, i14, i15);
        }

        d10 = 0.5D;
        i12 = (int)((this.mobSpawnerNoise.generateNoiseOctaves((double)i4 * d10, (double)i5 * d10) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
        if(i12 < 0) {
            i12 = 0;
        }

        if(this.rand.nextInt(10) == 0) {
            ++i12;
        }

        Object object18 = new WorldGenTrees();
        if(this.rand.nextInt(10) == 0) {
            object18 = new WorldGenBigTree();
        }

        int i16;
        for(i14 = 0; i14 < i12; ++i14) {
            i15 = i4 + this.rand.nextInt(16) + 8;
            i16 = i5 + this.rand.nextInt(16) + 8;
            ((WorldGenerator)object18).func_517_a(1.0D, 1.0D, 1.0D);
            ((WorldGenerator)object18).generate(this.worldObj, this.rand, i15, this.worldObj.getHeightValue(i15, i16), i16);
        }

        int i17;
        for(i14 = 0; i14 < 2; ++i14) {
            i15 = i4 + this.rand.nextInt(16) + 8;
            i16 = this.rand.nextInt(128);
            i17 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, i15, i16, i17);
        }

        if(this.rand.nextInt(2) == 0) {
            i14 = i4 + this.rand.nextInt(16) + 8;
            i15 = this.rand.nextInt(128);
            i16 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, i14, i15, i16);
        }

        if(this.rand.nextInt(4) == 0) {
            i14 = i4 + this.rand.nextInt(16) + 8;
            i15 = this.rand.nextInt(128);
            i16 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, i14, i15, i16);
        }

        if(this.rand.nextInt(8) == 0) {
            i14 = i4 + this.rand.nextInt(16) + 8;
            i15 = this.rand.nextInt(128);
            i16 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, i14, i15, i16);
        }

        for(i14 = 0; i14 < 10; ++i14) {
            i15 = i4 + this.rand.nextInt(16) + 8;
            i16 = this.rand.nextInt(128);
            i17 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenReed()).generate(this.worldObj, this.rand, i15, i16, i17);
        }

        for(i14 = 0; i14 < 1; ++i14) {
            i15 = i4 + this.rand.nextInt(16) + 8;
            i16 = this.rand.nextInt(128);
            i17 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(this.worldObj, this.rand, i15, i16, i17);
        }

        for(i14 = 0; i14 < 50; ++i14) {
            i15 = i4 + this.rand.nextInt(16) + 8;
            i16 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            i17 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, i15, i16, i17);
        }

        for(i14 = 0; i14 < 20; ++i14) {
            i15 = i4 + this.rand.nextInt(16) + 8;
            i16 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
            i17 = i5 + this.rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, i15, i16, i17);
        }

        this.generatedTemperatures = this.worldObj.getWorldChunkManager().getTemperatures(this.generatedTemperatures, i4 + 8, i5 + 8, 16, 16);

        for(i14 = i4 + 8 + 0; i14 < i4 + 8 + 16; ++i14) {
            for(i15 = i5 + 8 + 0; i15 < i5 + 8 + 16; ++i15) {
                int i20 = i14 - (i4 + 8);
                int i21 = i15 - (i5 + 8);
                i16 = this.worldObj.findTopSolidBlock(i14, i15);
                double d23 = this.generatedTemperatures[i20 * 16 + i21] - (double)(i16 - 64) / 64.0D * 0.3D;
                if(d23 < 0.5D && i16 > 0 && i16 < 128 && this.worldObj.getBlockId(i14, i16, i15) == 0 && this.worldObj.getBlockMaterial(i14, i16 - 1, i15).getIsSolid() && this.worldObj.getBlockMaterial(i14, i16 - 1, i15) != Material.ice) {
                    this.worldObj.setBlockWithNotify(i14, i16, i15, Block.snow.blockID);
                }
            }
        }

        BlockSand.fallInstantly = false;
    }
}
