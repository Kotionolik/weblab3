package db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private static final AtomicBoolean instanceCreated = new AtomicBoolean(false);
    
    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> usedConnections;
    
    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    
    private String url;
    private String username;
    private String password;
    private String driver;
    
    private ConnectionPool() throws ConnectionPoolException {
        try {
            ResourceBundle resource = ResourceBundle.getBundle("database");
            this.url = resource.getString("url");
            this.username = resource.getString("username");
            this.password = resource.getString("password");
            this.driver = resource.getString("driver");
            
            Class.forName(driver);
            logger.info("DB Driver loaded: {}", driver);
            
            availableConnections = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
            usedConnections = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
            
            initializePool();
            logger.info("Initialised pool with {} connections", INITIAL_POOL_SIZE);
            
        } catch (ClassNotFoundException e) {
            logger.error("Couldn't load DB driver", e);
            throw new ConnectionPoolException("Couldn't load DB driver", e);
        }
    }
    
    public static ConnectionPool getInstance() throws ConnectionPoolException {
        if (!instanceCreated.get()) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                    logger.info("Created ConnectionPool");
                }
            }
        }
        return instance;
    }
    
    private void initializePool() throws ConnectionPoolException {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                availableConnections.add(createConnection());
                logger.debug("Created connection #{}", i + 1);
            } catch (SQLException e) {
                logger.error("Error while creating connection #{}", i + 1, e);
                throw new ConnectionPoolException("Couldn't create connection pool", e);
            }
        }
    }
    
    private Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        logger.debug("Created new connection to DB");
        return connection;
    }

    public synchronized Connection getConnection() throws ConnectionPoolException {
        try {
            Connection connection = availableConnections.poll();
            
            if (connection == null) {
                if (usedConnections.size() < MAX_POOL_SIZE) {
                    logger.warn("No available connections, creating new...");
                    connection = createConnection();
                } else {
                    logger.error("Max pool size reached: {}", MAX_POOL_SIZE);
                    throw new ConnectionPoolException("Max pool size reached: " + MAX_POOL_SIZE);
                }
            }
            
            if (connection != null && connection.isClosed()) {
                logger.warn("Connection closed, creating new...");
                connection = createConnection();
            }
            
            usedConnections.add(connection);
            logger.debug("Connection added. Available connections: {}, Used connections: {}", 
                        availableConnections.size(), usedConnections.size());
            
            return connection;
            
        } catch (SQLException e) {
            logger.error("Error while getting connection from pool", e);
            throw new ConnectionPoolException("Error while getting connection from pool", e);
        }
    }
    
    public synchronized void releaseConnection(Connection connection) throws ConnectionPoolException {
        if (connection == null) {
            logger.warn("Trying to return null connection");
            return;
        }
        
        try {
            if (usedConnections.remove(connection)) {
                if (!connection.isClosed()) {
                    availableConnections.add(connection);
                    logger.debug("Connection returned. Available connections: {}, Used connections: {}", 
                                availableConnections.size(), usedConnections.size());
                } else {
                    logger.warn("Connection closed, creating new...");
                    availableConnections.add(createConnection());
                }
            } else {
                logger.warn("Connection is not from that pool");
            }
        } catch (SQLException e) {
            logger.error("Error while returning connection to pool", e);
            throw new ConnectionPoolException("Error while returning connection to pool", e);
        }
    }
    

    public int getAvailableConnectionsCount() {
        return availableConnections.size();
    }
    

    public int getUsedConnectionsCount() {
        return usedConnections.size();
    }
    
    public synchronized void shutdown() throws ConnectionPoolException {
        logger.info("Shutting down ConnectionPool...");
        
        try {
            for (Connection connection : usedConnections) {
                closeConnection(connection);
            }
            usedConnections.clear();
            
            for (Connection connection : availableConnections) {
                closeConnection(connection);
            }
            availableConnections.clear();
            
            logger.info("ConnectionPool shut down succesfully");
            
        } catch (Exception e) {
            logger.error("Error while shutting down ConnectionPool", e);
            throw new ConnectionPoolException("Error while shutting down ConnectionPool", e);
        }
    }
    

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    logger.debug("Connection closed.");
                }
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }
}