package com.hta2405.unite.dto;

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
public class ProjectInfo {
	private int projectId;
    private String projectName;
    private String endDate;
    private int memberCount;
    private int progressRate;
    private Boolean isManager;
    
    private List<String> participantNames; // 참여자 목록
    private List<String> viewers;
    private String empName; // 책임자 이름
    
    private String project_file_path;
    private String project_file_original;
    private String project_file_uuid;
    private String project_file_type;
}
