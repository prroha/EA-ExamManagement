package org.proha.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proha.exceptions.ValidationException;
import org.proha.model.dto.ExamDTO;
import org.proha.model.dto.ResultDTO;
import org.proha.model.dto.StudentDTO;
import org.proha.model.dto.SubjectDTO;
import org.proha.notification.model.NotificationType;
import org.proha.notification.service.NotificationService;
import org.proha.service.ExamService;
import org.proha.service.ResultService;
import org.proha.service.StudentService;
import org.proha.service.SubjectService;
import org.proha.utils.CommonMethods;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/results/*")
public class ResultController extends BaseController {

    @Inject
    private ResultService resultService;
    @Inject
    private StudentService studentService;
    @Inject
    private SubjectService subjectService;
    @Inject
    private ExamService examService;

    @Inject
    private NotificationService notificationService;

    private static final Logger LOGGER = Logger.getLogger(ResultController.class.getName());

    private static final String RESULT_LIST_PAGE = "/jsp/result/resultList.jsp";
    private static final String RESULT_FORM_PAGE = "/jsp/result/resultCreateOrEdit.jsp";

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/results")){
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
        try {
            handleDelete(request, response);
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    private void handleListing(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = CommonMethods.getPageParameter(request);
            int pageSize = CommonMethods.getPageSizeParameter(request);

            List<ResultDTO> results = resultService.findAll(page, pageSize);
            int total = resultService.count();
            int totalPages = (int) Math.ceil((double) total / pageSize);
            request.setAttribute("results", results);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            LOGGER.info("Listed Results for page " + page + " with size " + pageSize);
            forwardToPage(request, response, RESULT_LIST_PAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing results", e);
            notificationService.notify( "Failed to retrieve result list.", NotificationType.ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving results");
        }
    }

    private void handleShowForm(HttpServletRequest request, HttpServletResponse response, boolean isEdit, ResultDTO result)
            throws ServletException, IOException {
        int page = CommonMethods.getPageParameter(request);
        int pageSize = CommonMethods.getPageSizeParameter(request);
        List<StudentDTO> students = studentService.findAll(page, pageSize);
        List<SubjectDTO> subjects = subjectService.findAll(page, pageSize);
        List<ExamDTO> exams = examService.findAll(page, pageSize);
        System.out.println("Students: " + students.size());
        System.out.println("Exams: " + exams.size());
        request.setAttribute("result", result != null ? result : new ResultDTO());
        request.setAttribute("students", students);
        request.setAttribute("subjects", subjects);
        request.setAttribute("exams", exams);
        request.setAttribute("isEdit", isEdit);
        forwardToPage(request, response, RESULT_FORM_PAGE);
    }
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID resultId = CommonMethods.getUUIDParameter(request, "id");
        if (resultId != null) {

            Optional<ResultDTO> result = resultService.findById(resultId);
            if (result.isPresent()) {
                handleShowForm(request, response, true, result.get());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Result not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Result ID is required");
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

        UUID resultId = CommonMethods.getUUIDParameter(request, "id");
        UUID examId = CommonMethods.getUUIDParameter(request, "examId");
        UUID studentId = CommonMethods.getUUIDParameter(request, "studentId");
        String grade = request.getParameter("grade");
        String remarks = request.getParameter("remarks");
        Integer marksObtained = Integer.parseInt(request.getParameter("marksObtained"));
        LocalDate resultDate = LocalDate.parse(request.getParameter("resultDate"));
        ResultDTO result = null;

        System.out.println("Result ID: " + resultId);
        System.out.println("Exam ID: " + examId);
        System.out.println("Student ID: " + studentId);
        try {
            result = new ResultDTO(resultId, studentId, examId, marksObtained, grade, resultDate, remarks);

            if (resultId == null) {
                resultService.create(result);
                notificationService.notify( "Result created successfully.", NotificationType.SUCCESS);
                LOGGER.info("Created results: " + result);
            } else {
                resultService.update(resultId, result);
                notificationService.notify("Result updated successfully.", NotificationType.SUCCESS);
                LOGGER.info("Updated results: " + result);
            }

            response.sendRedirect(request.getContextPath() + "/results");
        } catch (ValidationException e) {
            handleException(request, response, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving/updating result", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving/updating result");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID resultId = CommonMethods.getUUIDParameter(request, "id");
        if (resultId != null) {
            resultService.delete(resultId);
            notificationService.notify("Result deleted successfully.", NotificationType.SUCCESS);
            response.setStatus(HttpServletResponse.SC_OK);
            LOGGER.info("Deleted result with ID: " + resultId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Result ID is required");
        }
    }
}
