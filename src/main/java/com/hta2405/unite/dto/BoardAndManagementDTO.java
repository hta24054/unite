package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardAndManagementDTO {
    private Long boardId;
    private String boardName1;
    private String boardName2;
    private String boardDescription;
    private String boardManager;
}
