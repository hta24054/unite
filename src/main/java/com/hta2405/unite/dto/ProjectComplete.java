package com.hta2405.unite.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectComplete {
    private int projectId; //프로젝트 아이디
    private String projectName; //프로젝트 이름
    private String empName; // 책임자 이름
    private List<String> participantNames; // 참여자 목록
    private Date projectStartDate; //시작일
    private Date projectEndDate;  //종료일
    private String projectFilePath;  //첨부파일(추가 필요?)

}