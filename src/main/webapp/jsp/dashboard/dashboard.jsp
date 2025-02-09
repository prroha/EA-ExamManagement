<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
<head>
    <title>Dashboard</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <script src="${pageContext.request.contextPath}/js/chart.umd.js"></script>
<%--    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>--%>

    <style>
        .dashboard-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }
        .dashboard-section {
            flex: 1;
            min-width: 300px;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: #fff;
        }
        .chart-container {
            position: relative;
            height: 300px;
        }
    </style>
</head>
<body>
<div>
    <jsp:include page="/jsp/includes/layout.jsp"/>
</div>
<main class="main-content-center">
<div class="dashboard-container">
    <div class="dashboard-section">
        <h2>Students</h2>
        <div class="chart-container">
            <canvas id="studentsChart"></canvas>
        </div>
    </div>
    <div class="dashboard-section">
        <h2>Subjects</h2>
        <div class="chart-container">
            <canvas id="subjectsChart"></canvas>
        </div>
    </div>
    <div class="dashboard-section">
        <h2>Results</h2>
        <div class="chart-container">
            <canvas id="resultsChart"></canvas>
        </div>
    </div>
    <div class="dashboard-section">
        <h2>Exams</h2>
        <div class="chart-container">
            <canvas id="examsChart"></canvas>
        </div>
    </div>

    <div class="dashboard-section">
    <c:if test="${not empty subjects}">
        <c:forEach var="subject" items="${subjects}">
            <option value="${subject.id}">${subject.name}</option>
        </c:forEach>
    </c:if>
        <c:if test="${empty subjects}">
            <option>No subjects available</option>
        </c:if>
    </div>
</div>
</main>

<script>document.addEventListener('DOMContentLoaded', function () {
    const studentsData = ${students != null ? students : '[]'};
    const subjectData = ${subjects != null ? subjects : '[]'};
    const resultData = ${results != null ? results : '[]'};
    const examData = ${exams != null ? exams : '[]'};

    const studentsChartCtx = document.getElementById('studentsChart').getContext('2d');
    const subjectsChartCtx = document.getElementById('subjectsChart').getContext('2d');
    const resultsChartCtx = document.getElementById('resultsChart').getContext('2d');
    const examsChartCtx = document.getElementById('examsChart').getContext('2d');

    // Students Bar Chart
    const studentsChart = new Chart(studentsChartCtx, {
        type: 'bar',
        data: {
            labels: studentsData.map(student => student.name),
            datasets: [{
                label: 'Number of Students',
                data: studentsData.map(student => student.count),
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    // Subjects Pie Chart
     new Chart(subjectsChartCtx, {
        type: 'pie',
        data: {
            labels: subjectData.map(subject => subject.name),
            datasets: [{
                label: 'Number of Subjects',
                data: subjectData.map(subject => subject.count),
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)'
                ],
                borderWidth: 1
            }]
        }
    });

    // Results Line Chart
     new Chart(resultsChartCtx, {
        type: 'line',
        data: {
            labels: resultData.map(result => result.date),
            datasets: [{
                label: 'Number of Results',
                data: resultData.map(result => result.count),
                backgroundColor: 'rgba(153, 102, 255, 0.2)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 1,
                fill: true // Optional for filling under the line
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    // Exams Line Chart
    new Chart(examsChartCtx, {
        type: 'line',
        data: {
            labels: examData.map(exam => exam.date),
            datasets: [{
                label: 'Number of Exams',
                data: examData.map(exam => exam.count),
                backgroundColor: 'rgba(153, 102, 255, 0.2)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 1,
                fill: true // Optional for filling under the line
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});

</script>
</body>
</html>