package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BoardRequestDTO {
    private Long boardId;
    private String boardName1;
    private String OriginalBoardName1;
    private String boardName2;
    private String OriginalBoardName2;
    private List<String> managerId;
}
