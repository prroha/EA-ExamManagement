package org.proha.notification.listener;

import org.proha.notification.event.NotificationEvent;

public interface NotificationListener {
    void handle(NotificationEvent event);
}
