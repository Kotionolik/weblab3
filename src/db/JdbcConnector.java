package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JdbcConnector {
    private static Connection connection;

    public static Connection getConnection() throws JdbcConnectionException {
        ResourceBundle resource = ResourceBundle.getBundle("database");
        try {
            String url = resource.getString("url");
            String username = resource.getString("username");
            String password = resource.getString("password");
            String driver = resource.getString("driver");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            throw new JdbcConnectionException("Can't load database driver.", e);
        } catch (SQLException e) {
            throw new JdbcConnectionException("Failed to connect to database.", e);
        }
        if (connection == null) {
            throw new JdbcConnectionException("Driver type is not correct in URL " + resource.getString("url") + ".");
        }
        return connection;
    }

    public void close() throws JdbcConnectionException {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new JdbcConnectionException("Can't close connection", e);
            }
        }
    }

}