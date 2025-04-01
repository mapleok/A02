package com.example.farmsim.utils;

import com.example.farmsim.model.entity.Crop;
import com.example.farmsim.model.entity.Environment;

public class CropCalculator {
    public static double calculateGrowth(Crop crop, Environment env) {
        return crop.getGrowthRate()
                * (1 + env.getTemperature() * crop.getTempWeight())
                * (1 + env.getSoilFertility() * crop.getSoilWeight())
                * (1 + env.getPrecipitation() * crop.getWaterWeight());
    }
}