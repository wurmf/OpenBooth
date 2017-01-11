package at.ac.tuwien.sepm.util.dbhandler.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private boolean testState;
    private Server h2Server;
    private String fileSep;


    private H2EmbeddedHandler() {
        this.connection=null;
        this.testState=false;
        this.fileSep=File.separator;
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
    private void openConnection(String dbName) throws SQLException, ClassNotFoundException, FileNotFoundException, PersistenceException{
        LOGGER.info("Trying to open connection to database "+dbName);
        try {
            Class.forName("org.h2.Driver");
            h2Server= Server.createTcpServer().start();
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+dbName, "sa","");
            ResultSet rs= connection.getMetaData().getTables(null,null,"ADMINUSERS",null);
            if(!rs.next()){
                LOGGER.info("openConnection - first startup detected");
                firstStartup();
            } else{
                LOGGER.info("openConnection - Database "+dbName+" already exists");
            }
        } catch(ClassNotFoundException|SQLException e){
            LOGGER.error("openConnection - ",e);
            throw e;
        }
    }

    /**
     * Returns a connection to the default database. If there is no open connection, the method calls {@link #openConnection(String dbName)} to start the database-server and create a connection.
     * @return a connection to the default database.
     * @throws PersistenceException if an error occured while opening a new connection or if there is an open connection to the test-database.
     */
    @Override
    public Connection getConnection() throws PersistenceException {
        if(connection==null){
            try {
                openConnection("fotostudio");
            } catch (SQLException|ClassNotFoundException|FileNotFoundException e) {
                throw new PersistenceException(e);
            }
        }
        return connection;
    }

    /**
     * Returns a connection to the test-database. If there is no open connection, the method calls {@link #openConnection(String dbName)} to start the database-server and create a connection.
     * @return a connection to the test-database.
     * @throws PersistenceException if an error occured while opening a new connection or if there is an open connection to the default database.
     */
    @Override
    public Connection getTestConnection() throws PersistenceException{
        if(connection==null){
            try {
                testState=true;
                openConnection("fotostudioTest");
            } catch (SQLException|ClassNotFoundException|FileNotFoundException e) {
                throw new PersistenceException(e);
            }
        } else if(!testState){
            LOGGER.error("getConnection - tried to open Connection to test-DB but there is already a connection established to the default-DB");
            throw new PersistenceException("tried to open Connection to test-DB but there is already a connection established to the default-DB");
        }
        return connection;
    }

    /**
     * Closes the connection and stops the H2-Server.
     */
    @Override
    public void closeConnection() {
        testState=false;
        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.info("closeConnection - unable to close connection - ",e);
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
    private void firstStartup() throws FileNotFoundException, SQLException, PersistenceException{
        try {
            String createPath=this.getClass().getResource("/sql/create.sql").getPath();
            String initPath=this.getClass().getResource("/sql/init.sql").getPath();

            ResultSet rs=RunScript.execute(connection, new FileReader(createPath));
            if(rs!=null)
                rs.close();
            if(!testState)
                rs=RunScript.execute(connection, new FileReader(initPath));
            if(rs!=null)
                rs.close();
        } catch(FileNotFoundException|SQLException e){
            LOGGER.error("firstStartup - ",e);
            throw e;
        }
        //TODO: Delete following before building module for client.
        if(!testState) {
            insertData();
            setUpDefaultImgs();
            LOGGER.info("Data inserted into DB, dummy images copied to filesystem");
        }
        LOGGER.info("Database initialized.");
    }

    /**
     * Insert test data into the database
     * @throws FileNotFoundException if the create-script is not found in the specified folder.
     * @throws SQLException if an error occurs while running the script on the database.
     */
    private void insertData() throws FileNotFoundException, SQLException{
        try {
            String insertPath=this.getClass().getResource("/sql/insert.sql").getPath();

            ResultSet rs=RunScript.execute(connection, new FileReader(insertPath));
            if(rs!=null)
                rs.close();
        } catch(FileNotFoundException|SQLException e){
            LOGGER.error("insertData - ",e);
            throw e;
        }
    }

    private void setUpDefaultImgs() throws PersistenceException{
        String destPath = System.getProperty("user.home") + "/fotostudio/BeispielBilder/";
        String image1 = this.getClass().getResource("/images/dummies/p1.jpg").getPath();
        String image2 = this.getClass().getResource("/images/dummies/p2.jpg").getPath();
        String logo1 = this.getClass().getResource("/images/logos/logofamp.jpg").getPath();
        String logo2 =this.getClass().getResource("/images/logos/logo1.jpg").getPath();

        Path img1Source = Paths.get(image1);
        Path img2Source = Paths.get(image2);
        Path logo1Source= Paths.get(logo1);
        Path logo2Source= Paths.get(logo2);

        Path img1Dest= Paths.get(destPath+"p1.jpg");
        Path img2Dest= Paths.get(destPath+"p2.jpg");
        Path logo1Dest= Paths.get(destPath+"logofamp.jpg");
        Path logo2Dest= Paths.get(destPath+"logo1.jpg");

        PreparedStatement stmt=null;
        try {
            if(!img1Dest.getParent().toFile().exists()){
                Files.createDirectory(img1Dest.getParent());
            }
            Files.copy(img1Source,img1Dest, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(img2Source,img2Dest, StandardCopyOption.REPLACE_EXISTING);

            Files.copy(logo1Source,logo1Dest, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(logo2Source,logo2Dest, StandardCopyOption.REPLACE_EXISTING);

            stmt=connection.prepareStatement("UPDATE images SET imagepath=? where imageID=?;");
            stmt.setString(1,destPath+image1);
            stmt.setInt(2,1);
            stmt.execute();

            stmt.setString(1,destPath+image2);
            stmt.setInt(2,2);
            stmt.execute();

            stmt.close();

            stmt=connection.prepareStatement("UPDATE logos SET path=?, label=? where logoID=?;");
            stmt.setString(1,destPath+logo1);
            stmt.setString(2,"Fotografie am Punkt");
            stmt.setInt(3,1);
            stmt.execute();

            stmt.setString(1,destPath+logo2);
            stmt.setString(2,"Beispiel-Logo");
            stmt.setInt(3,2);
            stmt.execute();
        } catch (IOException|SQLException e) {
            LOGGER.error("setUpDefaultImgs - ",e);
            throw new PersistenceException(e);
        } finally {
            if(stmt!=null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("setUpDefaultImgs - ",e);
                    throw new PersistenceException(e);
                }
            }
        }
    }
}
