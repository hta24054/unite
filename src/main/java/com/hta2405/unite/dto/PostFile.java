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
public class PostFile {
	private Long postFileId;
	private Long postId;
	private String postFilePath;
	private String postFileOriginal;
	private String postFileUUID;
	private String postFileType;
}
