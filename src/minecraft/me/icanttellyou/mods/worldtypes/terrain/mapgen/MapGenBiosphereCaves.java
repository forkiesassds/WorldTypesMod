package me.icanttellyou.mods.worldtypes.terrain.mapgen;

import me.icanttellyou.mods.worldtypes.provider.ChunkProviderCustom;
import me.icanttellyou.mods.worldtypes.terrain.BiospheresTerrainGenerator;
import me.icanttellyou.mods.worldtypes.terrain.ITerrainGenerator;
import net.minecraft.src.*;

import java.util.Random;

import static net.minecraft.src.mod_WorldTypes.generator;

public class MapGenBiosphereCaves extends MapGenBase {
    BiospheresTerrainGenerator generator;

    public MapGenBiosphereCaves() {
        this.generator = null;
    }

    @Override
    public void func_867_a(IChunkProvider iChunkProvider, World world, int integer4, int integer5, byte[] arr) {
        ITerrainGenerator iTerrainGenerator = ((ChunkProviderCustom)iChunkProvider).terrainGenerators.get(mod_WorldTypes.generator - ((ChunkProviderCustom)iChunkProvider).chunkProviders.toArray().length);
        if (this.generator == null && iTerrainGenerator instanceof BiospheresTerrainGenerator) {
            this.generator = (BiospheresTerrainGenerator) iTerrainGenerator;
        }
        super.func_867_a(iChunkProvider, world, integer4, integer5, arr);
    }

    protected void a(int integer2, int integer3, byte[] arr, double double5, double double7, double double9) {
        this.a(integer2, integer3, arr, double5, double7, double9, 10.0f + this.rand.nextFloat() * 20.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }

    protected void a(int integer2, int integer3, byte[] arr, double double5, double double7, double double9, float float11, float float12, float float13, int integer14, int integer15, double double16) {
        if (this.generator != null) {
            this.generator.setNoise(integer2, integer3);
        }
        double double18 = integer2 * 16 + 8;
        double double20 = integer3 * 16 + 8;
        float float22 = 0.0f;
        float float23 = 0.0f;
        Random random24 = new Random(this.rand.nextLong());
        if (integer15 <= 0) {
            int integer25 = this.field_1306_a * 16 - 16;
            integer15 = integer25 - random24.nextInt(integer25 / 4);
        }
        int integer25 = 0;
        if (integer14 == -1) {
            integer14 = integer15 / 2;
            integer25 = 1;
        }
        int integer26 = random24.nextInt(integer15 / 2) + integer15 / 4;
        int integer27 = (random24.nextInt(6) == 0) ? 1 : 0;
        while (integer14 < integer15) {
            double double28 = 1.5 + MathHelper.sin(integer14 * 3.141593f / integer15) * float11 * 1.0f;
            double double30 = double28 * double16;
            float float32 = MathHelper.cos(float13);
            float float33 = MathHelper.sin(float13);
            double5 += MathHelper.cos(float12) * float32;
            double7 += float33;
            double9 += MathHelper.sin(float12) * float32;
            if (integer27 != 0) {
                float13 *= 0.92f;
            }
            else {
                float13 *= 0.7f;
            }
            float13 += float23 * 0.1f;
            float12 += float22 * 0.1f;
            float23 *= 0.9f;
            float22 *= 0.75f;
            float23 += (random24.nextFloat() - random24.nextFloat()) * random24.nextFloat() * 2.0f;
            float22 += (random24.nextFloat() - random24.nextFloat()) * random24.nextFloat() * 4.0f;
            if (integer25 == 0 && integer14 == integer26 && float11 > 1.0f) {
                this.a(integer2, integer3, arr, double5, double7, double9, random24.nextFloat() * 0.5f + 0.5f, float12 - 1.570796f, float13 / 3.0f, integer14, integer15, 1.0);
                this.a(integer2, integer3, arr, double5, double7, double9, random24.nextFloat() * 0.5f + 0.5f, float12 + 1.570796f, float13 / 3.0f, integer14, integer15, 1.0);
                return;
            }
            if (integer25 != 0 || random24.nextInt(4) != 0) {
                double double34 = double5 - double18;
                double double36 = double9 - double20;
                double double38 = integer15 - integer14;
                double double40 = float11 + 2.0f + 16.0f;
                if (double34 * double34 + double36 * double36 - double38 * double38 > double40 * double40) {
                    return;
                }
                if (double5 >= double18 - 16.0 - double28 * 2.0 && double9 >= double20 - 16.0 - double28 * 2.0 && double5 <= double18 + 16.0 + double28 * 2.0) {
                    if (double9 <= double20 + 16.0 + double28 * 2.0) {
                        int integer42 = MathHelper.floor_double(double5 - double28) - integer2 * 16 - 1;
                        int integer43 = MathHelper.floor_double(double5 + double28) - integer2 * 16 + 1;
                        int integer44 = MathHelper.floor_double(double7 - double30) - 1;
                        int integer45 = MathHelper.floor_double(double7 + double30) + 1;
                        int integer46 = MathHelper.floor_double(double9 - double28) - integer3 * 16 - 1;
                        int integer47 = MathHelper.floor_double(double9 + double28) - integer3 * 16 + 1;
                        if (integer42 < 0) {
                            integer42 = 0;
                        }
                        if (integer43 > 16) {
                            integer43 = 16;
                        }
                        if (integer44 < 1) {
                            integer44 = 1;
                        }
                        if (integer45 > 120) {
                            integer45 = 120;
                        }
                        if (integer46 < 0) {
                            integer46 = 0;
                        }
                        if (integer47 > 16) {
                            integer47 = 16;
                        }
                        int integer48 = 0;
                        for (int i7 = integer42; integer48 == 0 && i7 < integer43; ++i7) {
                            for (int i8 = integer46; integer48 == 0 && i8 < integer47; ++i8) {
                                for (int i9 = integer45 + 1; integer48 == 0 && i9 >= integer44 - 1; --i9) {
                                    int integer49 = (i7 * 16 + i8) * 128 + i9;
                                    if (i9 >= 0 && i9 < 128) {
                                        if (arr[integer49] == Block.waterMoving.blockID || arr[integer49] == Block.waterStill.blockID || arr[integer49] == Block.lavaMoving.blockID || arr[integer49] == Block.lavaStill.blockID) {
                                            integer48 = 1;
                                        }
                                        if (i9 != integer44 - 1 && i7 != integer42 && i7 != integer43 - 1 && i8 != integer46 && i8 != integer47 - 1) {
                                            i9 = integer44;
                                        }
                                    }
                                }
                            }
                        }
                        if (integer48 == 0) {
                            for (int i7 = integer42; i7 < integer43; ++i7) {
                                double double51 = (i7 + integer2 * 16 + 0.5 - double5) / double28;
                                for (int integer49 = integer46; integer49 < integer47; ++integer49) {
                                    this.generator.midY = this.generator.getSurfaceLevel(i7, integer49);
                                    double double53 = (integer49 + integer3 * 16 + 0.5 - double9) / double28;
                                    int integer55 = (i7 * 16 + integer49) * 128 + integer45;
                                    for (int i12 = integer45 - 1; i12 >= integer44; --i12) {
                                        double double57 = (i12 + 0.5 - double7) / double30;
                                        if (double57 > -0.7 && double51 * double51 + double57 * double57 + double53 * double53 < 1.0) {
                                            int integer59 = arr[integer55];
                                            if (integer59 == Block.stone.blockID || integer59 == Block.sand.blockID || integer59 == Block.gravel.blockID || integer59 == Block.oreDiamond.blockID || integer59 == Block.oreLapis.blockID) {
                                                if (i12 < BiospheresTerrainGenerator.LAVA_LEVEL) {
                                                    if (this.generator != null) {
                                                        double double60 = this.generator.getMainDistance((int)Math.round(double18 + i7 - 8.0), i12 - 1, (int)Math.round(double20 + integer49 - 8.0));
                                                        if (double60 >= this.generator.sphereRadius && double60 < this.generator.sphereRadius + 5.0) {
                                                            arr[integer55] = (byte)Block.obsidian.blockID;
                                                        }
                                                        else if (double60 < this.generator.sphereRadius) {
                                                            arr[integer55] = (byte)Block.lavaMoving.blockID;
                                                        }
                                                    }
                                                    else {
                                                        arr[integer55] = (byte)Block.lavaMoving.blockID;
                                                    }
                                                }
                                                else if (i12 < this.generator.midY - 2 || i12 > this.generator.midY - 1) {
                                                    arr[integer55] = 0;
                                                }
                                            }
                                        }
                                        --integer55;
                                    }
                                }
                            }
                            if (integer25 != 0) {
                                break;
                            }
                        }
                    }
                }
            }
            ++integer14;
        }
    }

    @Override
    protected void func_868_a(World world, int integer3, int integer4, int integer5, int integer6, byte[] arr) {
        int integer8 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
        if (this.rand.nextInt(5) != 0) {
            integer8 = 0;
        }
        for (int j = 0; j < integer8; ++j) {
            double double10 = integer3 * 16 + this.rand.nextInt(16);
            double double12 = this.rand.nextInt(128);
            double double14 = integer4 * 16 + this.rand.nextInt(16);
            int integer16 = 1;
            if (this.rand.nextInt(4) == 0) {
                this.a(integer5, integer6, arr, double10, double12, double14);
                integer16 += this.rand.nextInt(4);
            }
            for (int m = 0; m < integer16; ++m) {
                float float18 = this.rand.nextFloat() * 3.141593f * 2.0f;
                float float19 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float float20 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                this.a(integer5, integer6, arr, double10, double12, double14, float20 * 5.0f, float18, float19, 0, 0, 0.5);
            }
        }
    }
}

