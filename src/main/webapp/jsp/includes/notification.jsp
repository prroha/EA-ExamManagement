<%@ page import="org.proha.notification.model.Notification" %>
<%@ page import="java.util.List" %>
<%@ page import="org.proha.notification.util.NotificationStorage" %>
<%@ page import="jakarta.json.Json" %>
<%@ page import="jakarta.json.JsonObjectBuilder" %>
<%@ page import="jakarta.json.JsonArrayBuilder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="notification-modal" id="notificationModal" style="display: none;">
    <div class="notification-modal-content card">
        <div class="notification-modal-header">
            <span>Error Occurred</span>
            <span class="notification-modal-close modalClose">&times;</span>
        </div>
        <p class="notification-modal-message" id="modalMessage"></p>
        <button class="button-secondary" id="modalOk">OK</button>
    </div>
</div>
<%--Get notifications from request--%>
<%
    List<Notification> notifications =
            NotificationStorage.getAndClearNotifications(session) ;
    JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

    for (Notification notification : notifications) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                .add("type", notification.type().toString())
                .add("message", notification.message());

        jsonArrayBuilder.add(jsonObjectBuilder);
    }

    String notificationsJson = jsonArrayBuilder.build().toString();
%>
<script>

    // Process notifications on page load
    document.addEventListener('DOMContentLoaded', function() {
        const notifications = <%= notificationsJson != null ? notificationsJson : "[]" %>;
        console.log("Notifications: ",notifications)
        notifications.forEach(notification => {
            switch (notification.type) {
                case 'SUCCESS':
                    showToast(notification.message, 'success');
                    break;
                case 'ERROR':
                    showToast(notification.message, 'error');
                    break;
                case 'VALIDATION':
                    showModal(notification.message);
                    break;
            }
        });
    });
</script>
<script src="${pageContext.request.contextPath}/js/jQuery.js"></script>
<script src="${pageContext.request.contextPath}/js/notifications.js"></script>

