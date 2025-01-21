package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
//@Setter
@AllArgsConstructor
@ToString
@Builder
public class ProjectTaskDTO {
    private String taskWriter;  //진행과정 - 작성자
    private String jobName; //직책
    private String taskSubject;  //진행과정 - 제목
    private String taskDate;  //진행과정 - 작성일
    //private String taskUpdateDate;  //진행과정 - 작성일
    private String memberId;
}
