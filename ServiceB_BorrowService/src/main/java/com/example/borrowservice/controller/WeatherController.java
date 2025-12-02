package com.example.borrowservice.controller;

import com.example.borrowservice.dto.WeatherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

/**
 * Controller for external Weather API integration
 * Demonstrates WebClient usage with Free Online API (OpenWeatherMap)
 * 
 * This is a BONUS feature for the final project:
 * "Use WebClient call other Free Online API (bonus point)"
 */
@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://localhost:8081"}, allowCredentials = "true")
public class WeatherController {

    private final WebClient weatherWebClient;
    
    @Value("${weather.api.key:demo}")
    private String apiKey;

    // Default city for the library (Taoyuan, Taiwan - Yuan Ze University area)
    private static final String DEFAULT_CITY = "Taoyuan";
    private static final String DEFAULT_COUNTRY_CODE = "TW";

    public WeatherController() {
        // Create a separate WebClient for OpenWeatherMap API
        this.weatherWebClient = WebClient.builder()
                .baseUrl("https://api.openweathermap.org/data/2.5")
                .build();
    }

    /**
     * Get current weather for the library location (default: Taoyuan, Taiwan)
     * GET /api/weather
     */
    @GetMapping
    public ResponseEntity<WeatherDTO> getLibraryWeather() {
        return getWeatherByCity(DEFAULT_CITY, DEFAULT_COUNTRY_CODE);
    }

    /**
     * Get current weather for a specific city
     * GET /api/weather/{city}
     * 
     * @param city City name (e.g., "Tokyo", "Taipei", "Taoyuan")
     */
    @GetMapping("/{city}")
    public ResponseEntity<WeatherDTO> getWeatherByCity(
            @PathVariable String city,
            @RequestParam(required = false, defaultValue = "") String countryCode) {
        
        try {
            // Build query parameter: "city" or "city,countryCode"
            String query = countryCode.isEmpty() ? city : city + "," + countryCode;
            
            // Call OpenWeatherMap API using WebClient
            Map<String, Object> response = weatherWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("q", query)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")  // Celsius
                            .queryParam("lang", "ja")       // Japanese descriptions
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response == null) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(createErrorResponse("Weather service unavailable"));
            }

            // Parse response
            WeatherDTO weatherDTO = parseWeatherResponse(response);
            return ResponseEntity.ok(weatherDTO);

        } catch (WebClientResponseException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("City not found: " + city));
        } catch (WebClientResponseException.Unauthorized ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid API key. Please configure weather.api.key in application.properties"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching weather: " + ex.getMessage()));
        }
    }

    /**
     * Get weather for multiple cities (useful for comparing weather)
     * GET /api/weather/cities?names=Tokyo,Taipei,Taoyuan
     */
    @GetMapping("/cities")
    public ResponseEntity<List<WeatherDTO>> getWeatherForCities(@RequestParam String names) {
        String[] cityNames = names.split(",");
        List<WeatherDTO> weatherList = java.util.Arrays.stream(cityNames)
                .map(String::trim)
                .map(city -> {
                    try {
                        ResponseEntity<WeatherDTO> response = getWeatherByCity(city, "");
                        return response.getBody();
                    } catch (Exception e) {
                        return createErrorResponse("Error fetching weather for " + city);
                    }
                })
                .toList();
        
        return ResponseEntity.ok(weatherList);
    }

    /**
     * Parse OpenWeatherMap API response into WeatherDTO
     */
    @SuppressWarnings("unchecked")
    private WeatherDTO parseWeatherResponse(Map<String, Object> response) {
        WeatherDTO dto = new WeatherDTO();

        // Get city name and country
        dto.setCity((String) response.get("name"));
        Map<String, Object> sys = (Map<String, Object>) response.get("sys");
        if (sys != null) {
            dto.setCountry((String) sys.get("country"));
        }

        // Get weather description and icon
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
        if (weatherList != null && !weatherList.isEmpty()) {
            Map<String, Object> weather = weatherList.get(0);
            dto.setDescription((String) weather.get("description"));
            dto.setIcon((String) weather.get("icon"));
        }

        // Get temperature data
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        if (main != null) {
            dto.setTemperature(((Number) main.get("temp")).doubleValue());
            dto.setFeelsLike(((Number) main.get("feels_like")).doubleValue());
            dto.setHumidity(((Number) main.get("humidity")).intValue());
        }

        // Get wind data
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        if (wind != null) {
            dto.setWindSpeed(((Number) wind.get("speed")).doubleValue());
        }

        // Generate library-appropriate message
        dto.setMessage(WeatherDTO.generateLibraryMessage(
                dto.getDescription() != null ? dto.getDescription() : "",
                dto.getTemperature()));

        return dto;
    }

    /**
     * Create error response DTO
     */
    private WeatherDTO createErrorResponse(String errorMessage) {
        WeatherDTO dto = new WeatherDTO();
        dto.setMessage(errorMessage);
        return dto;
    }
}
