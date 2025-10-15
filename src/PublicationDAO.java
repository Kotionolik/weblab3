import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationDAO {
    private static final String INSERT_PUBLICATION =
            "INSERT INTO Publications (title, publisher, description, price_per_month) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PUBLICATIONS =
            "SELECT * FROM Publications";

    public void addPublication(Publication pub) throws DAOException {
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(INSERT_PUBLICATION)) {
            ps.setString(1, pub.getTitle());
            ps.setString(2, pub.getPublisher());
            ps.setString(3, pub.getDescription());
            ps.setDouble(4, pub.getPrice());
            ps.executeUpdate();
            System.out.println("Издание успешно зарегистрировано.");
        } catch (JdbcConnectionException e)
        {
            throw new DAOException("Can't connect to the database", e);
        } catch (SQLException e)
        {
            throw new DAOException("Error while preparing statement", e);
        }
    }

    public List<Publication> getAllPublications() throws DAOException {
        List<Publication> list = new ArrayList<>();
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(SELECT_ALL_PUBLICATIONS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Publication(
                        rs.getInt("publication_id"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getString("description"),
                        rs.getDouble("price_per_month")
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
