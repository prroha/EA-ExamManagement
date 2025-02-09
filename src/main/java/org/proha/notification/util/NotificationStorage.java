package org.proha.notification.util;

import jakarta.servlet.http.HttpSession;
import org.proha.notification.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationStorage {
    public static List<Notification> getAndClearNotifications(HttpSession session) {
        List<Notification> notifications =
                (List<Notification>) session.getAttribute("notifications");
        System.out.println("notifications: " + notifications);
        if (notifications != null) {
            session.removeAttribute("notifications");
        }
        return notifications != null ? notifications : new ArrayList<>();
    }
}
