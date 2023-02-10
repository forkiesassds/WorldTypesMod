package me.icanttellyou.mods.worldtypes.proxy.gui;

import me.icanttellyou.mods.worldtypes.proxy.MinecraftProxy;
import net.minecraft.src.*;
import overrideapi.utils.Reflection;
import overrideapi.utils.gui.GuiScreenOverride;

import java.lang.reflect.Field;
import java.util.Random;

@GuiScreenOverride(GuiCreateWorldAether.class)
public class GuiCreateWorldAetherProxy extends GuiCreateWorldAether {
    private final MinecraftProxy minecraftProxy = new MinecraftProxy();

    private static final Field field_22131_a = Reflection.publicField(Reflection.findField(GuiCreateWorld.class, "a", "field_22131_a"));
    private static final Field createClicked = Reflection.publicField(Reflection.findField(GuiCreateWorld.class, "m", "createClicked"));
    private static final Field textboxSeed = Reflection.publicField(Reflection.findField(GuiCreateWorld.class, "j", "textboxSeed"));
    private static final Field folderName = Reflection.publicField(Reflection.findField(GuiCreateWorld.class, "l", "folderName"));
    private static final Field textboxWorldName = Reflection.publicField(Reflection.findField(GuiCreateWorld.class, "i", "textboxWorldName"));

    public GuiCreateWorldAetherProxy() throws IllegalAccessException {
        super(getField_22131_a((GuiCreateWorld) ModLoader.getMinecraftInstance().currentScreen), (int)Reflection.publicField(Reflection.findField(GuiCreateWorldAether.class, "musicID")).get(ModLoader.getMinecraftInstance().currentScreen));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton1) {
        try {
            if (guiButton1.enabled) {
                if (guiButton1.id == 0) {
                    this.mc.displayGuiScreen(null);
                    if (createClicked.getBoolean(this)) {
                        return;
                    }

                    createClicked.setBoolean(this, true);
                    long j2 = (new Random()).nextLong();
                    String string4 = ((GuiTextField) textboxSeed.get(this)).getText();
                    if (!MathHelper.stringNullOrLengthZero(string4)) {
                        try {
                            long j5 = Long.parseLong(string4);
                            if (j5 != 0L) {
                                j2 = j5;
                            }
                        } catch (NumberFormatException numberFormatException7) {
                            j2 = string4.hashCode();
                        }
                    }

                    this.mc.playerController = new PlayerControllerSP(this.mc);
                    this.minecraftProxy.startWorld(folderName.get(this).toString(), ((GuiTextField) textboxWorldName.get(this)).getText(), j2);
                    this.mc.displayGuiScreen(null);
                } else {
                    super.actionPerformed(guiButton1);
                }

            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static GuiScreen getField_22131_a(GuiCreateWorld guicreateworld) {
        try {
            return (GuiScreen) field_22131_a.get(guicreateworld);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
