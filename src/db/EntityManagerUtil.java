package db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityManagerUtil {
    private static final Logger logger = LogManager.getLogger(EntityManagerUtil.class);
    private static EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENCE_UNIT_NAME = "Weblab4PU";

    static {
        try {
            logger.info("Initializing EntityManagerFactory...");
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            logger.info("EntityManagerFactory initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to create EntityManagerFactory", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }
        logger.trace("Creating new EntityManager");
        return entityManagerFactory.createEntityManager();
    }

    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            logger.info("Closing EntityManagerFactory...");
            entityManagerFactory.close();
            logger.info("EntityManagerFactory closed successfully");
        }
    }

    public static boolean isOpen() {
        return entityManagerFactory != null && entityManagerFactory.isOpen();
    }
}