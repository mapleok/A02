package com.example.farmsim.model.entity;

public class WeatherModel {
    private static final double[] SEASONAL_TEMP_RANGE = {-10, 35}; // 温度范围
    private static final double[] SEASONAL_RAIN_RANGE = {0, 200};  // 降水量范围

    public static Environment generateWeather(String season) {
        Environment env = new Environment();
        switch (season) {
            case "spring":
                env.setTemperature(10 + Math.random() * 10);
                env.setPrecipitation(50 + Math.random() * 50);
                break;
            case "summer":
                env.setTemperature(25 + Math.random() * 10);
                env.setPrecipitation(100 + Math.random() * 100);
                break;
            case "autumn":
                env.setTemperature(15 + Math.random() * 10);
                env.setPrecipitation(50 + Math.random() * 50);
                break;
            case "winter":
                env.setTemperature(-5 + Math.random() * 10);
                env.setPrecipitation(20 + Math.random() * 30);
                break;
        }
        return env;
    }
}