package com.hta2405.unite.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Birthday;
import com.hta2405.unite.dto.BirthdayDTO;
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
public class BirthdayService {
    private static final String BASE_URL = "http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService/getLunCalInfo";
    private static final String SERVICE_KEY_PARAM = "&ServiceKey=";
    private static final String REDIS_KEY_PREFIX = "birthday:";
    private static final String REDIS_LUNAR_KEY_PREFIX = "lunar:";

    private final String apiKey;
    private final EmpMapper empMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public BirthdayService(@Value("${birthday.apiKey}") String apiKey, EmpMapper empMapper, RedisTemplate<String, String> redisTemplate) {
        this.apiKey = apiKey;
        this.empMapper = empMapper;
        this.redisTemplate = redisTemplate;
    }

    public BirthdayDTO getLunarDate(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String redisKey = REDIS_LUNAR_KEY_PREFIX + formattedDate;

        // Redis 캐싱 확인
        String cachedData = redisTemplate.opsForValue().get(redisKey);
        if (cachedData != null) {
            try {
                return objectMapper.readValue(cachedData, BirthdayDTO.class);
            } catch (Exception e) {
                log.error("Redis LunarDate 파싱 오류: {}", e.getMessage());
            }
        }

        // API 호출
        URI uri = URI.create(BASE_URL + "?solYear=" + formattedDate.substring(0, 4) +
                "&solMonth=" + formattedDate.substring(4, 6) +
                "&solDay=" + formattedDate.substring(6, 8) + SERVICE_KEY_PARAM + apiKey + "&_type=json");
        RestTemplate restTemplate = new RestTemplate();
        BirthdayDTO response = restTemplate.getForObject(uri, BirthdayDTO.class);

        // Redis 저장
        try {
            redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(response), 24, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("LunarDate Redis 저장 오류: {}", e.getMessage());
        }
        return response;
    }

    public List<Birthday> getTodayBirthdays() {
        LocalDate today = LocalDate.now();
        String redisKey = REDIS_KEY_PREFIX + today;

        try {
            // 캐시된 데이터 가져오기
            String cachedBirthdays = redisTemplate.opsForValue().get(redisKey);
            if (cachedBirthdays != null) {
                return objectMapper.readValue(cachedBirthdays, new TypeReference<>() {});
            }
        } catch (Exception e) {
            log.error("Redis에서 생일 데이터 가져오기 오류: {}", e.getMessage());
        }

        // Lunar 변환
        BirthdayDTO lunar = getLunarDate(today);
        int lunarMonth = lunar.getResponse().getBody().getItems().getItem().getLunarMonth();
        int lunarDay = lunar.getResponse().getBody().getItems().getItem().getLunarDay();

        // DB에서 데이터 조회
        List<Birthday> birthdays = empMapper.findTodayBirthdays(today.getMonthValue(), today.getDayOfMonth(), lunarMonth, lunarDay);

        // Redis 캐싱
        try {
            redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(birthdays), 24, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            log.error("Redis 저장 오류: {}", e.getMessage());
        }

        return birthdays;
    }
}
