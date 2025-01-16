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


    private String project_file_path;
    private String projectFileOriginal;
    private String project_file_uuid;
    private String project_file_type;
    private int project_finished;
    private int project_canceled;
}
