<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="/WEB-INF/tlds/custom" prefix="custom" %>

<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<div>
    <jsp:include page="/jsp/includes/layout.jsp"/>
</div>

<main class="main-content-center">
    <h1>Register</h1>

    <jsp:include page="/jsp/includes/notification.jsp" />
    <div class="card">
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="content-medium">
                <input type="hidden" name="csrf_token" value="${csrfToken}" />

                <div class="form-group">
                    <label for="username">Username</label>
                    <input value="${user.username != null ? user.username : ''}" id="username" name="username"
                           class="user-form__input form-control" placeholder="Enter Username" required>
                    <c:if test="${errors.username}">
                        <span class="error">${errors.username}</span>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" value="${user.email != null ? user.email : ''}" id="email" name="email"
                           class="user-form__input form-control" placeholder="Enter Email" required>
                    <c:if test="${errors.email}">
                        <span class="error">${errors.email}</span>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password"
                           class="user-form__input form-control" placeholder="Enter Password" required>
                    <c:if test="${errors.password}">
                        <span class="error">${errors.password}</span>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword"
                           class="user-form__input form-control" placeholder="Confirm Password" required>
                    <c:if test="${errors.confirmPassword}">
                        <span class="error">${errors.confirmPassword}</span>
                    </c:if>
                </div>

                <div class="button-container right">
                    <input type="submit" value="Register" class="button button-primary"/>
                    <a href="${pageContext.request.contextPath}/auth/login"
                       class="button button-muted">
                        Already have an account? Login
                    </a>
                </div>
            </div>
        </form>
    </div>
</main>
</body>
</html>