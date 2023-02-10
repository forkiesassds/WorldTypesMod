package me.icanttellyou.mods.worldtypes.terrain;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IChunkProvider;

public interface ITerrainGenerator {
    void generateTerrain(int chunkX, int chunkZ, byte[] blocks, BiomeGenBase[] biomes, double[] temps);
    void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ);
}
