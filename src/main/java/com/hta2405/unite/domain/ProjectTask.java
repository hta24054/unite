package com.hta2405.unite.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectTask {
    private long taskId;
    private String memberId;
    private int projectId;
    private String taskSubject;
    private String taskContent;
    private String taskDate;
    private String taskUpdateDate;
    private String taskFilePath;
    private String taskFileOriginal;
    private String taskFileUuid;
    private String taskFileType;
    private String memberName;
}
