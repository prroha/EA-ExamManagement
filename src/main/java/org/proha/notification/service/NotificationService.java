package org.proha.notification.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.proha.notification.dispatcher.NotificationDispatcher;
import org.proha.notification.event.NotificationEvent;
import org.proha.notification.listener.NotificationListener;
import org.proha.notification.listener.SessionNotificationListener;
import org.proha.notification.model.Notification;
import org.proha.notification.model.NotificationType;


@ApplicationScoped
public class NotificationService {
    private final NotificationDispatcher dispatcher = new NotificationDispatcher();

    public void registerListener(NotificationListener listener) {
        dispatcher.registerListener(listener);
    }

    public void notify(String message, NotificationType type) {
        Notification notification = new Notification(message, type);
        dispatcher.dispatch(new NotificationEvent(notification));
    }
}
