package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Post;
import com.hta2405.unite.domain.PostFile;
import com.hta2405.unite.util.CalendarDateTimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
public class PostDetailDTO {
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

    private String imgPath;
    private String imgOriginal;
    private String imgUUID;
    private String imgType;

    private List<PostFile> postFiles;

    // 포맷된 날짜 문자열을 반환
    public String getFormattedPostDate() {
        // Asia/Seoul 시간대로 변환
        ZonedDateTime seoulTime = postDate.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        // 원하는 형식으로 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return seoulTime.format(formatter);
    }

    // 포맷된 날짜 문자열을 반환
    public String getFormattedPostUpdateDate() {
        // Asia/Seoul 시간대로 변환
        ZonedDateTime seoulTime = postUpdateDate.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        // 원하는 형식으로 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return seoulTime.format(formatter);
    }
}
