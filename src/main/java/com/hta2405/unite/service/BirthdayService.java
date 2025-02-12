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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BirthdayService {
    private final String BaseURL = "http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService/getLunCalInfo";
    private final String ServiceKey = "&ServiceKey=";
    private final String apiKey;
    private final EmpMapper empMapper;
    private static final String REDIS_KEY_PREFIX = "birthday:";

    public BirthdayService(@Value("${birthday.apiKey}") String apiKey, EmpMapper empMapper) {
        this.apiKey = apiKey;
        this.empMapper = empMapper;
    }

    public BirthdayDTO getLunarDate(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String year = formattedDate.substring(0, 4);
        String month = formattedDate.substring(4, 6);
        String day = formattedDate.substring(6, 8);
        URI uri = URI.create(BaseURL + "?solYear=" + year + "&solMonth=" + month + "&solDay=" + day + ServiceKey + apiKey + "&_type=json");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, BirthdayDTO.class);
    }

    public List<Birthday> getTodayBirthdays() {
        LocalDate today = LocalDate.now();
        BirthdayDTO lunar = getLunarDate(today);
        int lunarMonth = lunar.getResponse().getBody().getItems().getItem().getLunarMonth();
        int lunarDay = lunar.getResponse().getBody().getItems().getItem().getLunarDay();

        return empMapper.findTodayBirthdays(today.getMonthValue(), today.getDayOfMonth(), lunarMonth, lunarDay);
    }
}
