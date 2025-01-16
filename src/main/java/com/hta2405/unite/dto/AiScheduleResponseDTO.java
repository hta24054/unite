package com.hta2405.unite.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AiScheduleResponseDTO {
    private String scheduleName;
    private String scheduleContent;
    private String scheduleStart;
    private String scheduleEnd;
}
