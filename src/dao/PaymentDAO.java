package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Payment;

import java.util.List;

public class PaymentDAO extends DAO {

    public PaymentDAO() throws DAOException {
        // Конструктор для совместимости
    }

    public void addPayment(Payment p) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            executeInTransaction(em, entityManager -> {
                entityManager.persist(p);
                logger.info("Payment with the sum of {} for subscription {} committed successfully",
                        p.getAmount(), p.getSubscriptionId());
                System.out.println("Payment committed successfully.");
                return null;
            });

        } catch (DAOException e) {
            logger.error("Error while committing payment for subscription {}", p.getSubscriptionId(), e);
            throw e;
        } finally {
            closeEntityManager(em);
        }
    }

    public List<Payment> getAllPayments() throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            TypedQuery<Payment> query = em.createNamedQuery("Payment.findAll", Payment.class);
            List<Payment> payments = query.getResultList();

            logger.info("Got {} payments from DB", payments.size());
            return payments;

        } catch (Exception e) {
            logger.error("Error while getting payment list from DB", e);
            throw new DAOException("Couldn't get payment list", e);
        } finally {
            closeEntityManager(em);
        }
    }

    public List<Payment> getPaymentsBySubscription(int subscriptionId) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            TypedQuery<Payment> query = em.createNamedQuery("Payment.findBySubscription", Payment.class);
            query.setParameter("subscriptionId", subscriptionId);

            List<Payment> payments = query.getResultList();

            logger.info("Got {} payments for subscription {}", payments.size(), subscriptionId);
            return payments;

        } catch (Exception e) {
            logger.error("Error while getting payments for subscription {}", subscriptionId, e);
            throw new DAOException("Couldn't get payments for subscription", e);
        } finally {
            closeEntityManager(em);
        }
    }
}