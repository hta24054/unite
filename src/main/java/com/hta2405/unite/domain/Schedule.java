package com.hta2405.unite.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Schedule {
    private int scheduleId; 		//일정코드
    private String empId;			//일정등록자
    private String scheduleName;	//일정명
    private String scheduleContent; //일정내용
    private LocalDateTime scheduleStart;//시작일시
    private LocalDateTime scheduleEnd;//종료일시
    private String scheduleColor;    //일정색상
    private boolean scheduleAllDay;		//종일
}
