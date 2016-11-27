package at.ac.tuwien.sepm.util.dbhandler.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This Singleton-class returns a connection to an H2-database called "fotostudio".
 * The class will always return the same connection-object as long as {@link #closeConnection()} is not called. If it is called a new connection will be opened when calling {@link #getConnection()}.
 */
public class H2Handler  implements DBHandler {
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
    Precondition: The H2-DB-application is running with a database called "fotostudio" and a user "sa" with an empty password.
     */
    /**
     * Opens a new connection to an H2-database of the name "fotostudio" with username "sa" and empty password
     * @throws Exception if either the driver-class is not found or the DriverManager can't establish a connection to the specified database with the given login credentials.
     */
    private void openConnection() throws PersistenceException{
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/fotostudio", "sa","");
        } catch(ClassNotFoundException|SQLException e){
            LOGGER.error("openConnection - "+e.getMessage());
            throw new PersistenceException(e);
        }
    }

    @Override
    public Connection getConnection() throws PersistenceException {
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
            LOGGER.info("closeConnection - "+e);
        } finally {
            connection=null;
        }
    }
}
