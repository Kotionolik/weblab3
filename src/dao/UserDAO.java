package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO {

    public UserDAO() throws DAOException {
        // Конструктор для совместимости
    }

    public void addUser(User user) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            executeInTransaction(em, entityManager -> {
                entityManager.persist(user);
                logger.info("User '{}' registered successfully", user.getUsername());
                System.out.println("User registered successfully.");
                return null;
            });

        } catch (DAOException e) {
            logger.error("Error while adding user '{}'", user.getUsername(), e);
            throw e;
        } finally {
            closeEntityManager(em);
        }
    }

    public List<User> getUsers() throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
            List<User> users = query.getResultList();

            logger.info("Got {} users from DB", users.size());
            return users;

        } catch (Exception e) {
            logger.error("Error while getting user list", e);
            throw new DAOException("Couldn't get user list", e);
        } finally {
            closeEntityManager(em);
        }
    }

    public User findByUsername(String username) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            TypedQuery<User> query = em.createNamedQuery("User.findByUsername", User.class);
            query.setParameter("username", username);

            List<User> results = query.getResultList();

            if (results.isEmpty()) {
                return null;
            }

            logger.info("Found user with username '{}'", username);
            return results.get(0);

        } catch (Exception e) {
            logger.error("Error while finding user by username", e);
            throw new DAOException("Couldn't find user", e);
        } finally {
            closeEntityManager(em);
        }
    }
}