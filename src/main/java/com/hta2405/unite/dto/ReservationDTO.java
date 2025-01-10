package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReservationDTO {
    private int reservationId; // 자원예약id
    private Long resourceId; // 자원id
    private String empId; // 예약자
    private LocalDateTime reservationStart; // 예약시작일시
    private LocalDateTime reservationEnd; // 예약종료일시
    private String reservationInfo; // 예약내용
    private boolean reservationAllDay; // 종일
    private String resourceType; // 자원 종류
    private String resourceName; // 자원 이름
    private boolean resourceUsable; // 자원 사용 가능 여부
}
