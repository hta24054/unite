package com.hta2405.unite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.dto.WeatherResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WeatherService {
    private final String apiKey;
    private final String URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String WEATHER_CITY;
    private static final String REDIS_KEY = "weather:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper; // JSON 직렬화/역직렬화

    public WeatherService(@Value("${weather.apiKey}") String apiKey,
                          @Value("${weather.city}")
                          String WEATHER_CITY, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.WEATHER_CITY = WEATHER_CITY;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherResponseDTO fetchWeatherData() {
        URI uri = URI.create(URL + "?q=" + WEATHER_CITY + "&units=metric&appid=" + apiKey);

        RestTemplate restTemplate = new RestTemplate();
        log.info("날씨정보 가져옴");
        return restTemplate.getForObject(uri, WeatherResponseDTO.class);
    }

    public WeatherResponseDTO getCachedWeather() {
        try {
            //캐싱된 데이터가 있으면 바로 반환
            String cachedData = redisTemplate.opsForValue().get(REDIS_KEY);
            if (cachedData != null) {
                try {
                    log.info("Redis cache hit : {}", REDIS_KEY);
                    return objectMapper.readValue(cachedData, WeatherResponseDTO.class);
                } catch (JsonProcessingException e) {
                    log.error("Redis cache error: {}", REDIS_KEY, e);
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            log.error("Redis 서버 오류", e);
        }

        //캐싱 데이터 없으므로 API 호출
        log.info("Redis cache miss : {}", REDIS_KEY);
        WeatherResponseDTO weatherResponseDTO = fetchWeatherData();
        try {
            redisTemplate.opsForValue().set(REDIS_KEY, objectMapper.writeValueAsString(weatherResponseDTO), 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis cache error : {}", REDIS_KEY, e);
        }
        return weatherResponseDTO;
    }
}