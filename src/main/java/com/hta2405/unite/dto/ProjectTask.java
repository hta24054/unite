package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectTask {
	private int projectId; //글번호
	private String memberName; //글 작성자
	private String memberPass; //글 비밀번호
	private String projectTitle; //글 제목
	private String projectContent; //글 내용
	private String board_file; //첨부될 파일 이름
	private int board_re_ref; //답변 글 작성시 참조되는 글 번호
	private int board_re_lev; //답변 글의 깊이
	private int board_re_seq; //답변 글의 순서
	private int board_readcount; //조회수
	private String projectDate; //글 등록일자
	private String projectUpdateDate; //업데이트일자
	private int board_cnt;
}

