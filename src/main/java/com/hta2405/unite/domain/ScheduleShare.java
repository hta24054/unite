package com.hta2405.unite.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScheduleShare {
    private int scheduleShareId;	//일정공유ID
    private String shareEmp;		//공유직원
    private int scheduleId;			//일정코드
}
