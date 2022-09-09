package me.icanttellyou.mods.worldtypes.terrain;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IChunkProvider;

public interface ITerrainGenerator {
    void generateTerrain(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4, double[] d5);
    void populate(IChunkProvider iChunkProvider1, int chunkx, int chunkz);
    IGeneratorSettings getSettings();
}
