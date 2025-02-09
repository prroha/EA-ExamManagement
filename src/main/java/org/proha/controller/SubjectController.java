package org.proha.controller;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proha.exceptions.ValidationException;
import org.proha.model.dto.SubjectDTO;
import org.proha.notification.model.NotificationType;
import org.proha.notification.service.NotificationService;
import org.proha.service.SubjectService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.ServletException;
import org.proha.utils.CommonMethods;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/subjects/*")
public class SubjectController extends BaseController {

    @Inject
    private SubjectService courseService;

    @Inject
    private NotificationService notificationService;

    private static final String SUBJECT_LIST_PAGE = "/jsp/subject/subjectList.jsp";
    private static final String SUBJECT_FORM_PAGE = "/jsp/subject/subjectCreateOrEdit.jsp";

    private static final Logger LOGGER = Logger.getLogger(SubjectController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/subjects")) {
                handleListing(request, response);
            } else if (pathInfo.equals("/new")) {
                handleShowForm(request, response, false, null);
            } else if (pathInfo.startsWith("/edit")) {
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

            List<SubjectDTO> subjects = courseService.findAll(page, pageSize);
            int total = courseService.count();
            int totalPages = (int) Math.ceil((double) total / pageSize);
            request.setAttribute("subjects", subjects);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            LOGGER.info("Listed Subjects for page " + page + " with size " + pageSize);
            forwardToPage(request, response, SUBJECT_LIST_PAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing subjects", e);
            notificationService.notify( "Failed to retrieve subject list.", NotificationType.ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving subjects");
        }
    }

    private void handleShowForm(HttpServletRequest request, HttpServletResponse response, boolean isEdit, SubjectDTO subject)
            throws ServletException, IOException {
        request.setAttribute("subject", subject != null ? subject : new SubjectDTO());
        request.setAttribute("isEdit", isEdit);
        forwardToPage(request, response, SUBJECT_FORM_PAGE);
    }
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID courseId = CommonMethods.getUUIDParameter(request, "id");
        if (courseId != null) {

            Optional<SubjectDTO> courseDTO = courseService.findById(courseId);
            if (courseDTO.isPresent()) {
                handleShowForm(request, response, true, courseDTO.get());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Subject not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subject ID is required");
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

        UUID subjectId = CommonMethods.getUUIDParameter(request, "id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        SubjectDTO subjectDTO = null;

        try {
            subjectDTO = new SubjectDTO(subjectId, name, description);

            if (!isUpdate && subjectId == null) {
                courseService.create(subjectDTO);
                notificationService.notify( "Subject created successfully.", NotificationType.SUCCESS);
                LOGGER.info("Created subjects: " + subjectDTO);
            } else {
                courseService.update(subjectId, subjectDTO);
                notificationService.notify("Subject updated successfully.", NotificationType.SUCCESS);
                LOGGER.info("Updated subjects: " + subjectDTO);
            }

            response.sendRedirect(request.getContextPath() + "/subjects");
        } catch (ValidationException e) {
            handleException(request, response, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving/updating subject", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving/updating subject");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            UUID courseId = CommonMethods.getUUIDParameter(request, "id");
            if (courseId != null) {
            courseService.delete(courseId);
            notificationService.notify("Subject deleted successfully.", NotificationType.SUCCESS);
            response.setStatus(HttpServletResponse.SC_OK);
                LOGGER.info("Deleted subject with ID: " + courseId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subject ID is required");
            }
    }
}
