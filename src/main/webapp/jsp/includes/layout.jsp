<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<!-- Header -->
<header class="admin-header">
    <div class="header__left" style="align-items: center;">
        <%
            Object user = session.getAttribute("authenticatedUser");
            if (user != null) {
        %>
        <div class="header__toggle">
            <i class="bx bx-menu" id="sidebarToggle"></i>
        </div>
        <%
            }
        %>
        <div class="header__title">XYZ Academy</div>
    </div>
    <div class="header__right" style="display: inline-flex; gap: 10px; align-items: center;">
        <%
            if (user != null) {
        %>
        <p>Welcome, <%= user.toString() %>!</p>
        <form action="${pageContext.request.contextPath}/auth/logout" method="get">
            <button type="submit" class="button button-primary">Logout</button>
        </form>
        <%
        } else {
        %>
        <form action="${pageContext.request.contextPath}/auth/login" method="get">
            <button type="submit" class="button button-primary">Login</button>
        </form>
        <form action="${pageContext.request.contextPath}/auth/register" method="get">
            <button type="submit" class="button button-secondary">Register</button>
        </form>
        <%
            }
        %>
    </div>
</header>

<!-- Sidebar -->

<%
    if (user != null) {
%>
<aside class="sidebar">
    <ul class="sidebar__nav">
        <li class="sidebar__item">
            <a class="sidebar__link" href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
        </li>
        <li class="sidebar__item">
            <a class="sidebar__link" href="<%= request.getContextPath() %>/students">Students</a>
        </li>
        <li class="sidebar__item">
            <a class="sidebar__link" href="<%= request.getContextPath() %>/exams">Exams</a>
        </li>
        <li class="sidebar__item">
            <a class="sidebar__link" href="<%= request.getContextPath() %>/results">Results</a>
        </li>
    </ul>
</aside>
<%
    }
%>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const toggle = document.querySelector('.header__toggle');
        const sidebar = document.querySelector('.sidebar');

        toggle.addEventListener('click', function () {
            sidebar.classList.toggle('sidebar--active');
        });
    });
</script>
</body>
</html>