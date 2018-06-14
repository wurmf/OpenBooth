package org.openbooth.util.dbhandler.impl;

import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.util.dbhandler.DBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This Singleton-class returns a connection to an H2-database called "openbooth".
 * The class will always return the same connection-object as long as {@link #closeConnection()} is not called. If it is called a new connection will be opened when calling {@link #getConnection()}.
 */
public class H2Handler implements DBHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(H2Handler.class);

    private static H2Handler ourInstance = new H2Handler();
    private Connection connection;


    private H2Handler() {
        this.connection=null;
    }
    public static H2Handler getInstance() {
        return ourInstance;
    }
    /*
    Precondition: The H2-DB-application is running with a database called "openbooth" and a user "sa" with an empty password.
     */
    /**
     * Opens a new connection to an H2-database of the name "openbooth" with username "sa" and empty password
     * @throws DatabaseException if either the driver-class is not found or the DriverManager can't establish a connection to the specified database with the given login credentials.
     */
    private void openConnection() throws DatabaseException{
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/openbooth", "sa","");
        } catch(ClassNotFoundException|SQLException e){
            LOGGER.error("openConnection - ",e);
            throw new DatabaseException(e);
        }
    }

    @Override
    public Connection getConnection() throws DatabaseException {
        if(connection==null){
            openConnection();
        }
        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.info("closeConnection - unable to close connection - ",e);
        } finally {
            connection=null;
        }
    }

    @Override
    public Connection getTestConnection() throws DatabaseException {
        return null;
    }
}
