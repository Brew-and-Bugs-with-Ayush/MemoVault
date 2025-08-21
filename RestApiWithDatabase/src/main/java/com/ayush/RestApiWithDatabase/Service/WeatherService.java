package com.ayush.RestApiWithDatabase.Service;

import com.ayush.RestApiWithDatabase.api.response.WeatherResponse;
import com.ayush.RestApiWithDatabase.cache.AppCache;
import com.ayush.RestApiWithDatabase.constants.Placeholder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apikey;

    private final RestTemplate restTemplate;

    private final AppCache appCache;
    private final RedisService redisService;


    @Autowired
    public WeatherService(RestTemplate restTemplate, AppCache appCache, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.appCache = appCache;
        this.redisService = redisService;
    }

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null){
            return weatherResponse;
        }
        else {
            String finalAPI = appCache.getValue(AppCache.Keys.valueOf(AppCache.Keys.WEATHER_API.toString())).replace(Placeholder.CITY , city).replace(Placeholder.API_KEY , apikey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI , HttpMethod.POST , null , WeatherResponse.class);
            WeatherResponse body = response.getBody();

            if (body != null){
                redisService.set("weather_of_" + city, body , 300L);
            }
            return body;
        }
    }
}
