package trinsdar.ic2c_custom_soils;

import net.minecraftforge.common.config.Config;

@Config( modid = Ic2cCustomSoils.MODID, name = "ic2/ic2c_custom_soils")
public class Ic2cCustomSoilsConfig {

    @Config.Comment("Set this to true to generate an example json in the config/ic2 folder. The file name is customSoilsExample.json")
    @Config.RequiresMcRestart
    public static boolean generateExampleJson = false;
}
