package com.hta2405.unite.service;

import com.hta2405.unite.dto.WeatherResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class WeatherService {
    private final String apiKey;
    private final String URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String WEATHER_CITY;
    private WeatherResponseDTO cachedWeather; // 캐싱된 날씨 데이터

    public WeatherService(@Value("${weather.apiKey}") String apiKey,
                          @Value("${weather.city}")
                          String WEATHER_CITY) {
        this.apiKey = apiKey;
        this.WEATHER_CITY = WEATHER_CITY;
    }

    public WeatherResponseDTO fetchWeatherData() {
        URI uri = URI.create(URL + "?q=" + WEATHER_CITY + "&units=metric&appid=" + apiKey);

        RestTemplate restTemplate = new RestTemplate();
        log.info("날씨정보 가져옴");
        return restTemplate.getForObject(uri, WeatherResponseDTO.class);
    }

    // 캐싱된 데이터 반환
    public WeatherResponseDTO getCachedWeather() {
        if (cachedWeather == null) {
            // 초기 서버 부팅 시 캐싱된 데이터가 없으면 API 호출
            cachedWeather = fetchWeatherData();
        }
        return cachedWeather;
    }

    // 1시간마다 데이터 갱신
    @Scheduled(cron = "0 0 * * * ?")
    public void updateWeatherCache() {
        try {
            cachedWeather = fetchWeatherData();
            System.out.println("Weather cache updated: " + cachedWeather);
        } catch (Exception e) {
            System.err.println("Failed to update weather data: " + e.getMessage());
        }
    }
}