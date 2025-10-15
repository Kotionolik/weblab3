import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private static final String INSERT_PAYMENT =
            "INSERT INTO Payments (subscription_id, amount, payment_date) VALUES (?, ?, ?)";
    private static final String SELECT_ALL =
            "SELECT * FROM Payments";

    public void addPayment(Payment p) throws DAOException {
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(INSERT_PAYMENT)) {
            ps.setInt(1, p.getSubscriptionId());
            ps.setDouble(2, p.getAmount());
            ps.setDate(3, p.getPaymentDate());
            ps.executeUpdate();
            System.out.println("Платеж успешно проведён.");
        } catch (JdbcConnectionException e)
        {
            throw new DAOException("Can't connect to the database", e);
        } catch (SQLException e)
        {
            throw new DAOException("Error while preparing statement", e);
        }
    }

    public List<Payment> getAllPayments() throws DAOException {
        List<Payment> list = new ArrayList<>();
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("subscription_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date")
                ));
            }
        } catch (JdbcConnectionException e)
        {
            throw new DAOException("Can't connect to the database", e);
        } catch (SQLException e)
        {
            throw new DAOException("Error while preparing statement", e);
        }
        return list;
    }
}
