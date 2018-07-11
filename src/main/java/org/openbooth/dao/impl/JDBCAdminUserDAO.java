package org.openbooth.dao.impl;

import org.openbooth.util.QueryBuilder;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.AdminUserDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.AdminUser;
import org.openbooth.util.dbhandler.DBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of the AdminUserDAO-Interface using H2-syntax to manipulate the the adminuser table in an H2-database.
 */
@Repository
public class JDBCAdminUserDAO implements AdminUserDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCAdminUserDAO.class);

    public static final String TABLE_NAME = "adminusers";
    public static final String NAME_COLUMN = "adminname";
    public static final String PW_HASH_COLUMN = "password";

    private Connection con;

    /**
     * Constructor with a DBHandler specified, which will be used to retrieve a database connection.
     * @param handler DBHandler from which the connection will be taken.
     * @throws PersistenceException if an error occurs while retrieving a connection from the DBHandler.
     */
    @Autowired
    public JDBCAdminUserDAO(DBHandler handler) throws PersistenceException{
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ADMIN_QUERY = QueryBuilder.buildSelectAllColumns(TABLE_NAME, NAME_COLUMN);

    @Override
    public AdminUser read(String adminName) throws PersistenceException{
        if(adminName == null) throw new IllegalArgumentException("adminName is null");
        try (PreparedStatement stmt = con.prepareStatement(READ_ADMIN_QUERY)){
            stmt.setString(1,adminName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String newAdminName = rs.getString(NAME_COLUMN);
                    byte[] password = rs.getBytes(PW_HASH_COLUMN);
                    LOGGER.trace("Read user {} from database", adminName);
                    return new AdminUser(newAdminName, password);
                } else {
                    LOGGER.debug("No user with name {} found", adminName);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
