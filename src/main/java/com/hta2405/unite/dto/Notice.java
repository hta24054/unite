package com.hta2405.unite.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notice {
    private Long noticeId;
    private String noticeSubject;
    private String noticeContent;
    private LocalDate noticeEndDate;
}
