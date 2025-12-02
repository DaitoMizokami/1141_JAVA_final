package com.example.borrowservice.dto;

/**
 * DTO for weather information from OpenWeatherMap API
 * Used to demonstrate external API integration via WebClient
 */
public class WeatherDTO {
    private String city;
    private String country;
    private String description;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private double windSpeed;
    private String icon;
    private String message;

    // Default constructor
    public WeatherDTO() {}

    // Constructor with all fields
    public WeatherDTO(String city, String country, String description, 
                      double temperature, double feelsLike, int humidity, 
                      double windSpeed, String icon, String message) {
        this.city = city;
        this.country = country;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.icon = icon;
        this.message = message;
    }

    // Getters and Setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Generate library-appropriate message based on weather
    public static String generateLibraryMessage(String description, double temperature) {
        if (description.toLowerCase().contains("rain") || description.toLowerCase().contains("drizzle")) {
            return "下雨天適合在圖書館享受閱讀時光";
        } else if (description.toLowerCase().contains("snow")) {
            return "下雪天來圖書館暖暖地閱讀吧";
        } else if (temperature > 30) {
            return "炎熱的天氣，來圖書館吹冷氣看書";
        } else if (temperature < 10) {
            return "天氣寒冷，圖書館是閱讀的好去處";
        } else if (description.toLowerCase().contains("clear") || description.toLowerCase().contains("sunny")) {
            return "晴朗的日子，歡迎來圖書館探索新書";
        } else if (description.toLowerCase().contains("cloud")) {
            return "多雲的天氣，適合靜靜地閱讀";
        } else {
            return "歡迎來圖書館借閱書籍";
        }
    }
}
