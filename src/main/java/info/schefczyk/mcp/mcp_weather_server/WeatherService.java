package info.schefczyk.mcp.mcp_weather_server;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * API-Documentation:
 * <a href="https://openweathermap.org/api#maps">openweathermap.org/api#maps</a>
 * <a href="https://openweathermap.org/api/geocoding-api">openweathermap.org/api/geocoding-api</a>
 * <a href="https://openweathermap.org/api/air-pollution">openweathermap.org/api/air-pollution</a>
 */
@Service
public class WeatherService {

    @Value("${my.openweathermap.apikey}")
    private String apiKey;
    private final RestClient restClient;

    public WeatherService() {
        restClient = RestClient.builder()
                .defaultHeader("Accept", "application/geo+json")
                .build();
    }

    @PostConstruct
    public void init() {
        System.out.println("WeatherService.init: appId=" + apiKey);
    }

    @Tool(description = "Get weather forecast for a specific latitude/longitude (format like '49.640556' and '8.278889')")
    public String getWeatherForecastByLocation(
            @ToolParam(description = "Latitude in form like '51.5073219'") String latitude,   // Latitude coordinate
            @ToolParam(description = "Longitude in form like '0.1276474'") String longitude   // Longitude coordinate
    ) {
        System.out.println("WeatherService.getWeatherForecastByLocation: appId=" + apiKey);
        var uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        System.out.println("Computed URI: " + uri);

        var response = restClient
                .get()
                .uri(uri)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

//        var response = restClient
//                .get()
//                .uri("https://api.openweathermap.org/data/2.5/weather?lat={latitude}&lon={longitude}&appid={appId}", latitude, longitude, appId)
//                .accept(APPLICATION_JSON)
//                .retrieve()
//                .toEntity(String.class);
        System.out.println(response);
        return response.getBody();
    }

    /**
     * Coordinates by location name
     */
    @Tool(description = "Coordinates by location name. Use comma separation in scheme '{city name},{state code},{country code}'")
    public String getLocation(
            @ToolParam(description = "Search string in scheme '{city name},{state code},{country code}'") String searchQuery
    ) {
        var response = restClient
                .get()
                .uri("https://api.openweathermap.org/geo/1.0/direct?q={searchQuery}&limit=1&appid={appId}", searchQuery, apiKey)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
        System.out.println(response);
        System.out.println(response.getBody());
        return response.getBody();
    }

    // TODO more endpoints, see https://openweathermap.org/api/air-pollution
}