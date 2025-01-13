package com.hta2405.unite.domain;

import com.hta2405.unite.enums.NotificationCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Notification {
    private Long id;               // pk
    private NotificationCategory category;       // 알림 유형
    private String title;          // 알림 제목
    private String message;        // 알림 메시지
    private String recipientId;    // 수신자 ID (empId fk)
    private String targetUrl;      // 알림 클릭 시 이동 URL
    private Boolean isRead;        // 읽음 상태
    private LocalDateTime createdAt; // 생성 시각
}