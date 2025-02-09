package org.proha.notification.dispatcher;

import org.proha.notification.event.NotificationEvent;
import org.proha.notification.listener.NotificationListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationDispatcher {
    private final List<NotificationListener> listeners = new ArrayList<>();

    public void registerListener(NotificationListener listener) {
        listeners.add(listener);
    }

    public void dispatch(NotificationEvent event) {
        for (NotificationListener listener : listeners) {
            listener.handle(event);
        }
    }
}
