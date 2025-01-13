package com.hta2405.unite.scheduler;

import com.hta2405.unite.mybatis.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {

    private final NotificationMapper notificationMapper;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupOldNotifications() {
        notificationMapper.deleteOldNotifications();
    }
}
