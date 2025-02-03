package com.hta2405.unite.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Project {
    private int projectId;

    private String projectName;
    private String projectStartDate;
    private String projectEndDate;
    private String managerId;
    private String managerName;
    private String projectContent;
    private String participants;
    private String viewers;
    private String bgColor;
    private String textColor;
    private String projectFavorite;
    private float avgProgress;


    private String projectFilePath;
    private String projectFileOriginal;
    private String projectFileUuid;
    private String projectFileType;
    private int project_finished;
    private int project_canceled;
}
