package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class ProjectDetailDTO {
    private int projectId; //프로젝트 아이디
    private String memberId; //유저 아이디
    private String managerId;
    private String participantNames; // 참여자 목록
    private String memberDesign; //진행률 - 업무
    private double memberProgressRate;  //진행률 - 진행률
    private boolean isManager = false;
    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }
    private String jobName; //직책
    private int todoNum; //투두
}
