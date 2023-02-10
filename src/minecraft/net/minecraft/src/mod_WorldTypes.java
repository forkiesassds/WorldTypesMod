package net.minecraft.src;

import me.icanttellyou.mods.worldtypes.proxy.gui.GuiCreateWorldAetherProxy;
import me.icanttellyou.mods.worldtypes.proxy.gui.GuiSelectWorldAetherProxy;
import me.icanttellyou.mods.worldtypes.proxy.world.DimensionOverworldProxy;
import me.icanttellyou.mods.worldtypes.proxy.gui.GuiCreateWorldProxy;
import me.icanttellyou.mods.worldtypes.proxy.gui.GuiSelectWorldProxy;
import me.icanttellyou.mods.worldtypes.proxy.world.SaveConverterMcRegionProxy;
import net.minecraft.client.Minecraft;
import overrideapi.OverrideAPI;
import overrideapi.proxy.ArrayListProxy;
import overrideapi.utils.Reflection;
import overrideapi.utils.gui.ButtonHandler;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class mod_WorldTypes extends BaseMod {
    public Minecraft mc = ModLoader.getMinecraftInstance();
    public static boolean isDevEnv;
    public static boolean unsupportedEnvironment;

    public static int generator = 0;
    public static int biomeGenerator = 0;
    public static int singleBiome = 3;
    public static List<BiomeGenBase> biomes = new ArrayList<>();
    public static EnumWorldTypes[] enumWorldTypes = EnumWorldTypes.values();
    public static EnumBiomeGenerators[] enumBiomeGenerators = EnumBiomeGenerators.values();
    @Override
    public String Version() {
        return "1.0";
    }

    @Override
    public void ModsLoaded() {
        DimensionBase.list.remove(0);
        new DimensionOverworldProxy();
        try {
            Field field = Minecraft.class.getDeclaredField(isDevEnv ? "saveLoader" : "aa");
            field.setAccessible(true);
            field.set(mc, new SaveConverterMcRegionProxy(new File(Minecraft.getMinecraftDir(), "saves")));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to override save handler!", e);
        }
        OverrideAPI.registerButtonHandler(new InjectWorldCreateScreen());
        OverrideAPI.overrideGuiScreen(GuiSelectWorldProxy.class);
        OverrideAPI.overrideGuiScreen(GuiCreateWorldProxy.class);
        OverrideAPI.overrideGuiScreen(GuiSelectWorldAetherProxy.class);
        OverrideAPI.overrideGuiScreen(GuiCreateWorldAetherProxy.class);
        Field[] fields = BiomeGenBase.class.getFields();
        for(Field f : fields) {
            try {
                if (f.getType().equals(BiomeGenBase.class)) biomes.add((BiomeGenBase) f.get(BiomeGenBase.class));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to get biomes!", e);
            }
        }

        //try to get biomeapi biomes
        try {
            List<BiomeGenBase> biomes = (List<BiomeGenBase>)BiomeGenBase.class.getField("registeredBiomes").get(BiomeGenBase.class);
            for (BiomeGenBase biome : biomes) {
                if (!biomes.contains(biome)) biomes.add(biome);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }

    public static class InjectWorldCreateScreen implements ButtonHandler {
        public Minecraft mc = ModLoader.getMinecraftInstance();
        private int id;
        private int id2;
        private int id3;

        @Override
        public void initGui(GuiScreen guiscreen, List<GuiButton> customButtons) {
            if (guiscreen instanceof GuiCreateWorld) {
                generator = 0;
                biomeGenerator = 0;
                singleBiome = 3;
                customButtons.get(0).yPosition += 16;
                customButtons.get(1).yPosition += 16;
                customButtons.add(new GuiButton(id = OverrideAPI.getUniqueButtonID(), guiscreen.width / 2 - 100, guiscreen.height / 4 + 96 + 6, 67, 20, enumWorldTypes[generator].displayName));
                customButtons.add(new GuiButton(id2 = OverrideAPI.getUniqueButtonID(), guiscreen.width / 2 - 100 + 67, guiscreen.height / 4 + 96 + 6, 67, 20, enumBiomeGenerators[biomeGenerator].displayName));
                customButtons.add(new GuiButton(id3 = OverrideAPI.getUniqueButtonID(), guiscreen.width / 2 - 100 + 134, guiscreen.height / 4 + 96 + 6, 67, 20, biomes.get(singleBiome).biomeName));
                for (GuiButton button : customButtons) {
                    if (button.id == id3) {
                        button.enabled = false;
                    }
                }
            }
        }

        @Override
        public void actionPerformed(GuiScreen guiscreen, GuiButton guibutton) {
            if (guiscreen instanceof GuiCreateWorld) {
                if(guibutton.enabled && guibutton.enabled2) {
                    if (guibutton.id == id) {
                        generator = (generator + 1) % enumWorldTypes.length;
                        guibutton.displayString = enumWorldTypes[generator].displayName;
                    }

                    if (guibutton.id == id2) {
                        biomeGenerator = (biomeGenerator + 1) % enumBiomeGenerators.length;
                        guibutton.displayString = enumBiomeGenerators[biomeGenerator].displayName;
                    }

                    if (guibutton.id == id3) {
                        singleBiome = (singleBiome + 1) % biomes.toArray().length;
                        guibutton.displayString = biomes.get(singleBiome).biomeName;
                    }

                    try {
                        List<GuiButton> controlList = (List<GuiButton>) Reflection.findField(GuiScreen.class, new String[]{"e", "controlList"}).get(OverrideAPI.getMinecraftInstance().currentScreen);
                        List<GuiButton> proxy = new ArrayListProxy<GuiButton>();
                        proxy.addAll(controlList);
                        for (GuiButton button : proxy) {
                            if (button.id == id2 || button.id == id3) {
                                if (enumWorldTypes[generator] == EnumWorldTypes.ALPHA) button.enabled = false;
                                else button.enabled = true;
                                if (button.id == id3) {
                                    if (enumBiomeGenerators[biomeGenerator] == EnumBiomeGenerators.SINGLE) button.enabled = true;
                                    else button.enabled = false;
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("dafuq? failed to get buttons?", e);
                    }
                }
            }
        }
    }

    public enum EnumWorldTypes {
        //to avoid stuff from going baaaad, put ones that rely on vanilla code before the custom ones.
        NORMAL("default", "Default"),
        SKY("sky", "Sky"),
        FLAT("flat", "Flat"),
        TWOD("2d", "2D Perlin"),
        ALPHA("alpha", "Alpha");

        public final String worldType;
        public final String displayName;
        EnumWorldTypes(String s, String s2) {
            worldType = s;
            displayName = s2;
        }
    }

    public enum EnumBiomeGenerators {
        NORMAL("default", "Default"),
        SINGLE("single", "Single Biome");

        public final String biomeGenerator;
        public final String displayName;

        EnumBiomeGenerators(String s, String s2) {
            biomeGenerator = s;
            displayName = s2;
        }
    }

    static {
        try {
            Class.forName("net.minecraft.src.Block");
            unsupportedEnvironment = false;
            isDevEnv = true;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("uu");
                unsupportedEnvironment = false;
            } catch (ClassNotFoundException ex) {
                System.err.println("Unsupported environment! Minecraft is either reobfuscated or you are using non-MCP mappings!");
                unsupportedEnvironment = true;
            }
            isDevEnv = false;
        }
    }
}
