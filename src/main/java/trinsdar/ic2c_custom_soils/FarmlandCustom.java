package trinsdar.ic2c_custom_soils;

import ic2.api.classic.crops.IFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FarmlandCustom implements IFarmland {
    int humidity;
    int nutrients;
    public FarmlandCustom(int humidity, int nutrients){
        this.humidity = humidity;
        this.nutrients = nutrients;
    }

    @Override
    public int getHumidity(World world, BlockPos blockPos) {
        return humidity;
    }

    @Override
    public int getHumidity(IBlockState iBlockState) {
        return humidity;
    }

    @Override
    public int getNutrients(World world, BlockPos blockPos) {
        return nutrients;
    }

    @Override
    public int getNutrients(IBlockState iBlockState) {
        return nutrients;
    }
}
