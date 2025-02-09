package org.proha.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.proha.model.dto.ExamDTO;
import org.proha.model.entity.Exam;
import org.proha.repository.ExamRepository;
import org.proha.utils.ExamMapper;
import org.proha.utils.StudentMapper;
import org.proha.utils.SubjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
public class ExamService {

    @Inject
    private ExamRepository repository;

    @Inject
    private ExamMapper mapper;
    @Inject
    private StudentMapper studentMapper;
    @Inject
    private SubjectMapper subjectMapper;


    private Logger LOGGER = LoggerFactory.getLogger(ExamService.class);


    public int count() {
        return repository.count();
    }


    /**
     * Get a paginated list of exams.
     *
     * @param page     The page number (1-based index)
     * @param pageSize The size of the page
     * @return List of ExamDTOs
     */
    public List<ExamDTO> findAll(int page, int pageSize) {
        LOGGER.info("Fetching exams - page: {}, pageSize: {}", page, pageSize);
        List<Exam> entities = repository.findAll(page, pageSize);

        LOGGER.info("Exams: "+ entities.get(0).toString());
        return entities.stream()
                .map(exam -> {
                    ExamDTO dto = mapper.toDTO(exam);
                    dto.setSubjectId(subjectMapper.toDTO(exam.getSubject()).getId());

                    return dto;
                })
                .collect(Collectors.toList());
    }


    /**
     * Find a course by ID.
     *
     * @param id The ID of the exam
     * @return Optional containing the EnrollmentDTO if found
     */
    public Optional<ExamDTO> findById(UUID id) {
        LOGGER.info("Fetching exam by ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Create a new exam.
     *
     * @param dto The exam data
     */
    @Transactional
    public void create(ExamDTO dto) {
        LOGGER.info("Creating exam: {}", dto);
        try {
            Exam entity = mapper.toEntity(dto);
            repository.save(entity);
            LOGGER.info("Exam created successfully: {}", entity);
        } catch (Exception e) {
            LOGGER.error("Error while creating exam: {}", dto, e);
            throw new ServiceException("Unable to create exam", e);
        }
    }

    /**
     * Update an existing exam.
     *
     * @param id  The ID of the exam to update
     * @param dto The updated exam data
     */
    @Transactional
    public void update(UUID id, ExamDTO dto) {
        LOGGER.info("Updating exam: ID = {}, Data = {}", id, dto);
        try {
            Exam entity = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Exam not found with ID: " + id));
            entity.setExamTitle(dto.getExamTitle());
            entity.setExamRemarks(dto.getExamRemarks());
            entity.setExamDate(dto.getExamDate());
            entity.setMaxMarks(dto.getMaxMarks());
            repository.update(entity);
            LOGGER.info("Exam updated successfully: {}", entity);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Failed to update exam - not found: ID = {}", id, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while updating exam: ID = {}, Data = {}", id, dto, e);
            throw new ServiceException("Unable to update exam", e);
        }
    }

    /**
     * Delete a exam.
     *
     * @param id The ID of the exam to delete
     */
    @Transactional
    public void delete(UUID id) {
        LOGGER.info("Deleting exam with ID: {}", id);
        try {
            repository.delete(id);
            LOGGER.info("Exam deleted successfully: ID = {}", id);
        } catch (Exception e) {
            LOGGER.error("Error while deleting exam: ID = {}", id, e);
            throw new ServiceException("Unable to delete exam", e);
        }
    }
}
