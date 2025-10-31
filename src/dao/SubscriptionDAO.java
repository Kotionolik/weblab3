package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Subscription;

import java.util.List;

public class SubscriptionDAO extends DAO {

    public SubscriptionDAO() throws DAOException {
        // Конструктор для совместимости
    }

    public void addSubscription(Subscription s) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            executeInTransaction(em, entityManager -> {
                entityManager.persist(s);
                logger.info("Subscription for user {} for publication {} added successfully",
                        s.getUserId(), s.getPublicationId());
                System.out.println("Subscription added successfully.");
                return null;
            });

        } catch (DAOException e) {
            logger.error("Error while adding subscription for user {}", s.getUserId(), e);
            throw e;
        } finally {
            closeEntityManager(em);
        }
    }

    public List<Subscription> getAllSubscriptions() throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();
            logger.debug("Getting all subscriptions");

            TypedQuery<Subscription> query = em.createNamedQuery("Subscription.findAll", Subscription.class);
            List<Subscription> subscriptions = query.getResultList();

            logger.info("Got {} subscriptions from DB", subscriptions.size());
            return subscriptions;

        } catch (Exception e) {
            logger.error("Error while getting subscriptions", e);
            throw new DAOException("Couldn't get subscriptions", e);
        } finally {
            closeEntityManager(em);
        }
    }

    public List<Subscription> getSubscriptionsByUser(int userId) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();
            logger.debug("Getting subscriptions for user {}", userId);

            TypedQuery<Subscription> query = em.createNamedQuery("Subscription.findByUser", Subscription.class);
            query.setParameter("userId", userId);

            List<Subscription> subscriptions = query.getResultList();

            logger.info("Got {} subscriptions for user {}", subscriptions.size(), userId);
            return subscriptions;

        } catch (Exception e) {
            logger.error("Error while getting subscriptions for user {}", userId, e);
            throw new DAOException("Couldn't get subscriptions", e);
        } finally {
            closeEntityManager(em);
        }
    }

    public List<Subscription> getUnpaidSubscriptions() throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();
            logger.debug("Getting unpaid subscriptions");

            TypedQuery<Subscription> query = em.createNamedQuery("Subscription.findUnpaid", Subscription.class);
            List<Subscription> subscriptions = query.getResultList();

            logger.info("Got {} unpaid subscriptions from DB", subscriptions.size());
            return subscriptions;

        } catch (Exception e) {
            logger.error("Error while getting unpaid subscriptions", e);
            throw new DAOException("Couldn't get unpaid subscriptions", e);
        } finally {
            closeEntityManager(em);
        }
    }
}