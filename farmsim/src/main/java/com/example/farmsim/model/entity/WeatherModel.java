package com.example.farmsim.model.entity;

public class WeatherModel {

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