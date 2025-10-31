package dao;

import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO extends DAO {
    private static final String INSERT_SUBSCRIPTION =
            "INSERT INTO Subscriptions (user_id, publication_id, start_date, months) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL =
            "SELECT * FROM Subscriptions";
    private static final String SELECT_BY_USER =
            "SELECT s.subscription_id, s.user_id, s.publication_id, s.start_date, s.months " +
                    "FROM Subscriptions s " +
                    "JOIN Users u ON s.user_id = u.user_id " +
                    "WHERE u.user_id = ?";
    private static final String SELECT_UNPAID =
            "SELECT s.subscription_id, s.user_id, s.publication_id, s.start_date, s.months, " +
                    "(p.price_per_month * s.months) - COALESCE(payment_summary.total_paid, 0) as unpaid_amount " +
                    "FROM Subscriptions s " +
                    "INNER JOIN Publications p ON s.publication_id = p.publication_id " +
                    "LEFT JOIN (SELECT subscription_id, SUM(amount) as total_paid " +
                    "FROM Payments GROUP BY subscription_id) " +
                    "payment_summary ON s.subscription_id = payment_summary.subscription_id " +
                    "WHERE COALESCE(payment_summary.total_paid, 0) < (p.price_per_month * s.months)";

    public SubscriptionDAO() throws DAOException {
        super();
    }

    public void addSubscription(Subscription s) throws DAOException {
        Connection connection = null;
        PreparedStatement ps = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(INSERT_SUBSCRIPTION);
            
            ps.setInt(1, s.getUserId());
            ps.setInt(2, s.getPublicationId());
            ps.setDate(3, s.getStartDate());
            ps.setInt(4, s.getMonths());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Subscription for user {} for publication {} added successfuly", 
                           s.getUserId(), s.getPublicationId());
                System.out.println("Subscription added successfuly.");
            }
            
        } catch (SQLException e) {
            logger.error("Error while adding subscription for user {}", s.getUserId(), e);
            throw new DAOException("Couldn't add subscription", e);
        } finally {
            closeResources(ps, null);
            releaseConnection(connection);
        }
    }

    public List<Subscription> getAllSubscriptions() throws DAOException {
        logger.debug("Getting all subscriptions");
        return getSubscriptionsByQuery(SELECT_ALL, null);
    }

    public List<Subscription> getSubscriptionsByUser(int userId) throws DAOException {
        logger.debug("Getting subscriptions for user {}", userId);
        return getSubscriptionsByQuery(SELECT_BY_USER, userId);
    }

    public List<Subscription> getUnpaidSubscriptions() throws DAOException {
        logger.debug("Getting unpaid subscriptions");
        return getSubscriptionsByQuery(SELECT_UNPAID, null);
    }

    private List<Subscription> getSubscriptionsByQuery(String query, Integer userId) throws DAOException {
        List<Subscription> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(query);
            
            if (userId != null) {
                ps.setInt(1, userId);
            }
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new Subscription(
                        rs.getInt("subscription_id"),
                        rs.getInt("user_id"),
                        rs.getInt("publication_id"),
                        rs.getDate("start_date"),
                        rs.getInt("months")
                ));
            }
            
            logger.info("Got {} subscriptions from DB", list.size());
            
        } catch (SQLException e) {
            logger.error("Error while getting subscriptions", e);
            throw new DAOException("Couldn't get subscriptions", e);
        } finally {
            closeResources(ps, rs);
            releaseConnection(connection);
        }
        
        return list;
    }
}