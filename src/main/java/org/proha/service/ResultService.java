package org.proha.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.proha.model.dto.ResultDTO;
import org.proha.model.entity.Result;
import org.proha.repository.SubjectRepository;
import org.proha.repository.ResultRepository;
import org.proha.repository.StudentRepository;
import org.proha.utils.ExamMapper;
import org.proha.utils.SubjectMapper;
import org.proha.utils.ResultMapper;
import org.proha.utils.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
public class ResultService {

    @Inject
    private ResultRepository repository;

    @Inject
    private StudentRepository studentRepository;

    @Inject
    private SubjectRepository subjectRepository;

    @Inject
    private ResultMapper mapper;
    @Inject
    private StudentMapper studentMapper;

    @Inject
    private ExamMapper examMapper;

    @Inject
    private SubjectMapper subjectMapper;

    private Logger LOGGER = LoggerFactory.getLogger(ResultService.class);


    public int count() {
        return repository.count();
    }

    /**
     * Get a paginated list of results.
     *
     * @param page     The page number (1-based index)
     * @param pageSize The size of the page
     * @return List of ResultDTOs
     */
    public List<ResultDTO> findAll(int page, int pageSize) {
        LOGGER.info("Fetching results - page: {}, pageSize: {}", page, pageSize);
        List<Result> entities = repository.findAll(page, pageSize);
        return entities.stream()
                .map(result -> {
                    ResultDTO dto = mapper.toDTO(result);
                    dto.setStudent(studentMapper.toDTO(result.getStudent()));
                    dto.setExam(examMapper.toDTO(result.getExam()));
                    dto.setSubject(subjectMapper.toDTO(result.getExam().getSubject()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Find a result by ID.
     *
     * @param id The ID of the result
     * @return Optional containing the ResultDTO if found
     */
    public Optional<ResultDTO> findById(UUID id) {
        LOGGER.info("Fetching result by ID: {}", id);
        return repository.findById(id)
                .map(result -> {
                    ResultDTO dto = mapper.toDTO(result);
                    dto.setStudent(studentMapper.toDTO(result.getStudent()));
                    dto.setExam(examMapper.toDTO(result.getExam()));
                    return dto;
                });
    }

    /**
     * Create a new result.
     *
     * @param dto The result data
     */
    @Transactional
    public void create(ResultDTO dto) {
        LOGGER.info("Creating result: {}", dto);
        try {
            Result entity = mapper.toEntity(dto);
            repository.save(entity);
            LOGGER.info("Result created successfully: {}", entity);
        } catch (Exception e) {
            LOGGER.error("Error while creating result: {}", dto, e);
            throw new ServiceException("Unable to create result", e);
        }
    }

    /**
     * Update an existing result.
     *
     * @param id  The ID of the result to update
     * @param dto The updated result data
     */
    @Transactional
    public void update(UUID id, ResultDTO dto) {
        LOGGER.info("Updating result: ID = {}, Data = {}", id, dto);
        try {
            Result entity = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Result not found with ID: " + id));
            entity.setGrade(dto.getGrade());
            entity.setRemarks(dto.getRemarks());
//            entity.setExam(examMapper.toEntity(dto.getExam()));
//            entity.setStudent(studentMapper.toEntity(dto.getStudent()));
            entity.setMarksObtained(dto.getMarksObtained());
            entity.setResultDate(dto.getResultDate());
            repository.save(entity);
            LOGGER.info("Result updated successfully: {}", entity);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Failed to update result - not found: ID = {}", id, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while updating result: ID = {}, Data = {}", id, dto, e);
            throw new ServiceException("Unable to update result", e);
        }
    }

    /**
     * Delete a result.
     *
     * @param id The ID of the result to delete
     */
    @Transactional
    public void delete(UUID id) {
        LOGGER.info("Deleting result with ID: {}", id);
        try {
            repository.delete(id);
            LOGGER.info("Result deleted successfully: ID = {}", id);
        } catch (Exception e) {
            LOGGER.error("Error while deleting result: ID = {}", id, e);
            throw new ServiceException("Unable to delete result", e);
        }
    }
}
