<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="/WEB-INF/tlds/custom" prefix="custom" %>

<!DOCTYPE html>
<html>
<head>
    <title><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Course</title>
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
<h1><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Course</h1>

<jsp:include page="/jsp/includes/notification.jsp" />
<div class="card">
    <form action="${pageContext.request.contextPath}/subjects/save" method="post">
        <input type="hidden" name="id" value="${subject.id != null ? subject.id : ''}"/>
        <c:if test="${isEdit}">
            <input type="hidden" name="_method" value="PUT" />
        </c:if>
        <input type="hidden" name="csrf_token" value="${csrfToken}" />

        <div class="form-group">
            <%--            <custom:formField id="name" value="${subject.name}" error="${errors.name}" />--%>

        </div>
        <div class="form-group">
            <label for="name">Name</label>
            <input value="${subject.name != null ? subject.name : ''}" id="name" name="name"
                   class="user-form__input form-control" placeholder="Enter your Name" required>
            <c:if test="${errors.name}">
                <span class="error">${errors.name}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="description">Description</label>
            <input value="${subject.description != null ? subject.description : ''}" id="description" name="description"
                   class="user-form__input form-control" placeholder="Enter your Description" required>
            <c:if test="${errors.description}">
                <span class="error">${errors.description}</span>
            </c:if>
        </div>

        <div class="button-container right">
            <a href="#"> <input type="submit" value="${isEdit ? 'Update' : 'Create'}" class=" button button-primary"/>
            </a>
            <a href="${pageContext.request.contextPath}/subjects"
               class="button button-secondary delete-button" onclick="return confirm('Unsaved changes will be lost. Continue?')">
                Cancel
            </a>
        </div>
    </form>
</div>


</body>
</html>
