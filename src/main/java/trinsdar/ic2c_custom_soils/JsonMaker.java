package trinsdar.ic2c_custom_soils;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileWriter;

public class JsonMaker {
    public static void init(FMLPreInitializationEvent event){
        try {
            JsonObject object = new JsonObject();
            File file = new File(event.getModConfigurationDirectory(), "ic2/customSoilsExample.json");
            file.createNewFile();
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.beginObject();
            writer.setIndent("  ");
                writer.name("crops");
                writer.beginArray();
                    writer.beginObject();
                        writer.name("name").value("Ferru");
                        writer.name("owner").value("ic2");
                        writer.name("id").value("ferru");
                        writer.name("discovered").value("Alblaka");
                        writer.name("dropchance").value(0.367545945F);
                        writer.name("optimalstage").value(4);
                        writer.name("totalstages").value(4);
                        writer.name("attributes");
                        writer.beginArray();
                            writer.value("Gray");
                            writer.value("Leaves");
                            writer.value("Metal");
                        writer.endArray();
                        writer.name("props");
                        writer.beginObject();
                            writer.name("tier").value(6);
                            writer.name("chemistry").value(2);
                            writer.name("consumable").value(0);
                            writer.name("defensive").value(0);
                            writer.name("colorful").value(1);
                            writer.name("weed").value(0);
                        writer.endObject();
                        writer.name("display");
                        writer.beginObject();
                            writer.name("name").value("thermalfoundation:material");
                            writer.name("meta").value(0);
                        writer.endObject();
                        writer.name("defaultStage");
                        writer.beginObject();
                            writer.name("points").value(800);
                            writer.name("redstoneStrength").value(0);
                            writer.name("resetStage").value(2);
                            writer.name("lightLevel").value("0");
                        writer.endObject();
                        writer.name("stages");
                        writer.beginArray();
                            writer.beginObject();
                                writer.name("index").value(1);
                                writer.name("parentIndex").value(-1);
                                writer.name("textures");
                                writer.beginArray();
                                    writer.value("ic2c_crop_overrides:crop_metal_stage_1");
                                writer.endArray();
                            writer.endObject();
                            writer.beginObject();
                                writer.name("index").value(2);
                                writer.name("textures");
                                writer.beginArray();
                                    writer.value("ic2c_crop_overrides:crop_metal_stage_2");
                                writer.endArray();
                            writer.endObject();
                            writer.beginObject();
                                writer.name("index").value(3);
                                writer.name("textures");
                                writer.beginArray();
                                    writer.value("ic2c_crop_overrides:crop_metal_stage_3");
                                writer.endArray();
                                writer.name("points").value(2000);
                                writer.name("requirements");
                                writer.beginObject();
                                    writer.name("block");
                                    writer.beginArray();
                                        writer.beginObject();
                                            writer.name("ore").value(true);
                                            writer.name("name").value("blockCrystalEmpoweredEnori");
                                        writer.endObject();
                                    writer.endArray();
                                writer.endObject();
                            writer.endObject();
                            writer.beginObject();
                                writer.name("index").value(4);
                                writer.name("textures");
                                writer.beginArray();
                                    writer.value("ic2c_crop_overrides:crop_ferru");
                                writer.endArray();
                                writer.name("drop");
                                writer.beginObject();
                                    writer.name("name").value("thermalfoundation:material");
                                    writer.name("count").value(1);
                                    writer.name("meta").value(0);
                                writer.endObject();
                            writer.endObject();
                        writer.endArray();
                    writer.endObject();
                writer.endArray();
            writer.endObject();
            writer.close();
            for(int i = 0; i < 8; i ++){

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
