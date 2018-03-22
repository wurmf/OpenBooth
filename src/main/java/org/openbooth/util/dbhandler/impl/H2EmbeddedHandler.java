package org.openbooth.util.dbhandler.impl;

import org.openbooth.util.FileTransfer;
import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.*;

/**
 * This Singleton-class starts an H2-Server instance and returns a connection to an H2-database called "openbooth".
 * The class will always return the same connection-object as long as {@link #closeConnection()} is not called. If it is called a new connection will be opened when calling {@link #getConnection()}.
 */
@Component
public class H2EmbeddedHandler  implements DBHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(H2EmbeddedHandler.class);

    private static H2EmbeddedHandler ourInstance = new H2EmbeddedHandler();
    private Connection connection;
    private boolean testState;
    private Server h2Server;


    private H2EmbeddedHandler() {
        this.connection=null;
        this.testState=false;
    }
    public static H2EmbeddedHandler getInstance() {
        return ourInstance;
    }

    /**
     * Opens a new connection to an H2-database of the name "openbooth" with username "sa" and empty password.
     * If it recognizes that the database has not been initialized yet, {@link #firstStartup()} is called.
     * @throws SQLException if either the driver-class is not found or the DriverManager can't establish a connection to the specified database with the given login credentials.
     * @throws ClassNotFoundException if the driver class is not found.
     * @throws FileNotFoundException if {@link #firstStartup()} throws one.
     */
    private void openConnection(String dbName) throws SQLException, ClassNotFoundException, DatabaseException, IOException{
        LOGGER.info("openConnection - Trying to open connection to database {}", dbName);
        try {
            Class.forName("org.h2.Driver");
            h2Server= Server.createTcpServer().start();
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+dbName, "sa","");
            ResultSet rs= connection.getMetaData().getTables(null,null,"ADMINUSERS",null);
            if(!rs.next()){
                LOGGER.info("openConnection - first startup detected");
                firstStartup();
            }
            LOGGER.info("openConnection - connected to database '{}'",dbName);
        } catch(ClassNotFoundException|SQLException e){
            LOGGER.error("openConnection - ",e);
            throw e;
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
            } catch (SQLException|ClassNotFoundException|IOException e) {
                throw new DatabaseException(e);
            }
        }
        return connection;
    }

    /**
     * Returns a connection to the test-database. If there is no open connection, the method calls {@link #openConnection(String dbName)} to start the database-server and create a connection.
     * @return a connection to the test-database.
     * @throws DatabaseException if an error occured while opening a new connection or if there is an open connection to the default database.
     */
    @Override
    public Connection getTestConnection() throws DatabaseException{
        if(connection==null){
            try {
                testState=true;
                openConnection("übopenboothTest");
            } catch (SQLException|ClassNotFoundException|IOException e) {
                throw new DatabaseException(e);
            }
        } else if(!testState){
            LOGGER.error("getConnection - tried to open Connection to test-DB but there is already a connection established to the default-DB");
            throw new DatabaseException("tried to open Connection to test-DB but there is already a connection established to the default-DB");
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
     * @throws DatabaseException if the scripts cannot be read or an error occurs while writing to the database.
     */
    private void firstStartup() throws DatabaseException {
        try {
            InputStream createStream=this.getClass().getResourceAsStream("/sql/create.sql");
            InputStream initStream=this.getClass().getResourceAsStream("/sql/init.sql");

            InputStreamReader createISR= new InputStreamReader(createStream, "UTF-8");
            InputStreamReader initISR= new InputStreamReader(initStream, "UTF-8");

            ResultSet rs=RunScript.execute(connection, createISR);
            if(rs!=null)
                rs.close();
            if(!testState)
                rs=RunScript.execute(connection, initISR);
            if(rs!=null)
                rs.close();

            createISR.close();
            createStream.close();

            initISR.close();
            initStream.close();

        } catch(SQLException|IOException e){
            LOGGER.error("firstStartup - ",e);
            throw new DatabaseException(e);
        }
        //TODO: Delete following before building module for client.
        if(!testState) {
            insertData();
            setUpDefaultImgs();
            LOGGER.info("firstStartup - Data inserted into DB, dummy images copied to filesystem");
        }
        LOGGER.info("firstStartup - Database initialized.");
    }

    /**
     * Insert test data into the database
     * @throws DatabaseException if an error occurs while reading the scripts or while making the entries in the database.
     */
    private void insertData() throws DatabaseException{
        //Only for identifying the origin of the exception
        String exceptionMsgStart="insertData - ";

        ResultSet rs=null;
        InputStream insertStream=null;
        InputStreamReader insertISR=null;
        try {
            insertStream=this.getClass().getResourceAsStream("/sql/insert.sql");
            insertISR=new InputStreamReader(insertStream);

            rs=RunScript.execute(connection, insertISR);
        } catch(SQLException e){
            LOGGER.error(exceptionMsgStart,e);
            throw new DatabaseException(e);
        } finally{
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.error(exceptionMsgStart,e);
                }
            }
            if(insertISR!=null){
                try {
                    insertISR.close();
                } catch (IOException e) {
                    LOGGER.error(exceptionMsgStart,e);
                }
            }
            if(insertStream!=null){
                try {
                    insertStream.close();
                } catch (IOException e) {
                    LOGGER.error(exceptionMsgStart,e);
                }
            }
        }
    }

    /**
     * For test-reasons!
     * Copies some files used for tests and makes an entry in the database.
     * @throws DatabaseException if an error occurs while making the new entries.
     */
    private void setUpDefaultImgs() throws DatabaseException {
        String destPath = System.getProperty("user.home") + "/.openbooth/BeispielBilder/";
        String shootingPath=System.getProperty("user.home") + "/openbooth/shooting1/";

        String image1Source = "/images/dummies/p1.jpg";
        String image2Source ="/images/dummies/p2.jpg";
        String logo1Ssource ="/images/logos/logofamp.jpg";
        String logo2Source ="/images/logos/logo1.jpg";
        String green1Source ="/images/greenscreen/background/test_background1.jpg";
        String green2Source ="/images/greenscreen/background/test_background2.png";
        String green3Source ="/images/greenscreen/background/test_background3.jpg";
        String button1Source="/images/position/camerafront.png";
        String button2Source="/images/position/cameraside.png";
        String button3Source="/images/position/cameratop.png";

        String image1Dest = shootingPath+"image1.jpg";
        String image2Dest = shootingPath+"image2.jpg";
        String logo1Dest = destPath+"logofamp.jpg";
        String logo2Dest = destPath+"logo1.jpg";
        String green1Dest = destPath+"greenBg1.jpg";
        String green2Dest = destPath+"greenBg2.png";
        String green3Dest = destPath+"greenBg3.jpg";
        String button1Dest = destPath+"camerafront.png";
        String button2Dest = destPath+"cameraside.png";
        String button3Dest = destPath+"cameratop.png";

        PreparedStatement stmt=null;
        try{
            FileTransfer ft=new FileTransfer();
            ft.transfer(image1Source,image1Dest);
            ft.transfer(image2Source, image2Dest);
            ft.transfer(logo1Ssource, logo1Dest);
            ft.transfer(logo2Source, logo2Dest);
            ft.transfer(green1Source,green1Dest);
            ft.transfer(green2Source,green2Dest);
            ft.transfer(green3Source,green3Dest);
            ft.transfer(button1Source,button1Dest);
            ft.transfer(button2Source,button2Dest);
            ft.transfer(button3Source,button3Dest);

            stmt=connection.prepareStatement("UPDATE images SET imagepath=? where imageID=?;");
            stmt.setString(1,image1Dest);
            stmt.setInt(2,1);
            stmt.execute();

            stmt.setString(1,image2Dest);
            stmt.setInt(2,2);
            stmt.execute();

            stmt.close();

            stmt=connection.prepareStatement("UPDATE logos SET path=?, label=? where logoID=?;");
            stmt.setString(1,logo1Dest);
            stmt.setString(2,"Fotografie am Punkt");
            stmt.setInt(3,1);
            stmt.execute();

            stmt.setString(1,logo2Dest);
            stmt.setString(2,"Beispiel-Logo");
            stmt.setInt(3,2);
            stmt.execute();

            stmt.close();

            stmt=connection.prepareStatement("UPDATE shootings SET folderpath=? WHERE shootingID=1");
            stmt.setString(1,shootingPath);
            stmt.execute();

            stmt.close();

            stmt=connection.prepareStatement("UPDATE backgrounds SET path=? WHERE backgroundID=?");
            stmt.setString(1,green1Dest);
            stmt.setInt(2,1);
            stmt.execute();

            stmt.setString(1,green2Dest);
            stmt.setInt(2,2);
            stmt.execute();

            stmt.setString(1,green3Dest);
            stmt.setInt(2,3);
            stmt.execute();

            stmt.close();

            stmt=connection.prepareStatement("UPDATE positions SET buttonImagePath=? WHERE name=?");
            stmt.setString(1,button1Dest);
            stmt.setString(2,"DB-Test-Position-1");
            stmt.execute();

            stmt.setString(1,button2Dest);
            stmt.setString(2,"DB-Test-Position-3");
            stmt.execute();

            stmt.setString(1,button3Dest);
            stmt.setString(2,"DB-Test-Position-2");
            stmt.execute();
        } catch (IOException|SQLException e){
            LOGGER.error("setUpDefaultImgs - ",e);
            throw new DatabaseException(e);
        } finally{
            if(stmt!=null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.info("setUpDefaultImgs - ",e);
                    //close quietly
                }
            }
        }
    }
}
