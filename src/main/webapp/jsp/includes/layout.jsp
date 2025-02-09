<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<!-- Header -->
<header class="admin-header">
    <div class="header__toggle">
        <i class="bx bx-menu" id="sidebarToggle"></i>
    </div>
    <div class="header__content">
        <div class="header__title">XYZ Academy</div>

        </div>
    </div>
</header>

<!-- Sidebar -->
<aside class="sidebar">
    <ul class="sidebar__menu">
        <li>
            <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
        </li>
        <li>
            <a href="<%= request.getContextPath() %>/students">Students</a>
        </li>
        <li>
            <a href="<%= request.getContextPath() %>/exams">Exams</a>
        </li>
        <li>
            <a href="<%= request.getContextPath() %>/results">Results</a>
        </li>
    </ul>
</aside>

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
