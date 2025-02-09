<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="/WEB-INF/tlds/custom" prefix="custom" %>

<!DOCTYPE html>
<html>
<head>
    <title><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Exam</title>
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
<h1><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Exam</h1>

<jsp:include page="/jsp/includes/notification.jsp" />
<div class="card">
    <form action="${pageContext.request.contextPath}/exams/save" method="post">
        <div class="content-medium">
        <input type="hidden" name="id" value="${exam.id != null ? exam.id : ''}"/>
        <c:if test="${isEdit}">
            <input type="hidden" name="_method" value="PUT" />
        </c:if>
        <input type="hidden" name="csrf_token" value="${csrfToken}" />

        <div class="form-group">
            <label for="name">Exam Title</label>
            <input value="${exam.examTitle != null ? exam.examTitle : ''}" id="name" name="examTitle"
                   class="user-form__input form-control" placeholder="Enter Exam Title" required>
            <c:if test="${errors.name}">
                <span class="error">${errors.name}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="description">Remarks</label>
            <input value="${exam.examRemarks != null ? exam.examRemarks : ''}" id="description" name="examRemarks"
                   class="user-form__input form-control" placeholder="Enter Exam Remarks">
            <c:if test="${errors.examRemarks}">
                <span class="error">${errors.examRemarks}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="subjectId">Subject</label>
            <select id="subjectId" name="subjectId" class="form-control" required>
                <c:forEach var="subject" items="${subjects}">
                    <option value="${subject.id}" <c:if test="${exam.subjectId == subject.id}">selected</c:if>>${subject.name}</option>
                </c:forEach>
            </select>
            <c:if test="${errors.subjectId}">
                <span class="error">${errors.subjectId}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="examDate">Exam Date</label>
            <input type="date" value="${exam.examDate != null ? exam.examDate : ''}" id="examDate" name="examDate"
                   class="user-form__input form-control" required>
            <c:if test="${errors.examDate}">
                <span class="error">${errors.examDate}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="maxMarks">Max Marks</label>
            <input type="number" value="${exam.maxMarks != null ? exam.maxMarks : ''}" id="maxMarks" name="maxMarks"
                   class="user-form__input form-control" placeholder="Enter Max Marks" required>
            <c:if test="${errors.maxMarks}">
                <span class="error">${errors.maxMarks}</span>
            </c:if>
        </div>

        <div class="button-container right">
            <input type="submit" value="${isEdit ? 'Update' : 'Create'}" class="button button-primary"/>
            <a href="${pageContext.request.contextPath}/exams"
               class="button button-muted " >
                Cancel
            </a>
        </div>
        </div>
    </form>
</div>
</main>
</body>
</html>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const examDateInput = document.getElementById('examDate');

        examDateInput.addEventListener('click', function() {
            if ('showPicker' in HTMLInputElement.prototype) {
                this.showPicker();
            }
        });
    });
</script>