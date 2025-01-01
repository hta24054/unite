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

    private String project_name;
    private String project_start_date;
    private String project_end_date;
    private String manager_id;
    private String manager_name;
    private String project_content;
    private String participants;
    private String viewers;


    private String project_file_path;
    private String project_file_original;
    private String project_file_uuid;
    private String project_file_type;
    private int project_finished;
    private int project_canceled;
}
