package com.hta2405.unite.dto;

import com.hta2405.unite.enums.NotificationCategory;
import lombok.*;

@Getter
@Setter
@Builder
public class NotificationDTO {
    private Long id;               // Primary Key
    private NotificationCategory category;       // 알림 유형
    private String title;          // 알림 제목
    private String message;        // 알림 메시지
    private String recipientId;    // 수신자 ID
    private String targetUrl;      // 알림 클릭 시 이동 URL
    private Boolean isRead;        // 읽음 상태
    private String createdAt;      // 클라이언트와의 데이터 전송 시 String으로 처리
}
