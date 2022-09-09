package me.icanttellyou.mods.worldtypes.proxy.world;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.mod_WorldTypes;
import net.minecraft.src.overrideapi.utils.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class WorldInfoProxy extends WorldInfo {
    private static final Field playerTag = Reflection.publicField(Reflection.findField(WorldInfo.class, "h", "playerTag"));

    public WorldInfoProxy(NBTTagCompound nBTTagCompound1) {
        super(nBTTagCompound1);
        mod_WorldTypes.generator = nBTTagCompound1.getInteger("worldType");
        mod_WorldTypes.biomeGenerator = nBTTagCompound1.getInteger("biomeGenerator");
        mod_WorldTypes.singleBiome = nBTTagCompound1.getInteger("singleBiome");
    }

    public WorldInfoProxy(long l1, String s1) {
        super(l1, s1);
    }

    private void updateTags(NBTTagCompound nBTTagCompound1) {
        nBTTagCompound1.setInteger("worldType", mod_WorldTypes.generator);
        nBTTagCompound1.setInteger("biomeGenerator", mod_WorldTypes.biomeGenerator);
        nBTTagCompound1.setInteger("singleBiome", mod_WorldTypes.singleBiome);
    }

    private NBTTagCompound invokeUpdateTagCompound(NBTTagCompound nbtTagCompound, NBTTagCompound nbtTagCompound2) {
        try {
            Method method = WorldInfo.class.getDeclaredMethod(mod_WorldTypes.isDevEnv ? "updateTagCompound" : "a", NBTTagCompound.class, NBTTagCompound.class);
            method.setAccessible(true);
            return (NBTTagCompound) method.invoke(this, nbtTagCompound, nbtTagCompound2);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Failed to invoke updateTagCompound!: " + e);
        }
    }

    @Override
    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound nBTTagCompound1 = new NBTTagCompound();
        try {
            this.invokeUpdateTagCompound(nBTTagCompound1, (NBTTagCompound) playerTag.get(this));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.updateTags(nBTTagCompound1);
        return nBTTagCompound1;
    }

    @Override
    public NBTTagCompound getNBTTagCompoundWithPlayer(List list1) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        EntityPlayer entityPlayer3 = null;
        NBTTagCompound nBTTagCompound4 = null;
        if(list1.size() > 0) {
            entityPlayer3 = (EntityPlayer)list1.get(0);
        }

        if(entityPlayer3 != null) {
            nBTTagCompound4 = new NBTTagCompound();
            entityPlayer3.writeToNBT(nBTTagCompound4);
        }

        this.invokeUpdateTagCompound(nBTTagCompound2, nBTTagCompound4);
        this.updateTags(nBTTagCompound2);
        return nBTTagCompound2;
    }
}
