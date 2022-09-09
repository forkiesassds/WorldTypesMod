package me.icanttellyou.mods.worldtypes.terrain;

import net.minecraft.src.NBTTagCompound;

import java.util.HashMap;

public interface IGeneratorSettings {
    void writeToNBT(NBTTagCompound nbttagcompound);

    void readFromNBT(NBTTagCompound nbttagcompound);

    void initializeFields(Object... args);

    HashMap getOptions();
}
