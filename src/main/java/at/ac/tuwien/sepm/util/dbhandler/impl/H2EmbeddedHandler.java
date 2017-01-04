package at.ac.tuwien.sepm.util.dbhandler.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

/**
 * This Singleton-class starts an H2-Server instance and returns a connection to an H2-database called "fotostudio".
 * The class will always return the same connection-object as long as {@link #closeConnection()} is not called. If it is called a new connection will be opened when calling {@link #getConnection()}.
 */
@Component
public class H2EmbeddedHandler  implements DBHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(H2EmbeddedHandler.class);

    private static H2EmbeddedHandler ourInstance = new H2EmbeddedHandler();
    private Connection connection;
    private Server h2Server;


    private H2EmbeddedHandler() {
        this.connection=null;
    }
    public static H2EmbeddedHandler getInstance() {
        return ourInstance;
    }

    /**
     * Opens a new connection to an H2-database of the name "fotostudio" with username "sa" and empty password.
     * If it recognizes that the database has not been initialized yet, {@link #firstStartup()} is called.
     * @throws SQLException if either the driver-class is not found or the DriverManager can't establish a connection to the specified database with the given login credentials.
     * @throws ClassNotFoundException if the driver class is not found.
     * @throws FileNotFoundException if {@link #firstStartup()} throws one.
     */
    private void openConnection() throws SQLException, ClassNotFoundException, FileNotFoundException{
        try {
            Class.forName("org.h2.Driver");
            h2Server= Server.createTcpServer().start();
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/fotostudio", "sa","");
            ResultSet rs= connection.getMetaData().getTables(null,null,"ADMINUSERS",null);
            if(!rs.next()){
                LOGGER.info("openConnection - first startup detected");
                firstStartup();
            } else{
                LOGGER.info("openConnection - Database already exists");
            }
        } catch(ClassNotFoundException|SQLException e){
            LOGGER.error("openConnection - "+e);
            throw e;
        }
    }

    /**
     * Returns a connection to the database. If there is no open connection, the metho calls {@link #openConnection()} to start the database and create a connection.
     * @return a connection to the database.
     * @throws PersistenceException if an error occured while opening the connection, so this can only happen on first call of the method or after calling {@link #closeConnection()}.
     */
    @Override
    public Connection getConnection() throws PersistenceException {
        if(connection==null){
            try {
                openConnection();
            } catch (SQLException|ClassNotFoundException|FileNotFoundException e) {
                throw new PersistenceException(e);
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
            LOGGER.info("closeConnection - unable to close connection - "+e);
        } finally {
            connection=null;
            if(h2Server!=null){
                h2Server.stop();
            }
        }
    }

    /**
     * Runs the create.sql-script on the database.
     * @throws FileNotFoundException if the create-script is not found in the specified folder.
     * @throws SQLException if an error occurs while running the script on the database.
     */
    private void firstStartup() throws FileNotFoundException, SQLException{
        try {
            ResultSet rs=RunScript.execute(connection, new FileReader("sql/create.sql"));
            if(rs!=null) rs.close();
            rs=RunScript.execute(connection, new FileReader("sql/init.sql"));
            if(rs!=null) rs.close();
        } catch(FileNotFoundException|SQLException e){
            LOGGER.error("firstStartup - "+e);
            throw e;
        }
        //TODO: Delete following before building module for client.
        insertTestData();
        LOGGER.info("Database initialized.");
    }

    /**
     * Insert test data into the database
     * @throws FileNotFoundException if the create-script is not found in the specified folder.
     * @throws SQLException if an error occurs while running the script on the database.
     */
    private void insertTestData() throws FileNotFoundException, SQLException{
        try {
            ResultSet rs=RunScript.execute(connection, new FileReader("sql/insert.sql"));
            if(rs!=null && !rs.isClosed())rs.close();
        } catch(FileNotFoundException|SQLException e){
            LOGGER.error("insertTestData - "+e);
            throw e;
        }
    }

    /**
     * Runs scripts in the following order:
     * <ul>
     *     <li>drop.sql</li>
     *     <li>create.sql</li>
     *     <li>init.sql</li>
     *     <li>insert.sql</li>
     * </ul>
     * @throws PersistenceException if an error occurs while reading the sql-script-files or while executing them.
     */
    public void resetDBForTest() throws PersistenceException{
        getConnection();
        try{
            ResultSet rs=RunScript.execute(connection, new FileReader("sql/delete.sql"));
            if(rs!=null && !rs.isClosed())rs.close();
            rs=RunScript.execute(connection, new FileReader("sql/init.sql"));
            if(rs!=null && !rs.isClosed())rs.close();
        } catch(FileNotFoundException|SQLException e){
            LOGGER.error("resetForTest - "+e);
            throw new PersistenceException(e);
        }
        try{
            insertTestData();     //Run create.sql, init.sql, insert.sql
        } catch(FileNotFoundException|SQLException e){
            throw new PersistenceException(e);
        }
    }
}
