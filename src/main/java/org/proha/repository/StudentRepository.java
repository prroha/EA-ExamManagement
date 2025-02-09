package org.proha.repository;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.proha.exceptions.ValidationException;
import org.proha.model.entity.Student;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.logging.Logger;

@Stateless // Makes this class a CDI bean that can be injected
public class StudentRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext(unitName = "studentPU")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        if(entityManager == null)
            throw new IllegalArgumentException("Entity Manager is null");
        return entityManager;
    }

    public int count() {
        String query = "SELECT COUNT(s) FROM Student s";
        return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
    }

    @Transactional
    public List<Student> findAll(int page, int pageSize) {
        TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s", Student.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Optional<Student> findById(UUID id) {
        Student student = entityManager.find(Student.class, id);
        return Optional.ofNullable(student);
    }

    public Optional<Student> findByEmail(String email) {
        String query = "SELECT s FROM Student s WHERE s.email = :email";
        return entityManager.createQuery(query, Student.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

//    @Transactional
//    public Student save(Student student) {
//        try {
//            Map<String, String> errors = validateStudent(student);
//            if (!errors.isEmpty()) {
//                throw new ValidationException(errors);
//            }
//
//            List<Student> existingStudents = entityManager
//                    .createQuery("SELECT s FROM Student s WHERE s.name = :name AND s.id != :id", Student.class)
//                    .setParameter("name", student.getName())
//                    .setParameter("id", student.getId() != null ? student.getId() : UUID.randomUUID())
//                    .getResultList();
//
//            if (!existingStudents.isEmpty()) {
//                errors.put("name", "Student with this name already exists");
//                throw new ValidationException(errors);
//            }
//
//            if (student.getId() == null) {
//                entityManager.persist(student);
//            } else {
//                student = entityManager.merge(student);
//            }
//
//            return student;
//        } catch (PersistenceException e) {
//            logger.severe("Database error while saving student "+ e.getMessage());
//            Map<String, String> errors = new HashMap<>();
//            errors.put("database", "Unable to save student due to database error");
//            throw new ValidationException(errors);
//        }
//    }
@Transactional
public void save(Student student) {
    entityManager.persist(student);
}

    @Transactional
    public void update(Student student) {
            entityManager.merge(student);
    }

    @Transactional
    public void delete(UUID id) {
        Student student = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));
        entityManager.remove(student);
    }

    public List<Student> findStudentsByCourseId(UUID courseId, int page, int pageSize) {
        String query = "SELECT s FROM Student s " +
                "JOIN s.enrollments e " +
                "WHERE e.course.id = :courseId";
        return entityManager.createQuery(query, Student.class)
                .setParameter("courseId", courseId)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    private Map<String, String> validateStudent(Student student) {
        Map<String, String> errors = new HashMap<>();

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            errors.put("name", "Name is required");
        } else if (student.getName().length() > 100) {
            errors.put("name", "Name cannot be longer than 100 characters");
        }

        return errors;
    }

}
