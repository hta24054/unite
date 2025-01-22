package com.hta2405.unite.controller.api;

import com.hta2405.unite.dto.WeatherResponseDTO;
import com.hta2405.unite.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherApiController {
    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponseDTO> getWeather() {
        return ResponseEntity.ok(weatherService.getCachedWeather());
    }
}