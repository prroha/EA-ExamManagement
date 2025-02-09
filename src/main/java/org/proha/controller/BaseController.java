package org.proha.controller;

import jakarta.inject.Inject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import org.proha.notification.model.NotificationType;
import org.proha.notification.service.NotificationService;

@WebServlet
public abstract class BaseController extends HttpServlet {
    @Inject
    protected NotificationService notificationService;

    private static final Logger LOGGER = Logger.getLogger(BaseController.class.getName());

    protected void forwardToPage(HttpServletRequest request, HttpServletResponse response, String page)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        LOGGER.severe("An error occurred: " + e.getMessage());
        notificationService.notify("An error occurred: " + e.getMessage(), NotificationType.ERROR);
        forwardToPage(request, response, "/jsp/error.jsp");
    }

    protected int parsePageParameter(String param) {
        return parsePageParameter(param, 1);
    }

    protected int parsePageParameter(String param, int defaultValue) {
        try {
            return param != null ? Math.max(Integer.parseInt(param), 1) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}