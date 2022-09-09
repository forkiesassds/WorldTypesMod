package me.icanttellyou.mods.worldtypes.proxy.world;

import net.minecraft.src.DimensionBase;
import me.icanttellyou.mods.worldtypes.provider.WorldProviderCustom;

public class DimensionOverworldProxy extends DimensionBase {
    public DimensionOverworldProxy() {
        super(0, WorldProviderCustom.class, null);
        this.name = "Overworld";
    }
}
