package me.icanttellyou.mods.worldtypes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Map;

public class GuiMoreWorldGenSettings extends GuiScreen {
    GuiScreen parent;
    HashMap<String, Object> buttonList;
    Minecraft mc = ModLoader.getMinecraftInstance();

    public GuiMoreWorldGenSettings(GuiScreen guiScreen1) {
        parent = guiScreen1;
//        buttonList = ChunkProviderCustom.terrainGenerators.get(mod_WorldTypes.generator - ChunkProviderCustom.chunkProviders.toArray().length).getSettings().getOptions();
        buttonList = new HashMap<String, Object>();
        buttonList.put("aaaa dummy", 1);
        buttonList.put("aaaa dummy2", 12);
        buttonList.put("aaaa dum434my", 14);
        buttonList.put("aaaa d45435ummy", 165546);
        buttonList.put("aaaa d12ummy", 1655443);
    }

    @Override
    public void initGui() {
        StringTranslate stringTranslate1 = StringTranslate.getInstance();
//        for(int i5 = 0; i5 < i4; ++i5) {
//            EnumOptions enumOptions6 = enumOptions3[i5];
//            if(!enumOptions6.getEnumFloat()) {
//
//            } else {
//                this.controlList.add(new GuiSlider(enumOptions6.returnEnumOrdinal(), this.width / 2 - 155 + i2 % 2 * 160, this.height / 6 + 24 * (i2 >> 1), enumOptions6, this.options.getKeyBinding(enumOptions6), this.options.getOptionFloatValue(enumOptions6)));
//            }
//
//
//        }
        int i2 = 0;
        for (Map.Entry<String, Object> entry : buttonList.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String displayString = key + ": " + value;
            this.controlList.add(new GuiSmallButton(i2, this.width / 2 - 155 + i2 % 2 * 160, this.height / 6 + 24 * (i2 >> 1), displayString));
            ++i2;
        }
        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, stringTranslate1.translateKey("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton1) {
        if(guiButton1.enabled) {
            if(guiButton1.id == 200) {
//                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parent);
            } else {
                System.out.println(guiButton1.id);
            }
        }
    }
}
