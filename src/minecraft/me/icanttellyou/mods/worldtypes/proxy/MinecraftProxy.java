package me.icanttellyou.mods.worldtypes.proxy;

import me.icanttellyou.mods.worldtypes.proxy.world.WorldProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ModLoader;
import net.minecraft.src.StatList;
import net.minecraft.src.World;

//despite it being called a proxy, it's not going to extend Minecraft.
public class MinecraftProxy {
    private final Minecraft mc = ModLoader.getMinecraftInstance();

    public void startWorld(String string1, String string2, long j3) {
        mc.changeWorld1(null);
        System.gc();
        if(mc.getSaveLoader().isOldMapFormat(string1)) {
            convertMapFormat(string1, string2);
        } else {
            ISaveHandler iSaveHandler5 = mc.getSaveLoader().getSaveLoader(string1, false);
            World world6;
            world6 = new WorldProxy(iSaveHandler5, string2, j3);
            if(world6.isNewWorld) {
                mc.statFileWriter.readStat(StatList.createWorldStat, 1);
                mc.statFileWriter.readStat(StatList.startGameStat, 1);
                mc.changeWorld2(world6, "Generating level");
            } else {
                mc.statFileWriter.readStat(StatList.loadWorldStat, 1);
                mc.statFileWriter.readStat(StatList.startGameStat, 1);
                mc.changeWorld2(world6, "Loading level");
            }
        }
    }

    private void convertMapFormat(String string1, String string2) {
        mc.loadingScreen.printText("Converting World to " + mc.getSaveLoader().func_22178_a());
        mc.loadingScreen.displayLoadingString("This may take a while :)");
        mc.getSaveLoader().convertMapFormat(string1, mc.loadingScreen);
        this.startWorld(string1, string2, 0L);
    }
}
