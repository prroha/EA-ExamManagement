<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="/WEB-INF/tlds/custom" prefix="custom" %>

<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
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
  <h1>Login</h1>

  <jsp:include page="/jsp/includes/notification.jsp" />
  <div class="card">
    <form action="${pageContext.request.contextPath}/login" method="post">
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
          <label for="password">Password</label>
          <input type="password" id="password" name="password"
                 class="user-form__input form-control" placeholder="Enter Password" required>
          <c:if test="${errors.password}">
            <span class="error">${errors.password}</span>
          </c:if>
        </div>

        <div class="button-container right">
          <input type="submit" value="Login" class="button button-primary"/>
          <a href="${pageContext.request.contextPath}/auth/register"
             class="button button-muted">
            Don't have an account? Register
          </a>
        </div>
      </div>
    </form>
  </div>
</main>
</body>
</html>