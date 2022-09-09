package me.icanttellyou.mods.worldtypes.proxy.world;

import net.minecraft.src.ISaveHandler;
import net.minecraft.src.SaveConverterMcRegion;

import java.io.File;

public class SaveConverterMcRegionProxy extends SaveConverterMcRegion {
    public SaveConverterMcRegionProxy(File file1) {
        super(file1);
    }

    @Override
    public ISaveHandler getSaveLoader(String string1, boolean z2) {
        return new SaveOldDirProxy(this.field_22180_a, string1, z2);
    }
}
