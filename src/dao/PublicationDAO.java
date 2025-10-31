package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Publication;

import java.util.List;

public class PublicationDAO extends DAO {

    public PublicationDAO() throws DAOException {
        // Конструктор для совместимости
    }

    public void addPublication(Publication pub) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            executeInTransaction(em, entityManager -> {
                entityManager.persist(pub);
                logger.info("Publication '{}' registered successfully", pub.getTitle());
                System.out.println("Publication registered successfully.");
                return null;
            });

        } catch (DAOException e) {
            logger.error("Error while adding publication '{}'", pub.getTitle(), e);
            throw e;
        } finally {
            closeEntityManager(em);
        }
    }

    public List<Publication> getAllPublications() throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            TypedQuery<Publication> query = em.createNamedQuery("Publication.findAll", Publication.class);
            List<Publication> publications = query.getResultList();

            logger.info("Got {} publications from DB", publications.size());
            return publications;

        } catch (Exception e) {
            logger.error("Error while getting publication list", e);
            throw new DAOException("Couldn't get publication list", e);
        } finally {
            closeEntityManager(em);
        }
    }

    public List<Publication> findByPublisher(String publisher) throws DAOException {
        EntityManager em = null;

        try {
            em = getEntityManager();

            TypedQuery<Publication> query = em.createNamedQuery("Publication.findByPublisher", Publication.class);
            query.setParameter("publisher", publisher);

            List<Publication> publications = query.getResultList();

            logger.info("Found {} publications by publisher '{}'", publications.size(), publisher);
            return publications;

        } catch (Exception e) {
            logger.error("Error while finding publications by publisher", e);
            throw new DAOException("Couldn't find publications", e);
        } finally {
            closeEntityManager(em);
        }
    }
}