package com.hta2405.unite.dto;

import java.util.Date;

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
    private Date endDate;
    private int memberCount;
    private Double progressRate;
}
