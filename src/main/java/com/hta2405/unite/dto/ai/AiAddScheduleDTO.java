package com.hta2405.unite.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AiAddScheduleDTO {
    private String scheduleName;
    private String scheduleContent;
    private LocalDateTime scheduleStart;
    private LocalDateTime scheduleEnd;
}
