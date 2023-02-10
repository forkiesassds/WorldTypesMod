package me.icanttellyou.mods.worldtypes.proxy.gui;

import me.icanttellyou.mods.worldtypes.proxy.MinecraftProxy;
import net.minecraft.src.*;
import overrideapi.utils.Reflection;
import overrideapi.utils.gui.GuiScreenOverride;

import java.lang.reflect.Field;

@GuiScreenOverride(GuiSelectWorldAether.class)
public class GuiSelectWorldAetherProxy extends GuiSelectWorldAether {
    private final MinecraftProxy minecraftProxy = new MinecraftProxy();

    private static final Field parentScreen = Reflection.publicField(Reflection.findField(GuiSelectWorld.class, "a", "parentScreen"));
    private static final Field selected = Reflection.publicField(Reflection.findField(GuiSelectWorld.class, "l", "selected"));

    public GuiSelectWorldAetherProxy() throws IllegalAccessException {
        super((GuiScreen) parentScreen.get(ModLoader.getMinecraftInstance().currentScreen), (int)Reflection.publicField(Reflection.findField(GuiSelectWorldAether.class, "musicID")).get(ModLoader.getMinecraftInstance().currentScreen));
    }


    @Override
    public void selectWorld(int i1) {
        this.mc.displayGuiScreen(null);
        try {
            if (!selected.getBoolean(this)) {

                selected.set(this, true);

                this.mc.playerController = new PlayerControllerSP(this.mc);
                String string2 = this.getSaveFileName(i1);
                if (string2 == null) {
                    string2 = "World" + i1;
                }

                this.minecraftProxy.startWorld(string2, this.getSaveName(i1), 0L);
                this.mc.displayGuiScreen(null);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
