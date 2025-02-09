<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<div>
    <jsp:include page="/jsp/includes/layout.jsp"/>
</div>
<main class="main-content-center">
    <div class="page-title">
        <h2>Student Management</h2>
    </div>

    <div class="responsive-grid">
        <div class="card">
            <div class="card-header right">
                <a href="<%= request.getContextPath() %>/students/new" class="button button-primary">Add Student</a>
            </div>

            <div class="">
<%--                <h3>User List</h3>--%>
                <table>
                    <thead>
                    <tr>
                        <th>StudentId</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody id="userTableBody">
                    <c:choose>
                        <c:when test="${not empty students}">
                            <c:forEach var="student" items="${students}">
                                <tr>
                                    <td><c:out value="${student.id.toString().substring(0,8)}" /></td>
                                    <td><c:out value="${student.name}" /></td>
                                    <td><c:out value="${student.email}" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/students/edit?id=${fn:escapeXml(student.id)}" class="button button-secondary edit-button">Edit</a>
                                        <a href="${pageContext.request.contextPath}/students/delete?id=${fn:escapeXml(student.id)}" class="button button-muted delete-button">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4">No students found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<script>
    function deleteStudent(id) {
        if (confirm('Are you sure you want to delete this student?')) {
            fetch('${pageContext.request.contextPath}/students/delete?id=' + id, {
                method: 'DELETE'
            }).then(response => {
                if (response.ok) {
                    window.location.reload();
                }
            });
        }
    }
</script>

</body>
</html>
