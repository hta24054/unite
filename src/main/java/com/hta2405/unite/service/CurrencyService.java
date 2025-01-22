package com.hta2405.unite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.dto.CurrencyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CurrencyService {
    private final String apiKey;
    private final String URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";
    private List<CurrencyDTO> cachedCurrency; // 캐싱된 환율 데이터

    public CurrencyService(@Value("${currency.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public List<CurrencyDTO> fetchCurrencyData() {
        //환율이 11시 전후 업데이트 되므로, 12시 이전에는 전날로 요청
        LocalDate date = LocalDate.now();
        if (LocalDateTime.now().getHour() < 12) {
            date = date.minusDays(1);
        }

        URI uri = URI.create(URL + "?authkey=" + apiKey + "&searchdate=" + date + "&data=AP01");
        log.info("currency uri = {}", uri);
        RestTemplate restTemplate = new RestTemplate();
        try {
            String jsonResponse = restTemplate.getForObject(uri, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            List<CurrencyDTO> currencies = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            List<CurrencyDTO> currencyDTOList = new ArrayList<>(currencies);

            log.info("Filtered currency data: {}", currencyDTOList);
            return currencyDTOList;

        } catch (Exception e) {
            log.error("Failed to fetch currency data from API: {}", e.getMessage());
            throw new RuntimeException("환율 정보를 가져오는데 실패했습니다.");
        }
    }


    // 캐싱된 데이터 반환
    public List<CurrencyDTO> getCachedCurrency() {
        if (cachedCurrency == null) {
            // 초기 서버 부팅 시 캐싱된 데이터가 없으면 API 호출
            cachedCurrency = fetchCurrencyData();
        }
        return cachedCurrency;
    }

    // 1시간마다 데이터 갱신
    @Scheduled(cron = "0 0 * * * ?")
    public void updateCurrencyCache() {
        try {
            cachedCurrency = fetchCurrencyData();
            System.out.println("Currency cache updated: " + cachedCurrency);
        } catch (Exception e) {
            System.err.println("Failed to update currency data: " + e.getMessage());
        }
    }
}
