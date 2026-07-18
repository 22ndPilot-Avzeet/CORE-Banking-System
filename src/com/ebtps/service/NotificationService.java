package com.ebtps.service;

import com.ebtps.dao.NotificationDAO;
import com.ebtps.dao.NotificationDAOImpl;

/**
 * Service to handle notifications asynchronously using basic Threads.
 * Demonstrates basic Multithreading.
 */
public class NotificationService {

    private final NotificationDAO notificationDAO;
    
    // Singleton instance
    private static NotificationService instance;

    private NotificationService() {
        this.notificationDAO = new NotificationDAOImpl();
    }

    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    /**
     * Submits a notification task to a new background thread.
     */
    public void sendNotificationAsync(int userId, String message) {
        Thread notifThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Simulate some slight delay/processing overhead
                    Thread.sleep(100); 
                    notificationDAO.addNotification(userId, message);
                    System.out.println("[Async] Notification sent to user " + userId + " on thread: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Notification thread interrupted: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Failed to send notification: " + e.getMessage());
                }
            }
        });
        notifThread.start();
    }
}
