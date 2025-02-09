package org.proha.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.proha.model.entity.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class ResultRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public int count() {
        String query = "SELECT COUNT(e) FROM Result e";
        return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
    }

    public List<Result> findAll(int page, int pageSize) {
        String query = "SELECT e FROM Result e JOIN FETCH e.student JOIN FETCH e.exam";
        return entityManager.createQuery(query, Result.class)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }


    public List<Result> findByStudentId(UUID studentId) {
        String query = "SELECT e FROM Result e JOIN FETCH e.exam c WHERE e.student.id = :studentId";
        return entityManager.createQuery(query, Result.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    public Optional<Result> findById(UUID enrollmentId) {
        Result enrollment = entityManager.find(Result.class, enrollmentId);
        return Optional.ofNullable(enrollment);
    }

    @Transactional
    public void save(Result enrollment) {
        entityManager.persist(enrollment);
    }

    @Transactional
    public void update(Result enrollment) {
        entityManager.merge(enrollment);
    }

    @Transactional
    public void delete(UUID enrollmentId) {
        Result enrollment = findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found with ID: " + enrollmentId));
        entityManager.remove(enrollment);
    }

    public List<Result> findByCourseId(UUID courseId) {
        TypedQuery<Result> query = entityManager.createQuery(
                "SELECT e FROM Result e WHERE e.exam.id = :courseId", Result.class);

        query.setParameter("courseId", courseId);

        return query.getResultList();
    }
    public void deleteByStudentAndCourse(UUID studentId, UUID courseId) {
        entityManager.createQuery("DELETE FROM Result e WHERE e.student.id = :studentId AND e.exam.id = :courseId")
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }
}
