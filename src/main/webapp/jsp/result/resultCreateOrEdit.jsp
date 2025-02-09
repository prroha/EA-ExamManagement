<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="/WEB-INF/tlds/custom" prefix="custom" %>

<!DOCTYPE html>
<html>
<head>
    <title><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Result</title>
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
<h1><c:if test="${isEdit}">Edit</c:if><c:if test="${!isEdit}">Create</c:if> Result</h1>

<jsp:include page="/jsp/includes/notification.jsp" />
<div class="card">
    <form action="${pageContext.request.contextPath}/results/save" method="post">
        <div class="content-medium">
        <input type="hidden" name="id" value="${result.id != null ? result.id : ''}"/>
        <c:if test="${isEdit}">
            <input type="hidden" name="_method" value="PUT" />
        </c:if>
        <input type="hidden" name="csrf_token" value="${csrfToken}" />

        <div class="form-group">
            <label for="studentId">Student</label>
            <select id="studentId" name="studentId" class="form-control" required>
                <c:forEach var="student" items="${students}">
                    <option value="${student.id}" <c:if test="${result.studentId == student.id}">selected</c:if>>${student.name}</option>
                </c:forEach>
            </select>
            <c:if test="${errors.studentId}">
                <span class="error">${errors.studentId}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="subjectId">Subject</label>
            <select id="subjectId" name="subjectId" class="form-control" required>
                <c:forEach var="subject" items="${subjects}">
                    <option value="${subject.id}" <c:if test="${result.subjectId == subject.id}">selected</c:if>>${subject.name}</option>
                </c:forEach>
            </select>
            <c:if test="${errors.subjectId}">
                <span class="error">${errors.subjectId}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="examId">Exam</label>
            <select id="examId" name="examId" class="form-control" required>
                <c:forEach var="exam" items="${exams}">
                    <option value="${exam.id}" <c:if test="${result.examId == exam.id}">selected</c:if>>${exam.examTitle}</option>
                </c:forEach>
            </select>
            <c:if test="${errors.examId}">
                <span class="error">${errors.examId}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="marksObtained">Marks Obtained</label>
            <input type="number" value="${result.marksObtained != null ? result.marksObtained : ''}" id="marksObtained" name="marksObtained"
                   class="user-form__input form-control" placeholder="Enter Marks Obtained" required>
            <c:if test="${errors.marksObtained}">
                <span class="error">${errors.marksObtained}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="grade">Grade</label>
            <input value="${result.grade != null ? result.grade : ''}" id="grade" name="grade"
                   class="user-form__input form-control" placeholder="Enter Grade" required>
            <c:if test="${errors.grade}">
                <span class="error">${errors.grade}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="resultDate">Result Date</label>
            <input type="date" value="${result.resultDate != null ? result.resultDate : ''}" id="resultDate" name="resultDate"
                   class="user-form__input form-control" required>
            <c:if test="${errors.resultDate}">
                <span class="error">${errors.resultDate}</span>
            </c:if>
        </div>

        <div class="button-container right">
            <input type="submit" value="${isEdit ? 'Update' : 'Create'}" class="button button-primary"/>
            <a href="${pageContext.request.contextPath}/results"
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
        const examDateInput = document.getElementById('resultDate');

        examDateInput.addEventListener('click', function() {
            if ('showPicker' in HTMLInputElement.prototype) {
                this.showPicker();
            }
        });
    });
</script>
