package com.hta2405.unite.service;

import com.hta2405.unite.domain.Notification;
import com.hta2405.unite.dto.NotificationDTO;
import com.hta2405.unite.enums.NotificationCategory;
import com.hta2405.unite.mybatis.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전송 템플릿

    // 알림 전송
    public void sendNotification(NotificationDTO notificationDTO) {
        //DB에 알림 저장
        Notification notification = Notification.builder()
                .category(notificationDTO.getCategory())
                .title(notificationDTO.getTitle())
                .message(notificationDTO.getMessage())
                .recipientId(notificationDTO.getRecipientId())
                .targetUrl(notificationDTO.getTargetUrl())
                .isRead(notificationDTO.getIsRead())
                .createdAt(LocalDateTime.parse(notificationDTO.getCreatedAt())).build();
        notificationMapper.insertNotification(notification);

        // WebSocket으로 전송
        messagingTemplate.convertAndSendToUser(
                notificationDTO.getRecipientId(), // 수신자 ID
                "/queue/notification",          // WebSocket 대상
                notificationDTO                  // 전달할 데이터
        );
        log.info("Notification sent to user {}: {}", notificationDTO.getRecipientId(), notificationDTO);
    }

    // 알림 조회
    public List<NotificationDTO> getNotifications(String recipientId) {
        return notificationMapper.selectNotificationsByRecipientId(recipientId)
                .stream()
                .map(notification -> NotificationDTO.builder()
                        .category(notification.getCategory())
                        .id(notification.getId())
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .recipientId(notification.getRecipientId())
                        .targetUrl(notification.getTargetUrl())
                        .isRead(notification.getIsRead())
                        .createdAt(notification.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }

    // 알림 읽음 처리
    public void markAsRead(Long id) {
        notificationMapper.markAsRead(id);
    }
}