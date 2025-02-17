package org.proha.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.proha.exceptions.ValidationException;
import org.proha.model.dto.UserDTO;
import org.proha.notification.model.NotificationType;
import org.proha.service.AuthService;
import org.proha.utils.CommonMethods;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/auth/*")
public class AuthController extends BaseController {

    @Inject
    private AuthService authService;

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    private static final String LOGIN_PAGE = "/jsp/auth/login.jsp";
    private static final String REGISTER_PAGE = "/jsp/auth/register.jsp";
    private static final String USER_LIST_PAGE = "/jsp/user/studentList.jsp";
    private static final String AUTHENTICATED_SESSION_KEY = "authenticatedUser";
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            switch (pathInfo) {
                case "/", "/login" -> checkAuthenticationAndSendToDashboard(request, response);
                case "/logout" -> handleLogout(request, response);
                case "/new" -> handleShowForm(request, response, false, null);
                case "/edit" -> handleEditForm(request, response);
                case "/register" -> forwardToPage(request, response, REGISTER_PAGE);
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
            }
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo.equals("/login")) {
                handleLogin(request, response);
            } else if (pathInfo.equals("/register")) {
                handleRegister(request, response);
            } else {
                handleCreate(request, response);
            }
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

    private void checkAuthenticationAndSendToDashboard(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AUTHENTICATED_SESSION_KEY) != null) {
                System.out.println(session.getAttribute(AUTHENTICATED_SESSION_KEY));
                response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            forwardToPage(request, response, LOGIN_PAGE);
        }
    }
    private void handleListing(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = CommonMethods.getPageParameter(request);
            int pageSize = CommonMethods.getPageSizeParameter(request);

            List<UserDTO> users = authService.findAll(page, pageSize);
            int totalStudents = authService.count();
            int totalPages = (int) Math.ceil((double) totalStudents / pageSize);

            request.setAttribute("users", users);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            LOGGER.info("Listed students for page " + page + " with size " + pageSize);
            forwardToPage(request, response, USER_LIST_PAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing courses", e);
            notificationService.notify("Failed to retrieve student list.", NotificationType.ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving Students");
        }
    }

    private void handleShowForm(HttpServletRequest request, HttpServletResponse response, boolean isEdit, UserDTO student)
            throws ServletException, IOException {
        request.setAttribute("user", student != null ? student : new UserDTO());
        request.setAttribute("isEdit", isEdit);
        forwardToPage(request, response, USER_LIST_PAGE);
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID studentId = CommonMethods.getUUIDParameter(request, "id");
        if (studentId != null) {
            Optional<UserDTO> userDto = authService.findById(studentId);
            if (userDto.isPresent()) {
                handleShowForm(request, response, true, userDto.get());
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
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        UUID userId = CommonMethods.getUUIDParameter(request, "id");

        UserDTO userDto = null;
        try {
            userDto = new UserDTO(userId, name, email, username, "");
            if (userId != null) {
                authService.update(userId, userDto);
                notificationService.notify("Student successfully updated", NotificationType.SUCCESS);
                LOGGER.info("Updated student: " + userDto);
            } else {
                authService.create(userDto);
                notificationService.notify("Student successfully created", NotificationType.SUCCESS);
                LOGGER.info("Created student: " + userDto);
            }
            response.sendRedirect(request.getContextPath() + "/students");
        } catch (ValidationException e) {
            handleException(request, response, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving/updating student", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving/updating student");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UUID studentId = CommonMethods.getUUIDParameter(request, "id");
        if (studentId != null) {
            authService.delete(studentId);
            response.setStatus(HttpServletResponse.SC_OK);
            notificationService.notify("Student successfully deleted", NotificationType.SUCCESS);
            LOGGER.info("Deleted student with ID: " + studentId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            boolean isAuthenticated = authService.authenticate(username, password);
            if (isAuthenticated) {
                request.getSession().setAttribute(AUTHENTICATED_SESSION_KEY, username);
                notificationService.notify("Login successful", NotificationType.SUCCESS);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                notificationService.notify("Invalid username or password", NotificationType.ERROR);
                forwardToPage(request, response, LOGIN_PAGE);
            }
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email") != null ? request.getParameter("email") : username;
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            if (!password.equals(confirmPassword)) {
                notificationService.notify("Passwords do not match", NotificationType.ERROR);
                forwardToPage(request, response, REGISTER_PAGE);
                return;
            }

            UserDTO userDto = new UserDTO(null, null, email, username, password);
            authService.register(userDto);
            notificationService.notify("Registration successful", NotificationType.SUCCESS);
            response.sendRedirect(request.getContextPath() + "/auth/login");
        } catch (ValidationException e) {
            handleException(request, response, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during registration");
        }
    }
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }
}