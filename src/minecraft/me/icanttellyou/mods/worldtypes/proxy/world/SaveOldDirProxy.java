package me.icanttellyou.mods.worldtypes.proxy.world;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.SaveOldDir;
import net.minecraft.src.WorldInfo;

import java.io.File;
import java.nio.file.Files;

public class SaveOldDirProxy extends SaveOldDir {
    public SaveOldDirProxy(File paramFile, String paramString, boolean paramBoolean) {
        super(paramFile, paramString, paramBoolean);
    }

    @Override
    public WorldInfo loadWorldInfo() {
        File localFile = new File(this.getSaveDirectory(), "level.dat");
        NBTTagCompound localnu3;
        NBTTagCompound localException2;
        if(localFile.exists()) {
            try {
                localException2 = CompressedStreamTools.func_1138_a(Files.newInputStream(localFile.toPath()));
                localnu3 = localException2.getCompoundTag("Data");
                return new WorldInfoProxy(localnu3);
            } catch (Exception exception5) {
                exception5.printStackTrace();
            }
        }

        localFile = new File(this.getSaveDirectory(), "level.dat_old");
        if(localFile.exists()) {
            try {
                localException2 = CompressedStreamTools.func_1138_a(Files.newInputStream(localFile.toPath()));
                localnu3 = localException2.getCompoundTag("Data");
                return new WorldInfoProxy(localnu3);
            } catch (Exception exception4) {
                exception4.printStackTrace();
            }
        }

        return null;
    }
}
