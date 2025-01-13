package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void insertNotification(Notification notification);

    List<Notification> selectNotificationsByRecipientId(String recipientId);

    void markAsRead(Long id);

    void deleteOldNotifications();
}
