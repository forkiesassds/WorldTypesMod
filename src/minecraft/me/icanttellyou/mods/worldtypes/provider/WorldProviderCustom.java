package me.icanttellyou.mods.worldtypes.provider;

import net.minecraft.src.*;

import static net.minecraft.src.mod_WorldTypes.*;

public class WorldProviderCustom extends WorldProvider {
    @Override
    public void registerWorldChunkManager() {
        switch (enumBiomeGenerators[biomeGenerator].biomeGenerator) {
            default:
//                switch (enumWorldTypes[generator].worldType) {
//                    default:
                        super.registerWorldChunkManager();
                        break;
//                }
            case "single":
                double i = 0.97D;
                double i2 = 0.45D;
                /* This makes the generator use temperature settings meant for the biomes (that are in vanilla) to avoid weird stuff.
                   Unfortunetley, weird stuff will still happen on modded biomes (ones that are in BiomeGenBase).
                 */
                if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.rainforest) {
                    i = 1D;
                    i2 = 1D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.swampland) {
                    i = 0.7D;
                    i2 = 0.5D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.seasonalForest) {
                    i = 1D;
                    i2 = 0.9D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.forest) {
                    i = 0.97D;
                    i2 = 0.6D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.savanna) {
                    i = 0.95D;
                    i2 = 0.2D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.shrubland) {
                    i = 0.97D;
                    i2 = 0.35D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.taiga) {
                    i = 0.5D;
                    i2 = 0.5D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.desert) {
                    i = 1D;
                    i2 = 0.2D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.plains) {
                    i = 0.97D;
                    i2 = 0.45D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.iceDesert) {
                    i = 0D;
                    i2 = 0D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.tundra) {
                    i = 0.5D;
                    i2 = 0.2D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.hell) {
                    i = 1D;
                    i2 = 0D;
                } else if (mod_WorldTypes.biomes.get(singleBiome) == BiomeGenBase.sky) {
                    i = 0.5D;
                    i2 = 0.5D;
                }

                //try to get biomeapi values for max temp and humidity
                try {
                    double temp = (double) BiomeGenBase.class.getField("maxTemp").get(singleBiome);
                    double humid = (double) BiomeGenBase.class.getField("maxHumid").get(singleBiome);
                    if (temp != Float.MIN_VALUE && humid != Float.MIN_VALUE) {
                        i = temp;
                        i2 = humid;
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                }
                this.worldChunkMgr = new WorldChunkManagerHell(mod_WorldTypes.biomes.get(singleBiome), i, i2);
                break;
        }
        if (enumWorldTypes[generator] == EnumWorldTypes.ALPHA) {
            this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 1, 1);
        }
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return new ChunkProviderCustom(this.worldObj, this.worldObj.getRandomSeed());
    }

    @Override
    public boolean canCoordinateBeSpawn(int i1, int i2) {
        int i3 = this.worldObj.getFirstUncoveredBlock(i1, i2);
        return i3 != 0 && (mod_WorldTypes.enumWorldTypes[generator].worldType.equals("default") ? i3 == Block.sand.blockID : i3 == Block.grass.blockID || i3 == Block.sand.blockID);
    }

    @Override
    public float getCloudHeight() {
        switch (enumWorldTypes[generator].worldType) {
            default:
                return super.getCloudHeight();
            case "sky":
            case "biospheres":
                return 8.0F;
        }
    }

    @Override
    public boolean func_28112_c() {
        switch (enumWorldTypes[generator].worldType) {
            default:
                return super.func_28112_c();
            case "sky":
                return false;
        }
    }
}
