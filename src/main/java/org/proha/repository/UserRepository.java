package org.proha.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.proha.model.entity.User;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.logging.Logger;

@Stateless
public class UserRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext()
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        if(entityManager == null)
            throw new IllegalArgumentException("Entity Manager is null");
        return entityManager;
    }

    public int count() {
        String query = "SELECT COUNT(s) FROM User s";
        return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
    }

    @Transactional
    public List<User> findAll(int page, int pageSize) {
        TypedQuery<User> query = entityManager.createQuery("SELECT s FROM User s", User.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Optional<User> findById(UUID id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        User user = entityManager.find(User.class, username);
        return Optional.ofNullable(user);
    }
    public Optional<User> findByEmail(String email) {
        String query = "SELECT s FROM User s WHERE s.email = :email";
        return entityManager.createQuery(query, User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Transactional
    public void update(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public void delete(UUID id) {
        User user = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        entityManager.remove(user);
    }
}
