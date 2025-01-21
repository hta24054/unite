package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
