package org.proha.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.proha.model.dto.SubjectDTO;
import org.proha.model.entity.Subject;
import org.proha.repository.SubjectRepository;
import org.proha.repository.ResultRepository;
import org.proha.utils.SubjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
public class SubjectService {

    @Inject
    private SubjectRepository repository;

    @Inject
    private ResultRepository enrollmentRepository;

    @Inject
    private SubjectMapper mapper;

    private Logger LOGGER = LoggerFactory.getLogger(SubjectService.class);


    public int count() {
        return repository.count();
    }


    /**
     * Get a paginated list of subjects.
     *
     * @param page     The page number (1-based index)
     * @param pageSize The size of the page
     * @return List of SubjectDtos
     */
    public List<SubjectDTO> findAll(int page, int pageSize) {
        LOGGER.info("Fetching subjects - page: {}, pageSize: {}", page, pageSize);
        List<Subject> subjects = repository.findAll(page, pageSize);
        return subjects.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a subject by ID.
     *
     * @param id The ID of the subject
     * @return Optional containing the SubjectDTO if found
     */
    public Optional<SubjectDTO> findById(UUID id) {
        LOGGER.info("Fetching subject by ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Find a subject by ID.
     *
     * @param name The Name of the subject
     * @return Optional containing the SubjectDTO if found
     */
    public Optional<SubjectDTO> findByName(String name) {
        LOGGER.info("Fetching subject by Name: {}", name);
        return repository.findByName(name)
                .map(mapper::toDTO);
    }
    /**
     * Create a new subject.
     *
     * @param dto The subject data
     */
    @Transactional
    public void create(SubjectDTO dto) {
        LOGGER.info("Creating subject: {}", dto);
        System.out.println("Create::: "+ dto.getName());
        try {
            Subject subject = mapper.toEntity(dto);
            repository.save(subject);
            LOGGER.info("Subject created successfully: {}", subject);
        } catch (Exception e) {
            LOGGER.error("Error while creating subject: {}", dto, e);
            throw new ServiceException("Unable to create subject", e);
        }
    }

    /**
     * Update an existing subject.
     *
     * @param id  The ID of the subject to update
     * @param dto The updated subject data
     */
    @Transactional
    public void update(UUID id, SubjectDTO dto) {
        LOGGER.info("Updating subject: ID = {}, Data = {}", id, dto);
        try {
            Subject entity = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Subject not found with ID: " + id));
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            repository.update(entity);
            LOGGER.info("Subject updated successfully: {}", entity);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Failed to update subject - not found: ID = {}", id, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while updating subject: ID = {}, Data = {}", id, dto, e);
            throw new ServiceException("Unable to update subject", e);
        }
    }

    /**
     * Delete a subject.
     *
     * @param id The ID of the subject to delete
     */
    @Transactional
    public void delete(UUID id) {
        LOGGER.info("Deleting subject with ID: {}", id);
        try {
            repository.delete(id);
            LOGGER.info("Subject deleted successfully: ID = {}", id);
        } catch (Exception e) {
            LOGGER.error("Error while deleting subject: ID = {}", id, e);
            throw new ServiceException("Unable to delete subject", e);
        }
    }
}
