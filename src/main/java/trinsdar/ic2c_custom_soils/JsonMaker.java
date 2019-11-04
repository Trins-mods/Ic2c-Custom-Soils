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
                writer.name("soils");
                writer.beginArray();
                    writer.beginObject();
                        writer.name("blockName").value("minecraft:stone");
                        writer.name("blockMeta").value(1);
                        writer.name("nutrientEffect").value(2);
                        writer.name("humidityEffect").value(1);
                    writer.endObject();
                writer.endArray();
                writer.name("farmlands");
                writer.beginArray();
                    writer.name("blockName").value("minecraft:stone");
                    writer.name("blockMeta").value(1);
                    writer.name("nutrientEffect").value(3);
                    writer.name("humidityEffect").value(2);
                writer.endArray();
            writer.endObject();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
