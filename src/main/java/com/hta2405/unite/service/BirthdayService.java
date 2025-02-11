package com.hta2405.unite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BirthdayService {
    private final String BaseURL = "http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService/getLunCalInfo";
    private final String ServiceKey = "&ServiceKey=";
    private final String apiKey;
    private final EmpMapper empMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "birthday:";
    private final ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);

    public BirthdayService(@Value("${birthday.apiKey}") String apiKey, EmpMapper empMapper, RedisTemplate<String, String> redisTemplate) {
        this.apiKey = apiKey;
        this.empMapper = empMapper;
        this.redisTemplate = redisTemplate;
    }

    public BirthdayDTO getLunarDate(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String year = formattedDate.substring(0, 4);
        String month = formattedDate.substring(4, 6);
        String day = formattedDate.substring(6, 8);

        URI uri = URI.create(BaseURL + "?solYear=" + year + "&solMonth=" + month + "&solDay=" + day
                + ServiceKey + apiKey + "&_type=json");

        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate.getForObject(uri, BirthdayDTO.class);
        } catch (Exception e) {
            // 로그를 통해 문제를 추적할 수 있게 예외 처리
            log.error("Failed to get lunar date for: {}", date, e);
            return null;  // API 호출 실패 시 null 반환
        }
    }

    public List<Birthday> getTodayBirthdays() {
        LocalDate today = LocalDate.now();
        String redisKey = REDIS_KEY_PREFIX + today;

        // Lunar Date 조회
        BirthdayDTO lunar = getLunarDate(today);
        if (lunar == null) {
            log.error("Failed to retrieve lunar date for {}", today);
            return Collections.emptyList();  // Lunar Date 조회 실패 시 빈 리스트 반환
        }

        int lunarMonth = lunar.getResponse().getBody().getItems().getItem().getLunarMonth();
        int lunarDay = lunar.getResponse().getBody().getItems().getItem().getLunarDay();

        try {
            // 캐시에서 데이터 조회
            String cachedBirthdays = redisTemplate.opsForValue().get(redisKey);
            if (cachedBirthdays != null) {
                // 캐시가 있으면 반환
                return objectMapper.readValue(cachedBirthdays, new TypeReference<List<Birthday>>(){});
            }

            // 캐시가 없으면 DB에서 조회
            List<Birthday> birthdays = empMapper.findTodayBirthdays(today.getMonthValue(), today.getDayOfMonth(), lunarMonth, lunarDay);

            // DB에서 조회한 데이터를 캐시에 저장 (24시간 유효)
            redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(birthdays), 24, TimeUnit.HOURS);
            return birthdays;
        } catch (Exception e) {
            // 캐시 처리 실패나 DB 조회 실패 시 로깅
            log.error("Error retrieving birthdays for {}: {}", today, e.getMessage());
            // 캐시나 DB 조회 실패 시에도 기본적으로 DB에서 다시 조회
            return empMapper.findTodayBirthdays(today.getMonthValue(), today.getDayOfMonth(), lunarMonth, lunarDay);
        }
    }

}
