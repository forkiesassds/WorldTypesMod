package me.icanttellyou.mods.worldtypes.provider;

import me.icanttellyou.mods.worldtypes.terrain.BiospheresTerrainGenerator;
import me.icanttellyou.mods.worldtypes.terrain.FlatTerrainGenerator;
import me.icanttellyou.mods.worldtypes.terrain.ITerrainGenerator;
import me.icanttellyou.mods.worldtypes.terrain.TwoDTerrainGenerator;
import me.icanttellyou.mods.worldtypes.terrain.mapgen.MapGenBiosphereCaves;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.ChunkProviderSky;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.MapGenBase;
import net.minecraft.src.MapGenCaves;
import net.minecraft.src.mod_WorldTypes;
import net.minecraft.src.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.src.mod_WorldTypes.generator;

public class ChunkProviderCustom implements IChunkProvider {
    private final Random rand;
    private final World worldObj;
    private final MapGenBase field_902_u;
    private BiomeGenBase[] biomesForGeneration;
    //vanilla terrain generators
    public List<IChunkProvider> chunkProviders = new ArrayList<>();
    //custom terrain generators, which are more bare-bones since they do not work as chunk providers.
    public List<ITerrainGenerator> terrainGenerators = new ArrayList<>();

    public ChunkProviderCustom(World world1, long j2) {
        worldObj = world1;
        rand = new Random(j2);
        switch (mod_WorldTypes.enumWorldTypes[generator].worldType) {
            default:
                field_902_u = new MapGenCaves();
                break;
            case "biospheres":
                field_902_u = new MapGenBiosphereCaves();
                break;
        }
        chunkProviders.add(new ChunkProviderSky(world1, j2));
        chunkProviders.add(new ChunkProviderGenerate(world1, j2));
        terrainGenerators.add(new FlatTerrainGenerator(world1, j2));
        terrainGenerators.add(new TwoDTerrainGenerator(world1, j2));
        terrainGenerators.add(new BiospheresTerrainGenerator(world1, j2));
    }

    public void generateTerrain(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4, double[] d5) {
        switch (mod_WorldTypes.enumWorldTypes[generator].worldType) {
            default:
                ((ChunkProviderGenerate)chunkProviders.get(1)).generateTerrain(i1, i2, b3, biomeGenBase4, d5);
                break;
            case "sky":
                ((ChunkProviderSky)chunkProviders.get(0)).func_28071_a(i1, i2, b3, biomeGenBase4, d5);
                break;
            case "flat":
            case "2d":
            case "biospheres":
                terrainGenerators.get(generator - chunkProviders.toArray().length).generateTerrain(i1, i2, b3, biomeGenBase4, d5);
                break;
        }
    }

    public void replaceBlocksForBiome(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4) {
        switch (mod_WorldTypes.enumWorldTypes[generator].worldType) {
            default:
                ((ChunkProviderGenerate)chunkProviders.get(1)).replaceBlocksForBiome(i1, i2, b3, biomeGenBase4);
                break;
            case "sky":
                ((ChunkProviderSky)chunkProviders.get(0)).func_28072_a(i1, i2, b3, biomeGenBase4);
                break;
            case "flat":
            case "biospheres":
                break;
        }
    }

    @Override
    public Chunk prepareChunk(int i1, int i2) {
        return this.provideChunk(i1, i2);
    }

    @Override
    public Chunk provideChunk(int i1, int i2) {
        this.rand.setSeed((long)i1 * 341873128712L + (long)i2 * 132897987541L);
        byte[] b3 = new byte[32768];
        Chunk chunk4 = new Chunk(this.worldObj, b3, i1, i2);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, i1 * 16, i2 * 16, 16, 16);
        double[] d5 = this.worldObj.getWorldChunkManager().temperature;
        this.generateTerrain(i1, i2, b3, this.biomesForGeneration, d5);
        this.replaceBlocksForBiome(i1, i2, b3, this.biomesForGeneration);
        if (!mod_WorldTypes.enumWorldTypes[generator].worldType.equals("flat")) this.field_902_u.func_867_a(this, this.worldObj, i1, i2, b3);
        chunk4.func_1024_c();
        return chunk4;
    }

    @Override
    public boolean chunkExists(int i1, int i2) {
        return true;
    }

    @Override
    public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
        switch (mod_WorldTypes.enumWorldTypes[generator].worldType) {
            default:
                chunkProviders.get(1).populate(iChunkProvider1, i2, i3);
                break;
            case "flat":
            case "biospheres":
                terrainGenerators.get(generator - chunkProviders.toArray().length).populate(iChunkProvider1, i2, i3);
                break;
        }
    }

    @Override
    public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
        return true;
    }

    @Override
    public boolean unload100OldestChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "RandomLevelSource";
    }
}
