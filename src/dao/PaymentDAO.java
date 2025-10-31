package dao;

import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends DAO {
    private static final String INSERT_PAYMENT =
            "INSERT INTO Payments (subscription_id, amount, payment_date) VALUES (?, ?, ?)";
    private static final String SELECT_ALL =
            "SELECT * FROM Payments";
    
    public PaymentDAO() throws DAOException {
        super();
    }

    public void addPayment(Payment p) throws DAOException {
        Connection connection = null;
        PreparedStatement ps = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(INSERT_PAYMENT);
            
            ps.setInt(1, p.getSubscriptionId());
            ps.setDouble(2, p.getAmount());
            ps.setDate(3, p.getPaymentDate());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Payment with the sum of {} for subcription {} commited successfully", 
                           p.getAmount(), p.getSubscriptionId());
                System.out.println("Payment commited successfully.");
            }
            
        } catch (SQLException e) {
            logger.error("Error while commiting payment for subscription {}", p.getSubscriptionId(), e);
            throw new DAOException("Couldn't do the payment", e);
        } finally {
            closeResources(ps, null);
            releaseConnection(connection);
        }
    }

    public List<Payment> getAllPayments() throws DAOException {
        List<Payment> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(SELECT_ALL);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("subscription_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date")
                ));
            }
            
            logger.info("Got {} payments from DB", list.size());
            
        } catch (SQLException e) {
            logger.error("Error while getting payment list from DB", e);
            throw new DAOException("Couldn't get payment list", e);
        } finally {
            closeResources(ps, rs);
            releaseConnection(connection);
        }
        
        return list;
    }
}