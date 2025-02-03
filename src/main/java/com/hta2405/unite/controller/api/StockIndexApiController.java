package com.hta2405.unite.controller.api;

import com.hta2405.unite.dto.StockIndexDTO;
import com.hta2405.unite.service.StockIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stockIndex")
@RequiredArgsConstructor
public class StockIndexApiController {
    private final StockIndexService stockIndexService;

    @GetMapping
    public ResponseEntity<StockIndexDTO> getStockIndex() {
        return ResponseEntity.ok(stockIndexService.getCachedIndex());
    }
}
