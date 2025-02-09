package org.proha.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.proha.model.dto.ResultDTO;
import org.proha.model.dto.UserDTO;
import org.proha.model.entity.Result;
import org.proha.model.entity.Student;
import org.proha.model.entity.User;
import org.proha.repository.ExamRepository;
import org.proha.repository.ResultRepository;
import org.proha.repository.StudentRepository;
import org.proha.repository.UserRepository;
import org.proha.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
public class AuthService {

    @Inject
    private UserRepository repository;

    @Inject
    private UserMapper mapper;
    @Inject
    private PasswordConfig passwordConfig;
    private Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    /**
     * Get the total count of students.
     *
     * @return Total number of students
     */
    public int count() {
        return repository.count();
    }

    /**
     * Get a paginated list of students.
     *
     * @param page     The page number (1-based index)
     * @param pageSize The size of the page
     * @return List of UserDtos
     */
    public List<UserDTO> findAll(int page, int pageSize) {
        LOGGER.info("Fetching users - page: {}, pageSize: {}", page, pageSize);
        List<User> users = repository.findAll(page, pageSize);
        return users.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a user by ID.
     *
     * @param id The ID of the student
     * @return Optional containing the UserDto if found
     */
    public Optional<UserDTO> findById(UUID id) {
        LOGGER.info("Fetching user by ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Find a student by email.
     *
     * @param username The username of the student
     * @return Optional containing the UserDto if found
     */
    public Optional<UserDTO> findByUsername(String username) {
        LOGGER.info("Fetching student by email: {}", username);
        return repository.findByUsername(username)
                .map(mapper::toDTO);
    }

    /**
     * Create a new student.
     *
     * @param dto The student data
     */
    @Transactional
    public void create(UserDTO dto) {
        LOGGER.info("Creating student: {}", dto);
        try {
            User user = mapper.toEntity(dto);
            repository.save(user);
            LOGGER.info("Student created successfully: {}", user);
        } catch (Exception e) {
            LOGGER.error("Error while creating student: {}", dto, e);
            throw new ServiceException("Unable to create user", e);
        }
    }

    /**
     * Update an existing student.
     *
     * @param id  The ID of the student to update
     * @param dto The updated student data
     */
    @Transactional
    public void update(UUID id, UserDTO dto) {
        LOGGER.info("Updating student: ID = {}, Data = {}", id, dto);
        try {
            User existingStudent = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));
            existingStudent.setName(dto.getName());
            existingStudent.setEmail(dto.getEmail());
            existingStudent.setPhone(dto.getPhone());
            repository.update(existingStudent);
            LOGGER.info("Student updated successfully: {}", existingStudent);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Failed to update student - not found: ID = {}", id, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while updating student: ID = {}, Data = {}", id, dto, e);
            throw new ServiceException("Unable to update student", e);
        }
    }

    /**
     * Delete a student.
     *
     * @param id The ID of the student to delete
     */
    @Transactional
    public void delete(UUID id) {
        LOGGER.info("Deleting student with ID: {}", id);
        try {
            repository.delete(id);
            LOGGER.info("Student deleted successfully: ID = {}", id);
        } catch (Exception e) {
            LOGGER.error("Error while deleting student: ID = {}", id, e);
            throw new ServiceException("Unable to delete student", e);
        }
    }

    /**
     * Create a new user (Registration).  Hashes the password.
     *
     * @param dto The user data (must include plain text password)
     * @throws ServiceException if user creation fails.
     */
    @Transactional
    public void register(UserDTO dto) {
        LOGGER.info("Registering user: {}", dto.getUsername());
        try {
            String hashedPassword = passwordConfig.hashPassword(dto.getPassword());

            User user = mapper.toEntity(dto);
            user.setPassword(hashedPassword);

            repository.save(user);

            LOGGER.info("User registered successfully: {}", user.getUsername());
        } catch (Exception e) {
            LOGGER.error("Error while registering user: {}", dto.getUsername(), e);
            throw new ServiceException("Unable to register user", e);
        }
    }

    /**
     * Authenticates a user (Login).
     *
     * @param username The username of the user trying to login
     * @param password The password of the user trying to login
     * @return Optional containing the UserDTO if login is successful, empty Optional otherwise.
     */
    public boolean authenticate(String username, String password) {
        LOGGER.info("Attempting login for user: {}", username);

        try {
            Optional<User> userOptional = repository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                if (passwordConfig.verifyPassword(password, user.getPassword())) {
                    LOGGER.info("User {} logged in successfully.", username);
                    return true;
                } else {
                    LOGGER.warn("Incorrect password for user: {}", username);
                    return false;
                }
            } else {
                LOGGER.warn("User not found: {}", username);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error during login for user: {}", username, e);
            return false;
        }
    }


}
