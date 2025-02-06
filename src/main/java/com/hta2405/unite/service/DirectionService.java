package com.hta2405.unite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hta2405.unite.domain.Birthday;
import com.hta2405.unite.dto.BirthdayDTO;
import com.hta2405.unite.dto.DirectionResponseDTO;
import com.hta2405.unite.dto.WeatherResponseDTO;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DirectionService {
    private final String apiKey;
    private final String URL = "https://maps.googleapis.com/maps/api/directions/json";
    private static final String REDIS_KEY = "direction:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public DirectionService(@Value("${googleMap.apiKey}") String apiKey,
                            RedisTemplate<String, String> redisTemplate,
                            ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public DirectionResponseDTO fetchDirectionsData(String origin, String destination) {
        URI uri = URI.create(URL + "?origin=" + origin + "&destination=" + destination
                + "&mode=transit&traffic_model=best_guess&departure_time=now&key=" + apiKey);

        RestTemplate restTemplate = new RestTemplate();
        log.info("경로 정보 가져옴");
        return restTemplate.getForObject(uri, DirectionResponseDTO.class);
    }

    public DirectionResponseDTO getCachedDirection(String origin, String destination) {
        // 캐시된 데이터가 있으면 바로 반환
        try {
            String cachedData = redisTemplate.opsForValue().get(REDIS_KEY);
            if (cachedData != null) {
                try {
                    log.info("Redis cache hit : {}", REDIS_KEY);
                    return objectMapper.readValue(cachedData, DirectionResponseDTO.class);
                } catch (JsonProcessingException e) {
                    log.error("Redis cache error: {}", REDIS_KEY, e);
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            log.error("Redis 서버 오류", e);
        }

        // 캐시된 데이터가 없으므로 API 호출
        log.info("Redis cache miss : {}", REDIS_KEY);
        DirectionResponseDTO directionsResponseDTO = fetchDirectionsData(origin, destination);
        try {
            redisTemplate.opsForValue().set(REDIS_KEY, objectMapper.writeValueAsString(directionsResponseDTO), 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis cache error : {}", REDIS_KEY, e);
        }
        return directionsResponseDTO;
    }
}
