package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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


    private String project_file_path;
    private String project_file_original;
    private String project_file_uuid;
    private String project_file_type;
    private int project_finished;
    private int project_canceled;
}
