package me.icanttellyou.mods.worldtypes.terrain;

import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Random;

public class BiospheresTerrainGenerator implements ITerrainGenerator {
    BiosphereGeneratorSettings biosphereGeneratorSettings = new BiosphereGeneratorSettings();
    public final Random rndSphere;
    public final Random rndNoise;
    private World world;
    private NoiseGeneratorOctaves noiseGen;
    public static int GRID_SIZE = 9;
    private static int BRIDGE_SIZE = 2;
    public static int SPECIAL_RADIUS = 7;
    public static int LAVA_LEVEL = 24;
    public static byte DOME_TYPE = 20;
    public static byte BRIDGE_SUPPORT = 5;
    public static byte BRIDGE_RAIL = 85;
    public static boolean NOISE = true;
    public static boolean WATERWORLD = false;
    public int midX;
    public int midY;
    public int midZ;
    public int oreMidX;
    public int oreMidY;
    public int oreMidZ;
    public int lakeMidY;
    public double sphereRadius;
    public double lakeRadius;
    public double lakeEdgeRadius;
    public double noiseMin;
    public double noiseMax;
    public BiomeGenBase biome;
    public boolean hasLake;
    public boolean lavaLake;
    public double[] noise;

    static {
        BiomeGenBase.hell.fillerBlock = (byte) Block.netherrack.blockID;
        BiomeGenBase.hell.topBlock = (byte) Block.netherrack.blockID;

        if (BiospheresTerrainGenerator.WATERWORLD) {
            Block.lightOpacity[8] = 0;
            Block.lightOpacity[9] = 0;
        }
    }

    public static class BiosphereGeneratorSettings implements IGeneratorSettings {
        @Override
        public void writeToNBT(NBTTagCompound nbttagcompound) {
            nbttagcompound.setByte("domeType", DOME_TYPE);
            nbttagcompound.setBoolean("noise", NOISE);
            nbttagcompound.setBoolean("waterWorld", WATERWORLD);
            nbttagcompound.setInteger("gridSize", GRID_SIZE);
            nbttagcompound.setInteger("specialRadius", SPECIAL_RADIUS);
            nbttagcompound.setInteger("lavaLevel", LAVA_LEVEL);
            nbttagcompound.setInteger("bridgeSize", BRIDGE_SIZE);
            nbttagcompound.setByte("bridgeSupport", BRIDGE_SUPPORT);
            nbttagcompound.setByte("bridgeRail", BRIDGE_RAIL);
        }

        @Override
        public void readFromNBT(NBTTagCompound nbttagcompound) {
            DOME_TYPE = nbttagcompound.getByte("domeType");
            NOISE = nbttagcompound.getBoolean("noise");
            WATERWORLD = nbttagcompound.getBoolean("waterWorld");
            GRID_SIZE = nbttagcompound.getInteger("gridSize");
            SPECIAL_RADIUS = nbttagcompound.getInteger("specialRadius");
            LAVA_LEVEL = nbttagcompound.getInteger("lavaLevel");
            BRIDGE_SIZE = nbttagcompound.getInteger("bridgeSize");
            BRIDGE_SUPPORT = nbttagcompound.getByte("bridgeSupport");
            BRIDGE_RAIL = nbttagcompound.getByte("bridgeRail");
        }

        @Override
        public void initializeFields(Object... args) {
            if (args.length != 9) {
                throw new RuntimeException("Incorrect amount of arguments? Expected 9, received: " + args.length);
            }
            DOME_TYPE = (byte) args[0];
            NOISE = (boolean) args[1];
            WATERWORLD = (boolean) args[2];
            GRID_SIZE = (int) args[3];
            SPECIAL_RADIUS = (int) args[4];
            LAVA_LEVEL = (int) args[5];
            BRIDGE_SIZE = (int) args[6];
            BRIDGE_SUPPORT = (byte) args[7];
            BRIDGE_RAIL = (byte) args[8];
        }

        @Override
        public HashMap getOptions() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("domeType", DOME_TYPE);
            hashMap.put("noise", NOISE);
            hashMap.put("waterWorld", WATERWORLD);
            hashMap.put("gridSize", GRID_SIZE);
            hashMap.put("specialRadius", SPECIAL_RADIUS);
            hashMap.put("lavaLevel", LAVA_LEVEL);
            hashMap.put("bridgeSize", BRIDGE_SIZE);
            hashMap.put("bridgeSupport", BRIDGE_SUPPORT);
            hashMap.put("bridgeRail", BRIDGE_RAIL);
            return hashMap;
        }
    }

    public BiospheresTerrainGenerator(World world, long long3) {
        this.noiseMin = Double.MAX_VALUE;
        this.noiseMax = Double.MIN_VALUE;
        this.noise = new double[256];
        this.rndSphere = new Random(long3);
        this.world = world;
        if (BiospheresTerrainGenerator.NOISE) {
            this.rndNoise = new Random(long3);
            this.noiseGen = new NoiseGeneratorOctaves(this.rndNoise, 4);
        }
        else {
            this.rndNoise = null;
        }
    }

    public void setRand(int integer2, int integer3) {
        this.midX = (integer2 - (int)Math.floor(Math.IEEEremainder(integer2, BiospheresTerrainGenerator.GRID_SIZE)) << 4) + 8;
        this.midZ = (integer3 - (int)Math.floor(Math.IEEEremainder(integer3, BiospheresTerrainGenerator.GRID_SIZE)) << 4) + 8;
        this.oreMidX = this.midX + BiospheresTerrainGenerator.GRID_SIZE / 2 * 16 - BiospheresTerrainGenerator.SPECIAL_RADIUS;
        this.oreMidZ = this.midZ + BiospheresTerrainGenerator.GRID_SIZE / 2 * 16 - BiospheresTerrainGenerator.SPECIAL_RADIUS;
        this.rndSphere.setSeed(this.world.getRandomSeed());
        long long4 = this.rndSphere.nextLong() / 2L * 2L + 1L;
        long long6 = this.rndSphere.nextLong() / 2L * 2L + 1L;
        long long8 = (this.midX * long4 + this.midZ * long6) * 2512576L ^ this.world.getRandomSeed();
        this.rndSphere.setSeed(long8);
        this.sphereRadius = (double)Math.round(16.0 + this.rndSphere.nextDouble() * 32.0 + this.rndSphere.nextDouble() * 16.0);
        this.lakeRadius = (double)Math.round(this.sphereRadius / 4.0);
        this.lakeEdgeRadius = this.lakeRadius + 2.0;
        this.biome = this.world.getWorldChunkManager().getBiomeGenAt(integer2 << 4, integer3 << 4);
        this.lavaLake = (this.biome == BiomeGenBase.hell || (this.biome != BiomeGenBase.tundra && this.rndSphere.nextInt(10) == 0));
        this.hasLake = (this.rndSphere.nextInt(2) == 0);
        this.oreMidY = BiospheresTerrainGenerator.SPECIAL_RADIUS + 1 + this.rndSphere.nextInt(127 - (BiospheresTerrainGenerator.SPECIAL_RADIUS + 1));
        if (BiospheresTerrainGenerator.NOISE) {
            this.setNoise(this.midX >> 4, this.midZ >> 4);
            this.noiseMin = Double.MAX_VALUE;
            for (int i = 0; i < this.noise.length; ++i) {
                if (this.noise[i] < this.noiseMin) {
                    this.noiseMin = this.noise[i];
                }
            }
            this.lakeMidY = (int)Math.round(64.0 + this.noiseMin * 8.0);
            this.setNoise(integer2, integer3);
        }
        else {
            this.lakeMidY = this.midY;
        }
    }

    public void setNoise(int integer2, int integer3) {
        if (BiospheresTerrainGenerator.NOISE) {
            double double4 = 0.0078125;
            this.noise = this.noiseGen.generateNoiseOctaves(this.noise, integer2 * 16, 109.0134, integer3 * 16, 16, 1, 16, double4, 1.0, double4);
        }
    }

    public double getMainDistance(int integer2, int integer3, int integer4) {
        return (double)Math.round(getDistance(integer2, integer3, integer4, this.midX, this.midY, this.midZ));
    }

    public double getOreDistance(int integer2, int integer3, int integer4) {
        return (double)Math.round(getDistance(integer2, integer3, integer4, this.oreMidX, this.oreMidY, this.oreMidZ));
    }

    public int getSurfaceLevel(int integer2, int integer3) {
        if (BiospheresTerrainGenerator.NOISE) {
            return (int)Math.round(64.0 + this.noise[integer3 + integer2 * 16] * 8.0);
        }
        return 64;
    }

    public static final double getDistance(double double1, double double3, double double5, double double7, double double9, double double11) {
        return Math.sqrt(Math.pow(double9 - double3, 2.0) + Math.pow(double7 - double1, 2.0) + Math.pow(double11 - double5, 2.0));
    }

    @Override
    public void generateTerrain(int integer2, int integer3, byte[] arr, BiomeGenBase[] biomeGenBase4, double[] d5) {
        this.setRand(integer2, integer3);
        int integer5 = integer2 << 4;
        int integer6 = integer3 << 4;
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                this.midY = this.getSurfaceLevel(x, z);
                for (int y = 127; y >= 0; --y) {
                    double double10 = this.getMainDistance(integer5 + x, y, integer6 + z);
                    double double12 = this.getOreDistance(integer5 + x, y, integer6 + z);
                    int integer14 = (x * 16 + z) * 128 + y;
                    byte byte15 = 0;
                    if (y > this.midY) {
                        if (double10 == this.sphereRadius) {
                            if (y >= this.midY + 4 || (Math.abs(integer5 + x - this.midX) > BiospheresTerrainGenerator.BRIDGE_SIZE && Math.abs(integer6 + z - this.midZ) > BiospheresTerrainGenerator.BRIDGE_SIZE)) {
                                byte15 = BiospheresTerrainGenerator.DOME_TYPE;
                            }
                        }
                        else if (this.hasLake && BiospheresTerrainGenerator.NOISE && this.biome != BiomeGenBase.desert && (double10 == this.lakeRadius + 1.0 || double10 == this.lakeRadius + 2.0)) {
                            if (y == this.lakeMidY) {
                                byte15 = this.biome.topBlock;
                            }
                            else if (y < this.lakeMidY) {
                                byte15 = this.biome.fillerBlock;
                            }
                        }
                        else if (this.hasLake && BiospheresTerrainGenerator.NOISE && this.biome != BiomeGenBase.desert && double10 <= this.lakeRadius) {
                            if (y == this.lakeMidY && this.biome == BiomeGenBase.tundra) {
                                byte15 = (byte)Block.ice.blockID;
                            }
                            else if (y <= this.lakeMidY) {
                                byte15 = (byte)(this.lavaLake ? Block.lavaMoving.blockID : Block.waterMoving.blockID);
                            }
                        }
                        else if (BiospheresTerrainGenerator.WATERWORLD && y <= this.midY + 4 && double10 > this.sphereRadius && (Math.abs(integer5 + x - this.midX) == BiospheresTerrainGenerator.BRIDGE_SIZE || Math.abs(integer6 + z - this.midZ) == BiospheresTerrainGenerator.BRIDGE_SIZE)) {
                            byte15 = BiospheresTerrainGenerator.DOME_TYPE;
                        }
                        else if (BiospheresTerrainGenerator.WATERWORLD && y == this.midY + 4 && double10 > this.sphereRadius && (Math.abs(integer5 + x - this.midX) < BiospheresTerrainGenerator.BRIDGE_SIZE || Math.abs(integer6 + z - this.midZ) < BiospheresTerrainGenerator.BRIDGE_SIZE)) {
                            byte15 = BiospheresTerrainGenerator.DOME_TYPE;
                        }
                        else if (BiospheresTerrainGenerator.WATERWORLD && y < this.midY + 4 && double10 > this.sphereRadius && (Math.abs(integer5 + x - this.midX) < BiospheresTerrainGenerator.BRIDGE_SIZE || Math.abs(integer6 + z - this.midZ) < BiospheresTerrainGenerator.BRIDGE_SIZE)) {
                            byte15 = 0;
                        }
                        else if (BiospheresTerrainGenerator.WATERWORLD && double10 > this.sphereRadius) {
                            byte15 = (byte)Block.waterStill.blockID;
                        }
                        else if (y == this.midY + 1 && double10 > this.sphereRadius && (Math.abs(integer5 + x - this.midX) == BiospheresTerrainGenerator.BRIDGE_SIZE || Math.abs(integer6 + z - this.midZ) == BiospheresTerrainGenerator.BRIDGE_SIZE)) {
                            byte15 = BiospheresTerrainGenerator.BRIDGE_RAIL;
                        }
                    }
                    else if (double10 == this.sphereRadius) {
                        byte15 = (byte)Block.stone.blockID;
                    }
                    else if (this.hasLake && this.biome != BiomeGenBase.desert && double10 <= this.lakeRadius) {
                        if (y == this.lakeMidY && this.biome == BiomeGenBase.tundra) {
                            byte15 = (byte)Block.ice.blockID;
                        }
                        else if (y <= this.lakeMidY) {
                            byte15 = (byte)(this.lavaLake ? Block.lavaMoving.blockID : Block.waterMoving.blockID);
                        }
                    }
                    else if (this.hasLake && y < this.lakeMidY - 1 && this.biome != BiomeGenBase.desert && double10 <= this.lakeEdgeRadius) {
                        byte15 = (byte)(this.lavaLake ? Block.gravel.blockID : Block.sand.blockID);
                    }
                    else if (double10 < this.sphereRadius) {
                        if (y == this.midY) {
                            byte15 = this.biome.topBlock;
                        }
                        else if (y == this.midY - 1) {
                            byte15 = this.biome.fillerBlock;
                        }
                        else {
                            byte15 = (byte)Block.stone.blockID;
                        }
                    }
                    else if (y == this.midY && double10 > this.sphereRadius && (Math.abs(integer5 + x - this.midX) < BiospheresTerrainGenerator.BRIDGE_SIZE + 1 || Math.abs(integer6 + z - this.midZ) < BiospheresTerrainGenerator.BRIDGE_SIZE + 1)) {
                        byte15 = BiospheresTerrainGenerator.BRIDGE_SUPPORT;
                    }
                    else if (BiospheresTerrainGenerator.WATERWORLD && double10 > this.sphereRadius) {
                        byte15 = (byte)Block.waterStill.blockID;
                    }
                    if (double12 <= BiospheresTerrainGenerator.SPECIAL_RADIUS) {
                        int integer16 = this.rndSphere.nextInt(200);
                        int integer17 = Block.stone.blockID;
                        if (integer16 < 1) {
                            integer17 = Block.oreDiamond.blockID;
                        }
                        else if (integer16 < 2) {
                            integer17 = Block.oreLapis.blockID;
                        }
                        byte15 = (byte)integer17;
                    }
                    arr[integer14] = byte15;
                }
            }
        }
    }




//    @Override
//    public Chunk provideChunk(int integer2, int integer3) {
//
//        byte[] blockArray = new byte[32768];
//        Chunk chunk5 = new Chunk(this.world, blockArray, integer2, integer3);
//        this.preGenerateChunk(integer2, integer3, blockArray);
//        this.caveGen.func_867_a(this, this.world, integer2, integer3, blockArray);
//        chunk5.func_1024_c();
//        return chunk5;
//    }


    @Override
    public void populate(IChunkProvider iChunkProvider, int integer3, int integer4) {
        BlockSand.fallInstantly = true;
        int integer5 = integer3 << 4;
        int integer6 = integer4 << 4;
        this.biome = this.world.getWorldChunkManager().getBiomeGenAt(integer5, integer6);
        this.rndSphere.setSeed(this.world.getRandomSeed());
        long long7 = this.rndSphere.nextLong() / 2L * 2L + 1L;
        long long9 = this.rndSphere.nextLong() / 2L * 2L + 1L;
        this.rndSphere.setSeed(integer3 * long7 + integer4 * long9 ^ this.world.getRandomSeed());
        int integer13 = 0;
        int integer14 = 0;
        int integer15 = 0;
        for (int i = 0; i < 8; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(64);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenDungeons().generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        for (int i = 0; i < 10; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16);
            integer14 = this.rndSphere.nextInt(64);
            integer15 = integer6 + this.rndSphere.nextInt(16);
            new WorldGenClay(32).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        for (int i = 0; i < 20; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16);
            integer14 = this.rndSphere.nextInt(64);
            integer15 = integer6 + this.rndSphere.nextInt(16);
            new WorldGenMinable(Block.oreCoal.blockID, 16).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        for (int i = 0; i < 20; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16);
            integer14 = this.rndSphere.nextInt(64);
            integer15 = integer6 + this.rndSphere.nextInt(16);
            new WorldGenMinable(Block.oreIron.blockID, 8).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        for (int i = 0; i < 2; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16);
            integer14 = this.rndSphere.nextInt(64);
            integer15 = integer6 + this.rndSphere.nextInt(16);
            new WorldGenMinable(Block.oreGold.blockID, 8).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        for (int i = 0; i < 8; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16);
            integer14 = this.rndSphere.nextInt(64);
            integer15 = integer6 + this.rndSphere.nextInt(16);
            new WorldGenMinable(Block.oreRedstone.blockID, 7).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        int integer12 = 0;
        if (this.rndSphere.nextInt(10) == 0) {
            ++integer12;
        }
        if (this.biome == BiomeGenBase.forest) {
            integer12 += 5;
        }
        else if (this.biome == BiomeGenBase.rainforest) {
            integer12 += 5;
        }
        else if (this.biome == BiomeGenBase.seasonalForest) {
            integer12 += 2;
        }
        else if (this.biome == BiomeGenBase.taiga) {
            integer12 += 5;
        }
        else if (this.biome == BiomeGenBase.desert) {
            integer12 -= 20;
        }
        else if (this.biome == BiomeGenBase.tundra) {
            integer12 -= 20;
        }
        else if (this.biome == BiomeGenBase.plains) {
            integer12 -= 20;
        }
        for (int i = 0; i < integer12; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            WorldGenerator worldGenerator16 = this.biome.getRandomWorldGenForTrees(this.rndSphere);
            worldGenerator16.func_517_a(1.0, 1.0, 1.0);
            worldGenerator16.generate(this.world, this.rndSphere, integer13, this.world.getHeightValue(integer13, integer15), integer15);
        }
        for (int i = 0; i < 2; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(128);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenFlowers(Block.plantYellow.blockID).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        if (this.rndSphere.nextInt(2) == 0) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(128);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenFlowers(Block.plantRed.blockID).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        if (this.rndSphere.nextInt(4) == 0) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(128);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenFlowers(Block.mushroomBrown.blockID).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        if (this.rndSphere.nextInt(8) == 0) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(128);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenFlowers(Block.mushroomRed.blockID).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }

        integer12 = 0;
        if (this.biome == BiomeGenBase.forest) {
            integer12 = 2;
        }
        if (this.biome == BiomeGenBase.rainforest) {
            integer12 = 10;
        }
        if (this.biome == BiomeGenBase.seasonalForest) {
            integer12 = 2;
        }
        if (this.biome == BiomeGenBase.taiga) {
            integer12 = 1;
        }
        if (this.biome == BiomeGenBase.plains) {
            integer12 = 10;
        }
        for (int i = 0; i < integer12; ++i) {
            int integer16 = 1;
            if (this.biome == BiomeGenBase.rainforest && this.rndSphere.nextInt(3) != 0) {
                integer16 = 2;
            }
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(128);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenTallGrass(Block.tallGrass.blockID, integer16).generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }

        for (int i = 0; i < 10; ++i) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(128);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenReed().generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        if (this.rndSphere.nextInt(32) == 0) {
            integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
            integer14 = this.rndSphere.nextInt(128);
            integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
            new WorldGenPumpkin().generate(this.world, this.rndSphere, integer13, integer14, integer15);
        }
        if (this.biome == BiomeGenBase.desert) {
            for (int i = 0; i < this.rndSphere.nextInt(5); ++i) {
                integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
                integer14 = this.midY;
                integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
                new WorldGenCactus().generate(this.world, this.rndSphere, integer13, integer14, integer15);
            }
        }
        else if (this.biome == BiomeGenBase.hell) {
            if (this.rndSphere.nextBoolean()) {
                integer13 = integer5 + this.rndSphere.nextInt(16) + 8;
                integer14 = this.midY;
                integer15 = integer6 + this.rndSphere.nextInt(16) + 8;
                new WorldGenFire().generate(this.world, this.rndSphere, integer13, integer14, integer15);
            }
        }
        else if (this.biome == BiomeGenBase.tundra || this.biome == BiomeGenBase.taiga) {
            this.setNoise(integer3, integer4);
            for (int z = 0; integer15 < 16; ++integer15) {
                for (int x = 0; integer13 < 16; ++integer13) {
                    this.midY = this.getSurfaceLevel(integer13, integer15);
                    int integer16 = integer13 + integer5;
                    int integer17 = integer15 + integer6;
                    integer14 = this.midY + 1;
                    double double18 = this.getMainDistance(integer16, this.midY, integer17);
                    if (double18 <= this.sphereRadius && this.world.isAirBlock(integer16, integer14, integer17) && this.world.getBlockMaterial(integer16, integer14 - 1, integer17).getIsSolid()) {
                        if (this.world.getBlockMaterial(integer16, integer14 - 1, integer17) != Material.ice) {
                            this.world.setBlockAndMetadataWithNotify(integer16, integer14, integer17, Block.snow.blockID, 0);
                        }
                    }
                }
            }
        }
        BlockSand.fallInstantly = false;
    }

    @Override
    public IGeneratorSettings getSettings() {
        return biosphereGeneratorSettings;
    }
}
