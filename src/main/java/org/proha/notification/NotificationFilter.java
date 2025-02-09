package org.proha.notification;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.proha.notification.listener.SessionNotificationListener;
import org.proha.notification.service.NotificationService;

import java.io.IOException;

@WebFilter("/*")
public class NotificationFilter implements Filter {
    @Inject
    private NotificationService notificationService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(true);

        SessionNotificationListener listener = new SessionNotificationListener(session);
        notificationService.registerListener(listener);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
