package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Notice {
    private Long noticeId;
    private String noticeSubject;
    private String noticeContent;
    private LocalDate noticeEndDate;
}
