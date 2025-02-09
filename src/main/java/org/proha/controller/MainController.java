package org.proha.controller;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.proha.model.dto.ResultDTO;
import org.proha.model.dto.StudentDTO;
import org.proha.model.dto.SubjectDTO;
import org.proha.notification.model.NotificationType;
import org.proha.notification.service.NotificationService;
import org.proha.service.SubjectService;
import org.proha.service.ResultService;
import org.proha.service.StudentService;
import org.proha.utils.CommonMethods;

import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class MainController extends BaseController {

    @Inject
    private NotificationService notificationService;

    @Inject
    private StudentService studentService;

    @Inject
    private SubjectService subjectService;

    @Inject
    private ResultService enrollmentService;
    private static final String AUTHENTICATED_SESSION_KEY = "authenticatedUser";
    private static final String DASHBOARD_PAGE = "/jsp/dashboard/dashboard.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        try {
            if (requestURI.endsWith(".css") || requestURI.endsWith(".js")) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
                checkAuthenticationAndSendToDashboard(request, response);
        } catch (Exception e) {
            notificationService.notify("An error occurred: " + e.getMessage(), NotificationType.ERROR);
        }
    }


    private void checkAuthenticationAndSendToDashboard(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AUTHENTICATED_SESSION_KEY) != null) {
            if (pathInfo == null || pathInfo.equals("/home") || pathInfo.equals("/dashboard")) {
                setDashboardData(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }

    private void setDashboardData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = CommonMethods.getPageParameter(request);
        int pageSize = CommonMethods.getPageSizeParameter(request);

        List<StudentDTO> students = studentService.findAll(page, pageSize);
        List<SubjectDTO> courses = subjectService.findAll(page, pageSize);
        List<ResultDTO> enrollments = enrollmentService.findAll(page, pageSize);

        if(students == null || courses == null || enrollments == null) {
            notificationService.notify("An error occurred while fetching data", NotificationType.ERROR);
            forwardToPage(request, response, DASHBOARD_PAGE);
            return;
        }
        // Serialize students
        JsonArrayBuilder studentsArrayBuilder = Json.createArrayBuilder();
        for (StudentDTO student : students) {
            JsonObjectBuilder studentObjectBuilder = Json.createObjectBuilder()
                    .add("id", student.getId().toString())
                    .add("name", student.getName())
                    .add("email", student.getEmail());
            studentsArrayBuilder.add(studentObjectBuilder);
        }
        JsonArray studentsJson = studentsArrayBuilder.build();

        // Serialize courses
        JsonArrayBuilder coursesArrayBuilder = Json.createArrayBuilder();
        for (SubjectDTO course : courses) {
            JsonObjectBuilder courseObjectBuilder = Json.createObjectBuilder()
                    .add("id", course.getId().toString())
                    .add("name", course.getName());
            coursesArrayBuilder.add(courseObjectBuilder);
        }
        JsonArray coursesJson = coursesArrayBuilder.build();

        // Serialize enrollments
        JsonArrayBuilder enrollmentsArrayBuilder = Json.createArrayBuilder();
        for (ResultDTO enrollment : enrollments) {
            JsonObjectBuilder enrollmentObjectBuilder = Json.createObjectBuilder()
                    .add("id", enrollment.getId().toString())
                    .add("studentId", enrollment.getStudentId().toString())
                    .add("courseId", enrollment.getExamId().toString());
            enrollmentsArrayBuilder.add(enrollmentObjectBuilder);
        }
        JsonArray enrollmentsJson = enrollmentsArrayBuilder.build();

        System.out.println("Students::: " + studentsJson.toString());
        System.out.println("Courses::: " + coursesJson.toString());
        System.out.println("Enrollments::: " + enrollmentsJson.toString());

        request.setAttribute("students", studentsJson.toString());
        request.setAttribute("courses", coursesJson.toString());
        request.setAttribute("enrollments", enrollmentsJson.toString());

            forwardToPage(request, response, DASHBOARD_PAGE);
    }
}