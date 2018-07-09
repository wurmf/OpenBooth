package org.openbooth.util.dbhandler;

import org.openbooth.util.exceptions.DatabaseException;

import java.sql.Connection;

/**
 * Interface for database handlers which must be able to open, return and close a connection to a database.
 */
public interface DBHandler {
    /**
     * Returns a connection to the database
     * @return A Connection-object linking to the target-database.
     * @throws DatabaseException if the implementation is unable to return a working connection to the database.
     */
    Connection getConnection() throws DatabaseException;

    /**
     * Closes an open connection to the database.
     */
    void closeConnection();
}