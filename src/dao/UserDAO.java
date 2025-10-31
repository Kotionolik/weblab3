package dao;

import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO {

    private static final String INSERT_USER = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Users";

    public UserDAO() throws DAOException {
        super();
    }

    public void addUser(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement ps = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(INSERT_USER);
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("User '{}' registered successfully", user.getUsername());
                System.out.println("User registered successfully.");
            }
            
        } catch (SQLException e) {
            logger.error("Error while adding user '{}'", user.getUsername(), e);
            throw new DAOException("Couldn't add user", e);
        } finally {
            closeResources(ps, null);
            releaseConnection(connection);
        }
    }

    public List<User> getUsers() throws DAOException {
        List<User> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(SELECT_ALL);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
            
            logger.info("Got {} users from DB", list.size());
            
        } catch (SQLException e) {
            logger.error("Error while getting user list", e);
            throw new DAOException("Couldn't get user list", e);
        } finally {
            closeResources(ps, rs);
            releaseConnection(connection);
        }
        
        return list;
    }
}