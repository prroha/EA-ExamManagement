package org.proha.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.proha.model.entity.Subject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Stateless
public class SubjectRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public int count() {
        String query = "SELECT COUNT(c) FROM Subject c";
        return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
    }

    public List<Subject> findAll(int page, int pageSize) {
        TypedQuery<Subject> query = entityManager.createQuery("SELECT c FROM Subject c", Subject.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Optional<Subject> findById(UUID id) {
        Subject subject = entityManager.find(Subject.class, id);
        return Optional.ofNullable(subject);
    }

    @Transactional
    public void save(Subject subject) {
        entityManager.persist(subject);
    }

    @Transactional
    public void update(Subject subject) {
        entityManager.merge(subject);
    }

    @Transactional
    public void delete(UUID courseId) {
        Subject subject = findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));
        entityManager.remove(subject);
    }

    public Optional<Subject> findByName(String name) {
        String query = "SELECT c FROM Subject c WHERE c.name = :name";
        return entityManager.createQuery(query, Subject.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

//    @Transactional
//    public void update(UUID id, Course subject) {
//        Course existingCourse = entityManager.find(Course.class, id);
//
//        if (existingCourse != null) {
//            entityManager.merge(subject);
//        } else {
//            throw new IllegalArgumentException("Course with ID " + subject.getId() + " does not exist");
//        }
//    }
}
