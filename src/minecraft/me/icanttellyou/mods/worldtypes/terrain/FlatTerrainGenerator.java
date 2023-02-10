package me.icanttellyou.mods.worldtypes.terrain;

import net.minecraft.src.*;

import java.util.Random;

public class FlatTerrainGenerator implements ITerrainGenerator {
    private final World thisWorld;
    private final Random rand;

    public FlatTerrainGenerator(World world, long seed) {
        this.thisWorld = world;
        this.rand = new Random(seed);
    }

    @Override
    public void generateTerrain(int chunkX, int chunkZ, byte[] blocks, BiomeGenBase[] biomes, double[] temps) {
        int blocklength = blocks.length / 256;

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                for(int yCoordLayer = 0; yCoordLayer < blocklength; ++yCoordLayer) {
                    int blockid = 0;
                    if(yCoordLayer == 0) {
                        blockid = Block.bedrock.blockID;
                    }
                    else if(yCoordLayer <= 60){
                        blockid = Block.stone.blockID;
                    }
                    else if(yCoordLayer <= 62) {
                        blockid = Block.dirt.blockID;
                    }
                    else if(yCoordLayer == 63) {
                        blockid = Block.grass.blockID;
                    }
                    blocks[x << 11 | z << 7 | yCoordLayer] = (byte)blockid;
                }
            }
        }
    }

    @Override
    public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
        this.rand.setSeed(this.thisWorld.getRandomSeed());
        long j4 = this.rand.nextLong() / 2L * 2L + 1L;
        long j6 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) chunkX * j4 + (long) chunkZ * j6 ^ this.thisWorld.getRandomSeed());
    }
}
