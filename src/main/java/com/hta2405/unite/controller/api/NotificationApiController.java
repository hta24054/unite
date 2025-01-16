package com.hta2405.unite.controller.api;

import com.hta2405.unite.dto.NotificationDTO;
import com.hta2405.unite.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationApiController {
    private final NotificationService notificationService;

    @PostMapping
    public void sendNotification(@RequestBody NotificationDTO notification) {
        notificationService.sendNotification(notification);
    }

    @GetMapping("/{recipientId}")
    public List<NotificationDTO> getNotifications(@PathVariable String recipientId) {
        return notificationService.getNotifications(recipientId);
    }

    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}