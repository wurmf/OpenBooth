package at.ac.tuwien.sepm.util.dbhandler;

import at.ac.tuwien.sepm.util.exceptions.DatabaseException;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;

import java.sql.Connection;

/**
 * Interface for database handlers which must be able to open, return and close a connection to a database.
 */
public interface DBHandler {
    /**
     * Returns a connection to the database on which the concrete implementation is working on.
     * If {@link DBHandler#getTestConnection()} was previously called, a connection to the test-database is returned.
     * @return A Connection-object linking to the target-database.
     * @throws DatabaseException if the implementation is unable to return a working connection to the database.
     */
    Connection getConnection() throws DatabaseException;

    /**
     * Closes an open connection to the database.
     */
    void closeConnection();

    /**
     * Establishes and returns a connection to a database only used for tests.
     * @return a connection to the test database
     * @throws DatabaseException if the implementation is unable to return a working connection to the database.
     */
    Connection getTestConnection() throws DatabaseException;
}