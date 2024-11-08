package com.hta2405.unite.dto;

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
public class ProjectDetail {
    private int projectId; //프로젝트 아이디
    private String memberId; //유저 아이디
    private String participantNames; // 참여자 목록
    private String memberDesign; //진행률 - 업무
    private int memberProgressRate;  //진행률 - 진행률
    private String taskWriter;  //진행과정 - 작성자
    private String taskTitle;  //진행과정 - 제목
    private String taskUpdateDate;  //진행과정 - 작성일
    private String content;
    private Boolean isManager;
}
