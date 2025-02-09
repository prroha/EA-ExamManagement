package org.proha.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.proha.model.dto.ResultDTO;
import org.proha.model.dto.StudentDTO;
import org.proha.model.entity.Result;
import org.proha.model.entity.Student;
import org.proha.repository.ExamRepository;
import org.proha.repository.ResultRepository;
import org.proha.repository.StudentRepository;
import org.proha.utils.ExamMapper;
import org.proha.utils.ResultMapper;
import org.proha.utils.StudentMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
@Stateless
public class StudentService {

    @Inject
    private StudentRepository repository;

    @Inject
    private ExamRepository examRepository;

    @Inject
    private ResultRepository resultRepository;

    @Inject
    private StudentMapper mapper;

    @Inject
    private ResultMapper resultMapper;

    @Inject
    private ExamMapper examMapper;
    private Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

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
     * @return List of StudentDTOs
     */
    public List<StudentDTO> findAll(int page, int pageSize) {
        LOGGER.info("Fetching students - page: {}, pageSize: {}", page, pageSize);
        List<Student> students = repository.findAll(page, pageSize);
        return students.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a student by ID.
     *
     * @param id The ID of the student
     * @return Optional containing the StudentDTO if found
     */
    public Optional<StudentDTO> findById(UUID id) {
        LOGGER.info("Fetching student by ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Find a student by email.
     *
     * @param email The email of the student
     * @return Optional containing the StudentDTO if found
     */
    public Optional<StudentDTO> findByEmail(String email) {
        LOGGER.info("Fetching student by email: {}", email);
        return repository.findByEmail(email)
                .map(mapper::toDTO);
    }

    /**
     * Create a new student.
     *
     * @param dto The student data
     */
    @Transactional
    public void create(StudentDTO dto) {
        LOGGER.info("Creating student: {}", dto);
        try {
            Student student = mapper.toEntity(dto);
            repository.save(student);
            LOGGER.info("Student created successfully: {}", student);
        } catch (Exception e) {
            LOGGER.error("Error while creating student: {}", dto, e);
            throw new ServiceException("Unable to create student", e);
        }
    }

    /**
     * Update an existing student.
     *
     * @param id  The ID of the student to update
     * @param dto The updated student data
     */
    @Transactional
    public void update(UUID id, StudentDTO dto) {
        LOGGER.info("Updating student: ID = {}, Data = {}", id, dto);
        try {
            Student existingStudent = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));
            existingStudent.setName(dto.getName());
            existingStudent.setEmail(dto.getEmail());
            existingStudent.setPhone(dto.getPhone());
            existingStudent.setAddress(mapper.mapAddressToEntity(dto.getAddress()));
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
     * Get results for a student.
     *
     * @param studentId The ID of the student
     * @return List of Results
     */
    public List<ResultDTO> getResultsForStudent(UUID studentId) {
        LOGGER.info("Fetching results for student ID: {}", studentId);
        List<Result> results = resultRepository.findByStudentId(studentId);
        return results.stream()
                .map(result -> {
                    ResultDTO dto = resultMapper.toDTO(result);
                    dto.setExam(examMapper.toDTO(result.getExam()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Delete an result.
     *
     * @param resultId The ID of the result to delete
     */
    @Transactional
    public void deleteResult(UUID resultId) {
        LOGGER.info("Deleting result with ID: {}", resultId);
        try {
            resultRepository.delete(resultId);
            LOGGER.info("Result deleted successfully: ID = {}", resultId);
        } catch (Exception e) {
            LOGGER.error("Error while deleting result: ID = {}", resultId, e);
            throw new ServiceException("Unable to delete result", e);
        }
    }
}
