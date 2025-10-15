import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {
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
            "SELECT s.subscription_id, s.user_id, s.publication_id, s.start_date, s.months " +
                    "FROM Subscriptions s " +
                    "LEFT JOIN Payments p ON s.subscription_id = p.subscription_id " +
                    "WHERE p.payment_id IS NULL";

    public void addSubscription(Subscription s) throws DAOException {
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(INSERT_SUBSCRIPTION)) {
            ps.setInt(1, s.getUserId());
            ps.setInt(2, s.getPublicationId());
            ps.setDate(3, s.getStartDate());
            ps.setInt(4, s.getMonths());
            ps.executeUpdate();
            System.out.println("Подписка успешно добавлена.");
        } catch (JdbcConnectionException e)
        {
            throw new DAOException("Can't connect to the database", e);
        } catch (SQLException e)
        {
            throw new DAOException("Error while preparing statement", e);
        }
    }

    public List<Subscription> getAllSubscriptions() throws DAOException {
        return getSubscriptionsByQuery(SELECT_ALL);
    }

    public List<Subscription> getSubscriptionsByUser(int userId) throws DAOException {
        List<Subscription> list = new ArrayList<>();
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(SELECT_BY_USER)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Subscription(
                            rs.getInt("subscription_id"),
                            rs.getInt("user_id"),
                            rs.getInt("publication_id"),
                            rs.getDate("start_date"),
                            rs.getInt("months")
                    ));
                }
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

    public List<Subscription> getUnpaidSubscriptions() throws DAOException {
        return getSubscriptionsByQuery(SELECT_UNPAID);
    }

    private List<Subscription> getSubscriptionsByQuery(String query) throws DAOException {
        List<Subscription> list = new ArrayList<>();
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Subscription(
                        rs.getInt("subscription_id"),
                        rs.getInt("user_id"),
                        rs.getInt("publication_id"),
                        rs.getDate("start_date"),
                        rs.getInt("months")
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
