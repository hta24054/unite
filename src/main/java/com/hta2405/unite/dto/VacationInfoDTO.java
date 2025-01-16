package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class VacationInfoDTO {
    private String targetEmpId; //직원 id
    private String targetEmpName; //직원이름
    private String jobName; //직책명
    private List<VacationDTO> vacationDTOList; //특정 년도 휴가정보
    private Long vacationCount; //현재 년도 총 부여 연차일
    private int usedCount; //현재 년도 연차 사용일
}
