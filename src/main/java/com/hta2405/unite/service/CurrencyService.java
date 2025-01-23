package com.hta2405.unite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.dto.CurrencyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CurrencyService {
    private final String apiKey;
    private final String URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";
    private static final String REDIS_KEY = "currency";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper; // JSON 직렬화/역직렬화

    public CurrencyService(@Value("${currency.api.key}") String apiKey,
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
        List<CurrencyDTO> currencyDTOList = fetchCurrencyDataWithFallback();

        try {
            log.debug("Currency data to cache: {}", objectMapper.writeValueAsString(currencyDTOList));
            redisTemplate.opsForValue().set(REDIS_KEY, objectMapper.writeValueAsString(currencyDTOList), 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis cache error: {}", REDIS_KEY, e);
        }

        return currencyDTOList;
    }

    // 환율 반환 메서드, 아직 업데이트 안된날은 전날로 시도함
    private List<CurrencyDTO> fetchCurrencyDataWithFallback() {
        LocalDate date = LocalDate.now();
        List<CurrencyDTO> currencyList = fetchCurrencyData(date);
        while (currencyList.isEmpty()) {
            date = date.minusDays(1);
            log.info("환율 정보가 업데이트 되지 않아, 전날로 조회합니다.");
            currencyList = fetchCurrencyData(date);
        }
        log.debug("Fetched currency data: {}", currencyList);
        return currencyList;
    }

    // API 호출
    public List<CurrencyDTO> fetchCurrencyData(LocalDate date) {
        URI uri = URI.create(URL + "?authkey=" + apiKey + "&searchdate=" + date + "&data=AP01");
        log.info("currency uri = {}", uri);
        RestTemplate restTemplate = new RestTemplate();
        try {
            String jsonResponse = restTemplate.getForObject(uri, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            List<CurrencyDTO> currencies = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            log.info("Filtered currency data: {}", currencies);
            return new ArrayList<>(currencies);

        } catch (Exception e) {
            log.error("Failed to fetch currency data from API: {}", e.getMessage());
            throw new RuntimeException("환율 정보를 가져오는데 실패했습니다.");
        }
    }

    // 1시간마다 데이터 갱신
    @Scheduled(cron = "0 0 12 * * ?")
    public void deleteRedisCache() {
        try {
            redisTemplate.delete(REDIS_KEY);
        } catch (Exception e) {
            log.error("Redis Cache 삭제 오류, key = {}", REDIS_KEY, e);
        }
    }
}
