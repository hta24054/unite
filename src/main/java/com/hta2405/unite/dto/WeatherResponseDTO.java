package com.hta2405.unite.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class WeatherResponseDTO {
    private Main main;
    private List<Weather> weather;

    @Getter
    @Setter
    public static class Main {
        private double temp;
        private double temp_min;
        private double temp_max;
    }

    @Getter
    @Setter
    public static class Weather {
        private String icon;
        private String description;
    }
}
