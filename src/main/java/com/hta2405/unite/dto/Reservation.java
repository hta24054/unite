package com.hta2405.unite.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@ToString
public class Reservation {
	private int reservationId; //자원예약id
	private int resourceId; //자원id
	private String empId; //예약자
	private LocalDateTime reservationStart; //예약시작일시
	private LocalDateTime reservationEnd; //예약종료일시
	private String reservationInfo; //예약내용
	private int reservationAllDay; //종일
}
