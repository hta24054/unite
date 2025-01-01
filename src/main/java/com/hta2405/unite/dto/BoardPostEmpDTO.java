package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Board;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardPostEmpDTO {
    private Board board;
    private Post post;
    private Emp emp;
}
