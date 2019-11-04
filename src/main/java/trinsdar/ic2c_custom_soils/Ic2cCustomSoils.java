package trinsdar.ic2c_custom_soils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ic2.core.IC2;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Ic2cCustomSoils.MODID, name = Ic2cCustomSoils.NAME, version = Ic2cCustomSoils.VERSION, dependencies = Ic2cCustomSoils.DEPENDS)
public class Ic2cCustomSoils {
    public static final String MODID = "ic2c_custom_soils";
    public static final String NAME = "Ic2c Custom Soils";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDS = "required-after:ic2;required-after:ic2-classic-spmod";

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        SoilJsonLoader.load(getObject(new File(IC2.configFolder, "customSoils.json")));
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID))
        {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
        }
    }

    private JsonObject getObject(File file) {
        try {
            if (file.exists()) {
                JsonElement obj = IC2.parser.parse(Files.toString(file, Charsets.UTF_8));
                if (obj != null && obj.isJsonObject()) {
                    return obj.getAsJsonObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonObject();
    }
}
