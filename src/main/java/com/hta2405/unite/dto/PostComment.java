package com.hta2405.unite.dto;

import java.time.LocalDateTime;

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
public class PostComment {
	private Long commentId;
	private Long postId;
	private String postCommentWriter;
	private String postCommentContent;
	private LocalDateTime postCommentDate;
	private LocalDateTime postCommentUpdateDate;
	private String postCommentFilePath;
	private String postCommentFileOriginal;
	private String postCommentFileUUID;
	private String postCommentFileType;
	private Long postCommentReRef;
	private Long postCommentReLev;
	private Long postCommentReSeq;
}
