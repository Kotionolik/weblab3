package dao;

import db.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DAO {
    protected static final Logger logger = LogManager.getLogger(DAO.class);
    protected ConnectionPool pool;
    
    public DAO() throws DAOException {
        try {
            pool = ConnectionPool.getInstance();
            logger.debug("DAO initialized with ConnectionPool");
        } catch (ConnectionPoolException e) {
            logger.error("Couldn't get ConnectionPool instance", e);
            throw new DAOException("Couldn't initialise DAO", e);
        }
    }
    
    protected Connection getConnection() throws DAOException {
        try {
            Connection connection = pool.getConnection();
            logger.trace("Got connection from pool");
            return connection;
        } catch (ConnectionPoolException e) {
            logger.error("Couldn't get connection from pool", e);
            throw new DAOException("Couldn't get connection from pool", e);
        }
    }
    
    protected void releaseConnection(Connection connection) throws DAOException {
        if (connection != null) {
            try {
                pool.releaseConnection(connection);
                logger.trace("Returned connection to pool");
            } catch (ConnectionPoolException e) {
                logger.error("Couldn't return connection to pool", e);
                throw new DAOException("Couldn't return connection to pool", e);
            }
        }
    }
    
    protected void closeResources(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.trace("ResultSet closed");
            } catch (SQLException e) {
                logger.warn("Error while closing ResultSet", e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
                logger.trace("PreparedStatement closed");
            } catch (SQLException e) {
                logger.warn("Error while closing PreparedStatement", e);
            }
        }
    }
}