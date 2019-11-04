package trinsdar.ic2c_custom_soils;

import ic2.api.classic.crops.ICropSoil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoilCustom implements ICropSoil {
    int nutrientEffect;
    int humidityEffect;
    public SoilCustom(int nutrientEffect, int humidityEffect){
        this.nutrientEffect = nutrientEffect;
        this.humidityEffect = humidityEffect;
    }

    @Override
    public int getNutrientEffect(World world, BlockPos blockPos) {
        return nutrientEffect;
    }

    @Override
    public int getNutrientEffect(IBlockState iBlockState) {
        return nutrientEffect;
    }

    @Override
    public int getHumidityEffect(World world, BlockPos blockPos) {
        return humidityEffect;
    }

    @Override
    public int getHumidityEffect(IBlockState iBlockState) {
        return humidityEffect;
    }
}
