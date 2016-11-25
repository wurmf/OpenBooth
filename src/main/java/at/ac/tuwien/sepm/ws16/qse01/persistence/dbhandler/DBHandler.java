package at.ac.tuwien.sepm.ws16.qse01.persistence.dbhandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for database handlers which must be able to open, return and close a connection to a database.
 */
public interface DBHandler {
    /**
     * Returns a connection to the database on which the concrete implementation is working on.
     * @return A Connection-object linking to the target-database.
     * @throws SQLException if the implementation is unable to return a working connection to the database.
     */
    Connection getConnection() throws Exception;

    /**
     * Closes an open connection to the database.
     * @throws SQLException if the implementation is unable to close an established connection.
     */
    void closeConnection();
}