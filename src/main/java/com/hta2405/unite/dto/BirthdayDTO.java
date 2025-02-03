package com.hta2405.unite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BirthdayDTO {
    private Response response;

    @Data
    public static class Response {
        private Body body;

        @Data
        public static class Body {
            private Items items;

            @Data
            public static class Items {
                private Item item;

                @Data
                public static class Item {
                    @JsonProperty("lunYear")
                    private int lunarYear;

                    @JsonProperty("lunMonth")
                    private int lunarMonth;

                    @JsonProperty("lunDay")
                    private int lunarDay;

                    @JsonProperty("solYear")
                    private int solarYear;

                    @JsonProperty("solMonth")
                    private int solarMonth;

                    @JsonProperty("solDay")
                    private int solarDay;
                }
            }
        }
    }
}
