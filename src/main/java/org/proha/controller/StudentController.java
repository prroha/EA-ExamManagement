package org.proha.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proha.exceptions.ValidationException;
import org.proha.model.dto.ExamDTO;
import org.proha.model.dto.ResultDTO;
import org.proha.model.dto.StudentDTO;
import org.proha.notification.model.NotificationType;
import org.proha.service.SubjectService;
import org.proha.service.ResultService;
import org.proha.service.StudentService;
import org.proha.utils.CommonMethods;

@WebServlet("/students/*")
public class StudentController extends BaseController {

    @Inject
    private StudentService studentService;

    @Inject
    private SubjectService courseService;

    @Inject
    private ResultService enrollmentService;

    private static final Logger LOGGER = Logger.getLogger(StudentController.class.getName());

    private static final String STUDENT_LIST_PAGE = "/jsp/student/studentList.jsp";
    private static final String STUDENT_FORM_PAGE = "/jsp/student/studentCreateOrEdit.jsp";

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/students")) {
                handleListing(request, response);
            } else if (pathInfo.equals("/new")) {
                handleShowForm(request, response, false, null);
            } else if (pathInfo.equals("/edit")) {
                handleEditForm(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
            }
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleCreate(request, response);
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleUpdate(request, response);
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/delete")) {
            handleDelete(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    private void handleListing(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = CommonMethods.getPageParameter(request);
            int pageSize = CommonMethods.getPageSizeParameter(request);

            List<StudentDTO> students = studentService.findAll(page, pageSize);
            int totalStudents = studentService.count();
            int totalPages = (int) Math.ceil((double) totalStudents / pageSize);

            request.setAttribute("students", students);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            LOGGER.info("Listed students for page " + page + " with size " + pageSize);
            forwardToPage(request, response, STUDENT_LIST_PAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing courses", e);
            notificationService.notify( "Failed to retrieve student list.", NotificationType.ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving Students");
        }
    }

    private void handleShowForm(HttpServletRequest request, HttpServletResponse response, boolean isEdit, StudentDTO student)
            throws ServletException, IOException {
        request.setAttribute("student", student != null ? student : new StudentDTO());
        request.setAttribute("isEdit", isEdit);
        forwardToPage(request, response, STUDENT_FORM_PAGE);
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID studentId = CommonMethods.getUUIDParameter(request, "id");
        if (studentId != null) {
            Optional<StudentDTO> studentDTO = studentService.findById(studentId);
            if (studentDTO.isPresent()) {
                handleShowForm(request, response, true, studentDTO.get());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required");
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleCreateOrUpdate(request, response, false);
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleCreateOrUpdate(request, response, true);
    }

    private void handleCreateOrUpdate(HttpServletRequest request, HttpServletResponse response, boolean isUpdate)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        UUID studentId = CommonMethods.getUUIDParameter(request, "id");

        StudentDTO studentDTO = null;
        try {
            studentDTO = new StudentDTO(studentId, name, email, phone, null);
            if (studentId != null) {
                studentService.update(studentId, studentDTO);
                notificationService.notify("Student successfully updated", NotificationType.SUCCESS);
                LOGGER.info("Updated student: " + studentDTO);
            } else {
                studentService.create(studentDTO);
                notificationService.notify("Student successfully created", NotificationType.SUCCESS);
                LOGGER.info("Created student: " + studentDTO);
            }
            response.sendRedirect(request.getContextPath() + "/students");
        } catch (ValidationException e) {
            handleException(request, response, e);
        }
     catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error saving/updating student", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving/updating student");
    }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UUID studentId = CommonMethods.getUUIDParameter(request, "id");
        if (studentId != null) {
            studentService.delete(studentId);
            response.setStatus(HttpServletResponse.SC_OK);
            notificationService.notify("Student successfully deleted", NotificationType.SUCCESS);
            LOGGER.info("Deleted student with ID: " + studentId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required");
        }
    }
}
