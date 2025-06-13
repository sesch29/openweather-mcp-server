package info.schefczyk.mcp.mcp_weather_server;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${secret.openweathermap.apikey}")
    private String apiKey;
    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService() {
        restClient = RestClient.builder()
                .defaultHeader("Accept", "application/geo+json")
                .build();
    }

    @Tool(description = "Get weather forecast for a specific latitude/longitude (format like '49.640556' and '8.278889')")
    public String getWeatherForecastByLocation(
            @ToolParam(description = "Latitude in form like '51.5073219'") String latitude,   // Latitude coordinate
            @ToolParam(description = "Longitude in form like '0.1276474'") String longitude   // Longitude coordinate
    ) {
        logger.debug("getWeatherForecastByLocation: latitude={}, longitude={}", latitude, longitude);
        var uri = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        var response = restClient
                .get()
                .uri(uri)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

        return response.getBody();
    }

    @Tool(description = "Get coordinates by location name. Use comma separation in scheme '{city name},{state code},{country code}'")
    public String getLocation(
            @ToolParam(description = "Search string in scheme '{city name},{state code},{country code}'. Limited to 1.") String searchQuery
    ) {
        logger.debug("getLocation: searchQuery={}", searchQuery);
        var response = restClient
                .get()
                .uri("https://api.openweathermap.org/geo/1.0/direct?q={searchQuery}&limit=1&appid={appId}", searchQuery, apiKey)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
        return response.getBody();
    }

    @Tool(description = "Search coordinates by location name. Use comma separation in scheme '{city name},{state code},{country code}'")
    public String searchLocation(
            @ToolParam(description = "Search string in scheme '{city name},{state code},{country code}'. Limited to 10.") String searchQuery
    ) {
        logger.debug("searchLocation: searchQuery={}", searchQuery);
        var response = restClient
                .get()
                .uri("https://api.openweathermap.org/geo/1.0/direct?q={searchQuery}&limit=10&appid={appId}", searchQuery, apiKey)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
        return response.getBody();
    }
}
