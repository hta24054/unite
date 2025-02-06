package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DirectionResponseDTO {
    private String status;
    private Route[] routes;

    @Getter
    @Setter
    public static class Route {
        private Leg[] legs;

        @Getter
        @Setter
        public static class Leg {
            private Distance distance;
            private Duration duration;
            private String start_address;
            private String end_address;

            @Getter
            @Setter
            public static class Distance {
                private String text;
                private int value;
            }

            @Getter
            @Setter
            public static class Duration {
                private String text;
                private int value;
            }
        }
    }
}
