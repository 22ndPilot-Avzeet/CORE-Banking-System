package com.ebtps.dao;

import com.ebtps.model.Notification;
import java.util.List;

public interface NotificationDAO {
    void addNotification(int userId, String message);
    List<Notification> getUnreadNotifications(int userId);
    void markAsRead(int notificationId);
}
