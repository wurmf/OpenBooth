package org.openbooth.util.dbhandler.impl;

import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.dbhandler.prep.DataPrepper;
import org.openbooth.util.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.h2.tools.Server;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * This Singleton-class starts an H2-Server instance and returns a connection to an H2-database called "openbooth".
 * The class will always return the same connection-object as long as {@link #closeConnection()} is not called. If it is called a new connection will be opened when calling {@link #getConnection()}.
 */
@Component
public class H2EmbeddedHandler  implements DBHandler {

    private static final boolean INSERT_DEMO_DATA = true;

    private static final Logger LOGGER = LoggerFactory.getLogger(H2EmbeddedHandler.class);

    protected Connection connection = null;
    private Server h2Server;

    /**
     * Opens a new connection to an H2-database of the name "openbooth" with username "sa" and empty password.
     * If it recognizes that the database has not been initialized yet, methods createTables, initDatabase and insertDemoData of DataPrepper are called.
     * @throws SQLException if either the driver-class is not found or the DriverManager can't establish a connection to the specified database with the given login credentials.
     * @throws ClassNotFoundException if the driver class is not found.
     * @throws DatabaseException if an error occurs on a first startup while injecting data used for demos.
     */
    protected void openConnection(String dbName) throws SQLException, ClassNotFoundException, DatabaseException {

        LOGGER.info("openConnection - Trying to open connection to database {}", dbName);

        Class.forName("org.h2.Driver");
        this.h2Server= Server.createTcpServer().start();
        this.connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+dbName, "sa","");

        ResultSet rs = connection.getMetaData().getTables(null,null,"ADMINUSERS",null);
        if(!rs.next()){
            LOGGER.info("openConnection - first startup detected");
            onFirstStartup(connection);
        }
        LOGGER.info("openConnection - connected to database '{}'",dbName);

    }

    protected void onFirstStartup(Connection connection) throws DatabaseException{
        DataPrepper.createTables(connection);
        DataPrepper.initDatabase(connection);
        if(INSERT_DEMO_DATA) {
            DataPrepper.insertTestData(connection);
            DataPrepper.setUpDefaultImages(connection);
        }
    }

    /**
     * Returns a connection to the default database. If there is no open connection, the method calls {@link #openConnection(String dbName)} to start the database-server and create a connection.
     * @return a connection to the default database.
     * @throws DatabaseException if an error occured while opening a new connection or if there is an open connection to the test-database.
     */
    @Override
    public Connection getConnection() throws DatabaseException {
        if(connection==null){
            try {
                openConnection("openbooth");
            } catch (SQLException|ClassNotFoundException e) {
                throw new DatabaseException(e);
            }
        }
        return connection;
    }


    /**
     * Closes the connection and stops the H2-Server.
     */
    @Override
    public void closeConnection() {

        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("closeConnection - unable to close connection - ",e);
        } finally {
            connection=null;
            if(h2Server!=null){
                h2Server.stop();
            }
        }
    }
}
