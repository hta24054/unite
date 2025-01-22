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

    public List<CurrencyDTO> getCachedCurrency() {
        // 초기 서버 부팅 시 캐싱된 데이터가 없으면 API 호출
        if (cachedCurrency == null) {
            cachedCurrency = fetchCurrencyDataWithFallback(LocalDate.now());
        }
        return cachedCurrency;
    }

    // 환율 반환 메서드, 아직 업데이트 안된날은 전날로 시도함
    private List<CurrencyDTO> fetchCurrencyDataWithFallback(LocalDate date) {
        List<CurrencyDTO> currencyList = fetchCurrencyData(date);
        if (currencyList.isEmpty()) {
            log.info("환율 정보가 업데이트 되지 않아, 전날로 조회합니다.");
            currencyList = fetchCurrencyData(date.minusDays(1));
        }
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
            List<CurrencyDTO> currencies = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

            log.info("Filtered currency data: {}", currencies);
            return new ArrayList<>(currencies);

        } catch (Exception e) {
            log.error("Failed to fetch currency data from API: {}", e.getMessage());
            throw new RuntimeException("환율 정보를 가져오는데 실패했습니다.");
        }
    }

    // 1시간마다 데이터 갱신
    @Scheduled(cron = "0 0 * * * ?")
    public void updateCurrencyCache() {
        try {
            cachedCurrency = fetchCurrencyDataWithFallback(LocalDate.now());
            log.info("Currency cache updated: {}", cachedCurrency);
        } catch (Exception e) {
            log.error("Failed to update currency data: {}", e.getMessage());
        }
    }
}
