package org.proha.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.proha.model.entity.Exam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class ExamRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public int count() {
        String query = "SELECT COUNT(c) FROM Exam c";
        return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
    }

    public List<Exam> findAll(int page, int pageSize) {
        TypedQuery<Exam> query = entityManager.createQuery("SELECT c FROM Exam c", Exam.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Optional<Exam> findById(UUID id) {
        Exam exam = entityManager.find(Exam.class, id);
        return Optional.ofNullable(exam);
    }

    @Transactional
    public void save(Exam exam) {
        entityManager.persist(exam);
    }

    @Transactional
    public void update(Exam exam) {
        entityManager.merge(exam);
    }

    @Transactional
    public void delete(UUID courseId) {
        Exam exam = findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));
        entityManager.remove(exam);
    }

//    @Transactional
//    public void update(UUID id, Course exam) {
//        Course existingCourse = entityManager.find(Course.class, id);
//
//        if (existingCourse != null) {
//            entityManager.merge(exam);
//        } else {
//            throw new IllegalArgumentException("Course with ID " + exam.getId() + " does not exist");
//        }
//    }
}
