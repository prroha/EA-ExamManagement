<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Result</title>
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
        <h2>Result Management</h2>
    </div>

    <div class="responsive-grid">
        <div class="card">
            <div class="card-header">
                <a href="<%= request.getContextPath() %>/results/new" class="button button-primary">Add Result</a>
            </div>

            <div class="">
                <table>
                    <thead>
                    <tr>
                        <th>Key</th>
                        <th>Student Name</th>
                        <th>Subject Name</th>
                        <th>Max Marks</th>
                        <th>Marks Obtained </th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody id="userTableBody">
                    <c:choose>
                        <c:when test="${not empty results}">
                            <c:forEach var="result" items="${results}">
                                <tr>
                                    <td><c:out value="${result.id.toString().substring(0,8)}" /></td>
                                    <td><c:out value="${result.student.name}" /></td>
                                    <td><c:out value="${result.subject.name}" /></td>
                                    <td><c:out value="${result.exam.maxMarks}" /></td>
                                    <td><c:out value="${result.marksObtained}" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/results/edit?id=${fn:escapeXml(result.id)}" class="button button-secondary edit-button">Edit</a>
                                        <a href="${pageContext.request.contextPath}/results/delete?id=${fn:escapeXml(result.id)}" class="button button-muted delete-button">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4">No results found.</td>
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
        if (confirm('Are you sure you want to delete this result?')) {
            fetch('${pageContext.request.contextPath}/results/delete?id=' + id, {
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
