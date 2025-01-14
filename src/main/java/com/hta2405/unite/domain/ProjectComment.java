package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectComment {
    private Long taskCommentId;
    private String taskCommentWriter;
    private long taskId;
    private String taskCommentContent;
    private LocalDateTime taskCommentDate;
    private int taskCommentReLev;
    private int taskCommentReSeq;
    private int taskCommentReRef;
    private String eName;
}
