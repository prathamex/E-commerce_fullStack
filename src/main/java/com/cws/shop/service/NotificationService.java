package com.cws.shop.service;
import java.util.List;

import org.springframework.data.domain.Page;

import com.cws.shop.dto.response.NotificationResponse;
import com.cws.shop.dto.response.PagedResponse;
import com.cws.shop.model.Notification;
import com.cws.shop.model.NotificationType;
import com.cws.shop.model.User;

public interface NotificationService {
	NotificationResponse createNotification(
            User user,
            String title,
            String message,
            NotificationType type);

    Long getUnreadCount(Long userId);

    NotificationResponse markAsRead(Long notificationId, Long userId);

    List<NotificationResponse> getUnreadNotifications(Long userId);

    PagedResponse<NotificationResponse> getAllNotifications(
            Long userId,
            int page,
            int size
    );
    
    void markAllAsRead(Long userId);

    
}