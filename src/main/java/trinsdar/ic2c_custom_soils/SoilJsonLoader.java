package trinsdar.ic2c_custom_soils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ic2.core.block.crop.Ic2Crops;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.util.Iterator;

public class SoilJsonLoader {
    public static void load(JsonObject obj) {
        boolean reload = false;
        Iterator var2;
        JsonElement element;
        JsonObject subObj;
        if (obj.has("soils")) {
            var2 = obj.getAsJsonArray("soils").iterator();

            while(var2.hasNext()) {
                element = (JsonElement)var2.next();

                try {
                    subObj = element.getAsJsonObject();
                    String blockName = subObj.get("blockName").getAsString();
                    int blockMeta = 0;
                    if (subObj.has("blockMeta")){
                        blockMeta = subObj.get("blockMeta").getAsInt();
                    }
                    if (blockMeta < 0){
                        continue;
                    }
                    Ic2Crops crops = Ic2Crops.instance;
                    IBlockState blockState = Block.getBlockFromName(blockName).getDefaultState();
                    if (blockMeta != 0){
                        blockState = Block.getBlockFromName(blockName).getStateFromMeta(blockMeta);
                    }
                    int humidity = subObj.get("humidity").getAsInt();
                    int nutrients = subObj.get("nutrient").getAsInt();
                    int nutrientEffect = subObj.get("nutrientEffect").getAsInt();
                    int humidityEffect = subObj.get("humidityEffect").getAsInt();
                    crops.registerCropSoil(new SoilCustom(nutrientEffect, humidityEffect), blockState);
                    crops.registerFarmland(new FarmlandCustom(humidity, nutrients), blockState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
