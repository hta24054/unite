package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ProjectTodoDTO {
    private int todoId;
    private String todoSubject;
    private int progressRate;
    private int orderColumn;
}
