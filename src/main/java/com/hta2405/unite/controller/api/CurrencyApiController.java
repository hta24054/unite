package com.hta2405.unite.controller.api;

import com.hta2405.unite.dto.CurrencyDTO;
import com.hta2405.unite.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyApiController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDTO>> getCurrency() {
        return ResponseEntity.ok(currencyService.getCachedCurrency());
    }
}
