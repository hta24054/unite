package com.hta2405.unite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.dto.CurrencyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.net.URI.create;

@Service
@Slf4j
public class CurrencyService {
    private final String apiKey;
    private final String URL = "https://ecos.bok.or.kr/api/StatisticSearch";
    private static final String REDIS_KEY = "currency";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper; // JSON 직렬화/역직렬화
    //미국, 일본, 유로, 중국 순
    private final String[][] nationalCode = {
            {"USD", "0000001"},
            {"JPY", "0000002"},
            {"EUR", "0000003"},
            {"CNH", "0000053"}
    };

    public CurrencyService(@Value("${bankOfKorea.api.key}") String apiKey,
                           RedisTemplate<String, String> redisTemplate,
                           ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public List<CurrencyDTO> getCachedCurrency() {
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
        List<CurrencyDTO> currencyDTOList = fetchCurrencyData();

        try {
            log.debug("Currency data to cache: {}", objectMapper.writeValueAsString(currencyDTOList));
            redisTemplate.opsForValue().set(REDIS_KEY, objectMapper.writeValueAsString(currencyDTOList), 24, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis cache error: {}", REDIS_KEY, e);
        }

        return currencyDTOList;
    }

    // API 호출
    public List<CurrencyDTO> fetchCurrencyData() {
        ArrayList<CurrencyDTO> list = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        for (String[] code : nationalCode) {
            String currencyUnit = code[0];
            String currencyCode = code[1];
            String url = String.format("%s/%s/json/kr/1/10/731Y001/D/%s/%s/%s",
                    URL,
                    apiKey,
                    LocalDate.now().minusDays(10).toString().replaceAll("-", ""),
                    LocalDate.now().toString().replaceAll("-", ""),
                    currencyCode);
            log.info("currency URL = {}", url);
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(create(url))
                        .GET()
                        .build();

                // HTTP 요청 전송 및 응답 수신
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    // JSON 파싱
                    JsonNode root = objectMapper.readTree(response.body());
                    JsonNode rows = root.path("StatisticSearch").path("row");

                    if (rows.isArray()) {
                        // 가장 최근 데이터 가져오기
                        JsonNode latestRow = rows.get(rows.size() - 1);
                        // CurrencyDTO 생성
                        list.add(new CurrencyDTO(currencyUnit, latestRow.path("DATA_VALUE").asText()));
                    }
                } else {
                    log.warn("Failed to fetch currency data for {}: {}", currencyUnit, response.statusCode());
                }
            } catch (Exception e) {
                log.error("Error fetching currency data for {}: {}", currencyUnit, e.getMessage(), e);
            }
        }
        log.info("currency data list = {}", list);
        return list;
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
