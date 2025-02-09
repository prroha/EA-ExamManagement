package org.proha.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proha.exceptions.ValidationException;
import org.proha.model.dto.ExamDTO;
import org.proha.model.dto.SubjectDTO;
import org.proha.notification.model.NotificationType;
import org.proha.notification.service.NotificationService;
import org.proha.service.ExamService;
import org.proha.service.SubjectService;
import org.proha.utils.CommonMethods;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/exams/*")
public class ExamController extends BaseController {

    @Inject
    private ExamService service;

    @Inject
    private SubjectService subjectService;
    @Inject
    private NotificationService notificationService;

    private static final Logger LOGGER = Logger.getLogger(ExamController.class.getName());

    private static final String EXAM_LIST_PAGE = "/jsp/exam/examList.jsp";
    private static final String EXAM_FORM_PAGE = "/jsp/exam/examCreateOrEdit.jsp";

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/exams")){
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

            List<ExamDTO> exams = service.findAll(page, pageSize);
            int total = service.count();
            int totalPages = (int) Math.ceil((double) total / pageSize);

            request.setAttribute("exams", exams);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            LOGGER.info("Listed Exams for page " + page + " with size " + pageSize);
            forwardToPage(request, response, EXAM_LIST_PAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing exams", e);
            notificationService.notify( "Failed to retrieve exam list.", NotificationType.ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving exams");
        }
    }

    private void handleShowForm(HttpServletRequest request, HttpServletResponse response, boolean isEdit, ExamDTO exam)
            throws ServletException, IOException {
        int page = CommonMethods.getPageParameter(request);
        int pageSize = CommonMethods.getPageSizeParameter(request);
        int total = subjectService.count();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        List<SubjectDTO> subjects = subjectService.findAll(page, pageSize);
        request.setAttribute("exam", exam != null ? exam : new ExamDTO());
        request.setAttribute("subjects", subjects);
        request.setAttribute("isEdit", isEdit);
        forwardToPage(request, response, EXAM_FORM_PAGE);
    }
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID examId = CommonMethods.getUUIDParameter(request, "id");
        if (examId != null) {

            Optional<ExamDTO> examDTO = service.findById(examId);
            if (examDTO.isPresent()) {
                handleShowForm(request, response, true, examDTO.get());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exam not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Exam ID is required");
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

        UUID examId = CommonMethods.getUUIDParameter(request, "id");
        UUID subjectId = CommonMethods.getUUIDParameter(request, "subjectId");
        String examTitle = request.getParameter("examTitle");
        String examRemarks = request.getParameter("examRemarks");

        Integer maxMarks = Integer.parseInt(request.getParameter("maxMarks"));
        LocalDate examDate = LocalDate.parse(request.getParameter("examDate"));
        ExamDTO examDTO = null;

        try {
            examDTO = new ExamDTO(examId, subjectId, examTitle, "", examDate,  maxMarks, examRemarks);

            if (examId == null) {
                service.create(examDTO);
                notificationService.notify( "Exam created successfully.", NotificationType.SUCCESS);
                LOGGER.info("Created exams: " + examDTO);
            } else {
                service.update(examId, examDTO);
                notificationService.notify("Exam updated successfully.", NotificationType.SUCCESS);
                LOGGER.info("Updated exams: " + examDTO);
            }

            response.sendRedirect(request.getContextPath() + "/exams");
        } catch (ValidationException e) {
            handleException(request, response, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving/updating exam", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving/updating exam");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID examId = CommonMethods.getUUIDParameter(request, "id");
        if (examId != null) {
            service.delete(examId);
            notificationService.notify("Exam deleted successfully.", NotificationType.SUCCESS);
            response.setStatus(HttpServletResponse.SC_OK);
            LOGGER.info("Deleted exam with ID: " + examId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Exam ID is required");
        }
    }
}
