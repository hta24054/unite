package com.hta2405.unite.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Board {
    private Long boardId;
    private String boardName1;
    private String boardName2;
    private String boardDescription;
    private Long deptId;
}
