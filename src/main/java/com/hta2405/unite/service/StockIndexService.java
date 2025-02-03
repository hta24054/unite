package com.hta2405.unite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.dto.StockIndexDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static java.net.URI.create;

@Service
@Slf4j
public class StockIndexService {
    private final String apiKey;
    private final String URL = "https://ecos.bok.or.kr/api/StatisticSearch";
    private final String REDIS_KEY = "stockIndex";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final String[] stockIndexCode = {"0001000", "0089000"}; //코스피, 코스닥


    public StockIndexService(@Value("${bankOfKorea.api.key}") String apiKey,
                             RedisTemplate<String, String> redisTemplate,
                             ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public StockIndexDTO getCachedIndex() {
        try {
            String cachedData = redisTemplate.opsForValue().get(REDIS_KEY);
            if (cachedData != null) {
                log.info("Redis cache hit : {}", REDIS_KEY);
                return objectMapper.readValue(cachedData, new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            log.error("Redis server error", e);
        }

        log.info("Redis cache miss, key = {}", REDIS_KEY);
        Double kospiIndex = fetchIndexData(stockIndexCode[0]);
        Double kosdaqIndex = fetchIndexData(stockIndexCode[1]);
        log.info("kospiInfo = {}",kospiIndex);
        log.info("kosdaqInfo = {}",kosdaqIndex);

        StockIndexDTO stockIndexDTO = new StockIndexDTO(kospiIndex, kosdaqIndex);
        try {
            log.debug("Currency data to cache: {}", objectMapper.writeValueAsString(stockIndexDTO));
            redisTemplate.opsForValue().set(REDIS_KEY, objectMapper.writeValueAsString(stockIndexDTO), 24, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis cache error: {}", REDIS_KEY, e);
        }

        return stockIndexDTO;
    }

    // API 호출
    public Double fetchIndexData(String indexCode) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = String.format("%s/%s/json/kr/1/10/802Y001/D/%s/%s/%s",
                URL,
                apiKey,
                LocalDate.now().minusDays(10).toString().replaceAll("-", ""),
                LocalDate.now().toString().replaceAll("-", ""),
                indexCode);
        log.info("index URL = {}", url);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode rows = root.path("StatisticSearch").path("row");

                if (rows.isArray()) {
                    // 가장 최근 데이터
                    JsonNode latestRow = rows.get(rows.size() - 1);
                    return latestRow.path("DATA_VALUE").asDouble();
                }
            } else {
                log.warn("Failed to fetch stockIndex data {}", response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error fetching stockIndex data {}", e.getMessage(), e);
        }
        return null;
    }

    // 12시간마다 데이터 갱신
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void deleteRedisCache() {
        try {
            redisTemplate.delete(REDIS_KEY);
        } catch (Exception e) {
            log.error("Redis Cache 삭제 오류, key = {}", REDIS_KEY, e);
        }
    }
}
