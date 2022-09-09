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
    public void generateTerrain(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4, double[] d5) {
        int blocklength = b3.length / 256;

        for(int i3 = 0; i3 < 16; ++i3) {
            for(int i4 = 0; i4 < 16; ++i4) {
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
                    b3[i3 << 11 | i4 << 7 | yCoordLayer] = (byte)blockid;
                }
            }
        }
    }

    @Override
    public void populate(IChunkProvider iChunkProvider1, int chunkx, int chunkz) {
        this.rand.setSeed(this.thisWorld.getRandomSeed());
        long j4 = this.rand.nextLong() / 2L * 2L + 1L;
        long j6 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)chunkx * j4 + (long)chunkz * j6 ^ this.thisWorld.getRandomSeed());
    }

    @Override
    public IGeneratorSettings getSettings() {
        return null;
    }
}
