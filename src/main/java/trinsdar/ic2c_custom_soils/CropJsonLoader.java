package trinsdar.ic2c_custom_soils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ic2.api.crops.CropCard;
import ic2.api.crops.CropProperties;
import ic2.api.crops.Crops;
import ic2.core.IC2;
import ic2.core.block.crop.Ic2Crops;
import ic2.core.block.crop.crops.CropCardBase;
import ic2.core.block.crop.crops.CropDynamic;
import ic2.core.block.crop.misc.CropLoader;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CropJsonLoader extends CropLoader {
    public static void load(JsonObject obj) {
        boolean reload = false;
        Iterator var2;
        JsonElement element;
        JsonObject subObj;
        if (obj.has("crops")) {
            var2 = obj.getAsJsonArray("crops").iterator();

            while(var2.hasNext()) {
                element = (JsonElement)var2.next();

                try {
                    subObj = element.getAsJsonObject();
                    CropDynamic.CropInfo info = new CropDynamic.CropInfo();
                    info.name = subObj.get("name").getAsString();
                    info.owner = subObj.get("owner").getAsString();
                    info.id = subObj.get("id").getAsString();
                    CropCardBase old = (CropCardBase) Ic2Crops.instance.getCropCard(info.owner, info.id);
                    if (old == null){
                        Ic2cCropOverrides.logger.info("Crop Owner & ID not found: [owner=" + info.owner + ", ID=" + info.id + "]");
                        continue;
                    }
                    if (subObj.has("discovered")){
                        info.discoverer = subObj.get("discovered").getAsString();
                    } else {
                        info.discoverer = old.getDiscoveredBy();
                    }
                    if (subObj.has("dropchance")){
                        info.harvestChanceMod = subObj.get("dropchance").getAsDouble();
                    } else {
                        info.harvestChanceMod = old.dropGainChance();
                    }
                    info.optimalHarvestStage = Math.max(2, subObj.get("optimalstage").getAsInt());
                    if (subObj.has("attributes")){
                        info.attributes = parseString(subObj.getAsJsonArray("attributes"));;
                    } else {
                        info.attributes = old.getAttributes();
                    }
                    if (subObj.has("props")){
                        JsonObject props = subObj.getAsJsonObject("props");
                        info.props = new CropProperties(props.get("tier").getAsInt(), props.get("chemistry").getAsInt(), props.get("consumable").getAsInt(), props.get("defensive").getAsInt(), props.get("colorful").getAsInt(), props.get("weed").getAsInt());
                    } else {
                        info.props = old.getProperties();
                    }
                    int stageCount;
                    if (subObj.has("totalstages")){
                        stageCount = subObj.get("totalstages").getAsInt();
                    } else {
                        stageCount = old.getMaxSize();
                    }


                    Int2ObjectMap<CropStage> stageMap = new Int2ObjectOpenHashMap();
                    if (subObj.has("defaultStage")) {
                        stageMap.put(-1, new CropLoader.CropStage(subObj.getAsJsonObject("defaultStage"), true));
                    }

                    CropStage stage;
                    for (JsonElement el : subObj.getAsJsonArray("stages")){
                        JsonObject test = el.getAsJsonObject();
                        stage = new CropStage(test, false);

                        int myIndex = getMyIndex(stage);
                        stageMap.put(myIndex, stage);
                    }

                    for (CropStage cropStage : stageMap.values()) {
                        cropStage.evaluateParents(stageMap);
                    }

                    for(int i = 0; i < stageCount; ++i) {
                        stage = stageMap.get(i + 1);
                        if (stage == null) {
                            Ic2cCropOverrides.logger.info("CropStage: " + (i + 1) + " is not found!");
                            return;
                        }

                        info.stages.add(stage);
                    }

                    if (IC2.platform.isRendering()) {
                        Set<ResourceLocation> textures = new ObjectLinkedOpenHashSet();

                        for (CropStage cropStage : stageMap.values()) {
                            textures.addAll(cropStage.getCropTextures());
                        }

                        Map<ResourceLocation, TextureAtlasSprite> sprites = registerTextures(textures);

                        Crops.instance.registerCropTextures(sprites);
                    }

                    if (info.owner.equals("ic2")){
                        if (info.id.equalsIgnoreCase("wheat")){
                            registerCrop(Ic2Crops.cropWheat, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("pumpkin")){
                            registerCrop(Ic2Crops.cropPumpkin, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("melon")){
                            registerCrop(Ic2Crops.cropMelon, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("dandelion")){
                            registerCrop(Ic2Crops.cropYellowFlower, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("rose")){
                            registerCrop(Ic2Crops.cropRedFlower, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("blackthorn")){
                            registerCrop(Ic2Crops.cropBlackFlower, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("tulip")){
                            registerCrop(Ic2Crops.cropPurpleFlower, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("cyazint")){
                            registerCrop(Ic2Crops.cropBlueFlower, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("daisy")){
                            registerCrop(Ic2Crops.cropWhiteFlower, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("venomilia")){
                            registerCrop(Ic2Crops.cropVenomilia, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("rainbow")){
                            registerCrop(Ic2Crops.cropRainbowFlower, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("reed")){
                            registerCrop(Ic2Crops.cropReed, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("stickreed")){
                            registerCrop(Ic2Crops.cropStickReed, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("cocoa")){
                            registerCrop(Ic2Crops.cropCocoa, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("ferru")){
                            registerCrop(Ic2Crops.cropFerru, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("malachite")){
                            registerCrop(Ic2Crops.cropMalachite, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("stannum")){
                            registerCrop(Ic2Crops.cropStannum, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("aurelia")){
                            registerCrop(Ic2Crops.cropAurelia, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("argentum")){
                            registerCrop(Ic2Crops.cropArgentum, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("lumilia")){
                            registerCrop(Ic2Crops.cropGlowstone, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("redwheat")){
                            registerCrop(Ic2Crops.cropRedwheat, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("bluewheat")){
                            registerCrop(Ic2Crops.cropBluewheat, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("netherwart")){
                            registerCrop(Ic2Crops.cropNetherWart, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("terrawart")){
                            registerCrop(Ic2Crops.cropTerraWart, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("glowshroom")){
                            registerCrop(Ic2Crops.cropGlowWart, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("coffee")){
                            registerCrop(Ic2Crops.cropCoffee, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("hops")){
                            registerCrop(Ic2Crops.cropHops, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("carrots")){
                            registerCrop(Ic2Crops.cropCarrots, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("potato")){
                            registerCrop(Ic2Crops.cropPotato, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("redmushroom")){
                            registerCrop(Ic2Crops.cropRedMushroom, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("brownmushroom")){
                            registerCrop(Ic2Crops.cropBrownMushroom, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("cacti")){
                            registerCrop(Ic2Crops.cropCacti, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("tea")){
                            registerCrop(Ic2Crops.cropTea, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("hemp")){
                            registerCrop(Ic2Crops.cropHemp, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("beetroots")){
                            registerCrop(Ic2Crops.cropBeetRoot, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("oaksapling")){
                            registerCrop(Ic2Crops.cropOakSapling, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("sprucesapling")){
                            registerCrop(Ic2Crops.cropSpruceSapling, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("birchsapling")){
                            registerCrop(Ic2Crops.cropBirchSapling, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("junglesapling")){
                            registerCrop(Ic2Crops.cropJungleSapling, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("acaciasapling")){
                            registerCrop(Ic2Crops.cropAcaciaSapling, info, subObj);
                            reload = true;
                        }
                        if (info.id.equalsIgnoreCase("dark_oaksapling")){
                            registerCrop(Ic2Crops.cropDarkOakSapling, info, subObj);
                            reload = true;
                        }
                    } else {
                        Ic2cCropOverrides.logger.info("Sorry, Crop with Owner & ID[owner=" + info.owner + ", ID=" + info.id + "]  not supported at this time.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (reload) {
            IC2.platform.reloadLang();
        }

    }

    private static void registerCrop(CropCard card, CropDynamic.CropInfo info, JsonObject subObj){
        card = new CropDynamic(info);
        Ic2Crops crop = Ic2Crops.instance;
        crop.registerCrop(card);
        if (subObj.has("display")) {
            ItemStack stack = createStack(subObj.getAsJsonObject("display"));
            if (!stack.isEmpty()) {
                crop.registerCropDisplayItem(card, stack);
            }
        }
    }

    private static String[] parseString(JsonArray array) {
        String[] result = new String[array.size()];

        for(int i = 0; i < result.length; ++i) {
            result[i] = array.get(i).getAsString();
        }

        return result;
    }

    @SideOnly(Side.CLIENT)
    private static Map<ResourceLocation, TextureAtlasSprite> registerTextures(Set<ResourceLocation> locations) {
        Map<ResourceLocation, TextureAtlasSprite> sprites = new Object2ObjectLinkedOpenHashMap();

        for (ResourceLocation location : locations) {
            sprites.put(location, Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(location));
        }

        return sprites;
    }

    private static int getMyIndex(CropStage stage){
        Field myIndex = null;
        try {
            myIndex = CropStage.class.getDeclaredField("myIndex");
        } catch (SecurityException e) {
            Ic2cCropOverrides.logger.info("CropStage security deployed");
        } catch (NoSuchFieldException e) {
            Ic2cCropOverrides.logger.info("Trying to access CropStage value has failed");
        }
        if (myIndex != null){
            myIndex.setAccessible(true);
        }
        int copy = 0;
        try {
            if (myIndex != null) {
                copy = ((int) myIndex.get(stage));
            }
        } catch (IllegalArgumentException e) {
            Ic2cCropOverrides.logger.info("Accessed CropStage class but field getter failed");
        } catch (IllegalAccessException e) {
            Ic2cCropOverrides.logger.info("Accessed CropStage class but access denied");
        }
        return copy;
    }

    public static class CropStageOverride extends CropLoader.CropStage{
        int myIndex;

        public CropStageOverride(JsonObject obj, boolean defaultStage) {
            super(obj, defaultStage);
            this.myIndex = defaultStage ? -1 : MathHelper.clamp(obj.get("index").getAsInt(), 0, 2147483647);
        }
    }
}
