package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
//@Setter
@AllArgsConstructor
@ToString
@Builder
public class ProjectTodoDTO {
    private int todoId;
    private String todoSubject;
    private int progressRate;
}
