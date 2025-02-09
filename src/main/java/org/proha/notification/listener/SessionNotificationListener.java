package org.proha.notification.listener;

import jakarta.servlet.http.HttpSession;
import org.proha.notification.event.NotificationEvent;
import org.proha.notification.listener.NotificationListener;
import org.proha.notification.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class SessionNotificationListener implements NotificationListener {
    private final HttpSession session;

    public SessionNotificationListener(HttpSession session) {
        this.session = session;
    }

    @Override
    public void handle(NotificationEvent event) {
        Notification notification = event.notification();
        List<Notification> notifications =
                (List<Notification>) session.getAttribute("notifications");

        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        session.setAttribute("notifications", notifications);
    }
}
