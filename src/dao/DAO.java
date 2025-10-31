package dao;

import db.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DAO {
    protected static final Logger logger = LogManager.getLogger(DAO.class);

    protected EntityManager getEntityManager() {
        logger.trace("Getting EntityManager");
        return EntityManagerUtil.getEntityManager();
    }

    protected void closeEntityManager(EntityManager em) {
        if (em != null && em.isOpen()) {
            logger.trace("Closing EntityManager");
            em.close();
        }
    }

    protected <T> void executeInTransaction(EntityManager em, TransactionCallback<T> callback) throws DAOException {
        try {
            em.getTransaction().begin();
            logger.trace("Transaction started");

            callback.execute(em);

            em.getTransaction().commit();
            logger.trace("Transaction committed");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.warn("Transaction rolled back due to error");
            }
            logger.error("Error during transaction", e);
            throw new DAOException("Transaction failed", e);
        }
    }

    @FunctionalInterface
    protected interface TransactionCallback<T> {
        T execute(EntityManager em) throws Exception;
    }
}