package me.icanttellyou.mods.worldtypes.proxy.world;

import net.minecraft.src.ISaveHandler;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;

public class WorldProxy extends World {
    public WorldProxy(ISaveHandler wt1, String s1, long l1) {
        this(wt1, s1, l1, null);
    }

    public WorldProxy(ISaveHandler wt1, String s1, long l1, WorldProvider xa1) {
        super(wt1, s1, l1, xa1);
        this.worldInfo = wt1.loadWorldInfo();

        boolean flag1 = false;
        if(this.worldInfo == null) {
            this.worldInfo = new WorldInfoProxy(l1, s1);
            flag1 = true;
        } else {
            this.worldInfo.setWorldName(s1);
        }

        if(flag1) {
            this.getInitialSpawnLocation();
        }
    }
}
