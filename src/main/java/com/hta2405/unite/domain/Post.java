package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Post {
	private Long postId;
	private Long boardId;
	private String postWriter; //emp_id
	private String postSubject;
	private String postContent;
	private LocalDateTime postDate;
	private LocalDateTime postUpdateDate;
	private Long postReRef;
	private Long postReLev;
	private Long postReSeq;
	private Long postView;
	private Long postCommentCnt;
	
	// 포맷된 날짜 문자열을 반환
    public String getFormattedPostDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return postDate.format(formatter);
    }
    
    // 포맷된 날짜 문자열을 반환
    public String getFormattedPostUpdateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return postUpdateDate.format(formatter);
    }
}
