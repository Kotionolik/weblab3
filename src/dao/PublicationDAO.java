package dao;

import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationDAO extends DAO {
    private static final String INSERT_PUBLICATION =
            "INSERT INTO Publications (title, publisher, description, price_per_month) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PUBLICATIONS =
            "SELECT * FROM Publications";
    
    public PublicationDAO() throws DAOException {
        super();
    }

    public void addPublication(Publication pub) throws DAOException {
        Connection connection = null;
        PreparedStatement ps = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(INSERT_PUBLICATION);
            
            ps.setString(1, pub.getTitle());
            ps.setString(2, pub.getPublisher());
            ps.setString(3, pub.getDescription());
            ps.setDouble(4, pub.getPrice());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Publication '{}' registered successfully", pub.getTitle());
                System.out.println("Publication registered successfully.");
            }
            
        } catch (SQLException e) {
            logger.error("Error while adding publication '{}'", pub.getTitle(), e);
            throw new DAOException("Couldn't add publication", e);
        } finally {
            closeResources(ps, null);
            releaseConnection(connection);
        }
    }

    public List<Publication> getAllPublications() throws DAOException {
        List<Publication> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            connection = getConnection();
            ps = connection.prepareStatement(SELECT_ALL_PUBLICATIONS);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new Publication(
                        rs.getInt("publication_id"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getString("description"),
                        rs.getDouble("price_per_month")
                ));
            }
            
            logger.info("Got {} publications from DB", list.size());
            
        } catch (SQLException e) {
            logger.error("Error while getting publication list", e);
            throw new DAOException("Couldn't get publication list", e);
        } finally {
            closeResources(ps, rs);
            releaseConnection(connection);
        }
        
        return list;
    }
}