package com.hta2405.unite.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Schedule {
	private int scheduleId; 		//일정코드
	private String empId;			//일정등록자
	private String scheduleName;	//일정명
	private String scheduleContent; //일정내용
	private Date scheduleStart;		//시작일시
	private Date scheduleEnd;		//종료일시
}
