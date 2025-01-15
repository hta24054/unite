package com.hta2405.unite.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardManagement {
    private Long boardManagementId;
    private Long boardId;
    private String boardManager;
}
