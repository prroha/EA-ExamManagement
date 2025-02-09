<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="/WEB-INF/tlds/custom" prefix="custom" %>

<!DOCTYPE html>
<html>
<head>
    <title><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Student</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        .tab-container {
            display: flex;
            border-bottom: 1px solid #ccc;
        }
        .tab {
            padding: 10px 20px;
            cursor: pointer;
        }
        .tab.active {
            border-bottom: 2px solid #000;
            font-weight: bold;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
        }
    </style>
</head>
<body>
<div>
    <jsp:include page="/jsp/includes/layout.jsp"/>
</div>

<main class="main-content-center">
<h1><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Student</h1>

<jsp:include page="/jsp/includes/notification.jsp" />
<div class="card">
<%--    <div class="tab-container">--%>
<%--        <div class="tab active" data-tab="student">Student</div>--%>
<%--        <div class="tab" data-tab="enrollment">Enrollment</div>--%>
<%--    </div>--%>
<%--    <div id="student" class="tab-content active">--%>
        <form action="${pageContext.request.contextPath}/students/save" method="post">
            <div class="content-medium">
            <input type="hidden" name="id" value="${student.id != null ? student.id : ''}"/>
            <c:if test="${isEdit}">
                <input type="hidden" name="_method" value="PUT" />
            </c:if>
            <input type="hidden" name="csrf_token" value="${csrfToken}" />

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="user-form__input form-control"
                       value="${student.email != null ? student.email : ''}"
                       placeholder="Enter your email" required>
                <c:if test="${errors.email}">
                    <span class="error">${errors.email}</span>
                </c:if>
            </div>
            <div class="form-group">
                <label for="name">Name</label>
                <input value="${student.name != null ? student.name : ''}" id="name" name="name"
                       class="user-form__input form-control" placeholder="Enter your Name" required>
                <c:if test="${errors.name}">
                    <span class="error">${errors.name}</span>
                </c:if>
            </div>

            <div class="button-container right">
                <input type="submit" value="${isEdit ? 'Update' : 'Create'}" class="button button-primary"/>
                <a href="${pageContext.request.contextPath}/students"
                   class="button button-muted " >
                    Cancel
                </a>
            </div>
            </div>
        </form>
<%--        </div>--%>
    </div>
<%--    --%>
<%--    <div id="enrollment" class="tab-content">--%>
<%--        <c:if test="${isEdit}">--%>
<%--            <h2>Manage Enrollments</h2>--%>
<%--            <div class="form-group">--%>
<%--                <!-- List Current Enrollments -->--%>
<%--                <label>Current Enrollments:</label>--%>
<%--                <c:if test="${not empty enrollments}">--%>
<%--                    <ul>--%>
<%--                        <c:forEach var="enrollment" items="${enrollments}">--%>
<%--                            <li>--%>
<%--                                    ${enrollment.course.name}--%>
<%--                                <form action="${pageContext.request.contextPath}/enrollments/remove" method="post" style="display:inline;">--%>
<%--                                    <input type="hidden" name="enrollmentId" value="${enrollment.id}" />--%>
<%--                                    <input type="hidden" name="csrf_token" value="${csrfToken}" />--%>
<%--                                    <button type="submit" class="button button-secondary">Remove</button>--%>
<%--                                </form>--%>
<%--                            </li>--%>
<%--                        </c:forEach>--%>
<%--                    </ul>--%>
<%--                </c:if>--%>
<%--                <c:if test="${empty enrollments}">--%>
<%--                    <p>No enrollments found for this student.</p>--%>
<%--                </c:if>--%>
<%--            </div>--%>
<%--            <!-- Add Enrollment -->--%>
<%--            <div class="form-group">--%>
<%--                <form action="${pageContext.request.contextPath}/enrollments/add" method="post" style="display:inline;">--%>
<%--                    <label for="course">Add Course:</label>--%>
<%--                    <select id="course" name="courseId" class="form-control">--%>
<%--                        <c:if test="${not empty courses}">--%>
<%--                            <c:forEach var="course" items="${courses}">--%>
<%--                                <option value="${course.id}">${course.name}</option>--%>
<%--                            </c:forEach>--%>
<%--                        </c:if>--%>

<%--                        <c:if test="${empty courses}">--%>
<%--                            <option>No courses available</option>--%>
<%--                        </c:if>--%>
<%--                    </select>--%>
<%--                    <input type="hidden" name="studentId" value="${student.id}" />--%>
<%--                    <input type="hidden" name="csrf_token" value="${csrfToken}" />--%>
<%--                    <button type="submit" class="button button-primary">Add</button>--%>
<%--                </form>--%>
<%--            </div>--%>
<%--        </c:if>--%>
<%--    </div>--%>
<%--    --%>
</div>

</main>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const tabs = document.querySelectorAll('.tab');
        const tabContents = document.querySelectorAll('.tab-content');

        tabs.forEach(tab => {
            tab.addEventListener('click', function () {
                tabs.forEach(t => t.classList.remove('active'));
                tabContents.forEach(tc => tc.classList.remove('active'));

                tab.classList.add('active');
                document.getElementById(tab.getAttribute('data-tab')).classList.add('active');
            });
        });
    });
</script>
</body>
</html>