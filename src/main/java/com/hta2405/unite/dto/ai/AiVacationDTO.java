package com.hta2405.unite.dto.ai;

import com.hta2405.unite.enums.AttendType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AiVacationDTO {
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private String vacationType;
    private String vacationInfo;
}
