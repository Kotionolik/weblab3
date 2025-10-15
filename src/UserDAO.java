import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class UserDAO {

    private static final String INSERT_USER = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Users";

    public void addUser(User user) throws DAOException {
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(INSERT_USER))
        {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            System.out.println("Пользователь успешно зарегестрирован.");
        } catch (JdbcConnectionException e)
        {
            throw new DAOException("Can't connect to the database", e);
        } catch (SQLException e)
        {
            throw new DAOException("Error while preparing statement", e);
        }
    }

    public List<User> getUsers() throws DAOException {
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = JdbcConnector.getConnection().prepareStatement(SELECT_ALL);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
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
