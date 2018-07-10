package org.openbooth.util.dbhandler.prep;

import org.h2.tools.RunScript;
import org.openbooth.util.FileTransfer;
import org.openbooth.util.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataPrepper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataPrepper.class);

    private DataPrepper(){}

    /**
     * Runs the create.sql-script on the given database.
     * @param connection the connection to the database
     * @throws DatabaseException if the script cannot be read or an error occurs while writing to the database.
     */
    public static void createTables(Connection connection) throws DatabaseException {
        try (InputStream createStream = DataPrepper.class.getResourceAsStream("/sql/create.sql");
             InputStreamReader createISR = new InputStreamReader(createStream, "UTF-8");
             ResultSet rs = RunScript.execute(connection, createISR)
        ){
            LOGGER.debug("create.sql script successfully run");
        } catch(SQLException|IOException e){
            throw new DatabaseException(e);
        }
    }

    /**
     * Runs the init.sql script on the database
     * @param connection the connection to the database
     * @throws DatabaseException if the script cannot be read or an error occurs while writing to the database.
     */
    public static void initDatabase(Connection connection) throws DatabaseException{
        try(InputStream initStream =  DataPrepper.class.getResourceAsStream("/sql/init.sql");
            InputStreamReader initISR = new InputStreamReader(initStream, "UTF-8");
            ResultSet rs = RunScript.execute(connection, initISR)
        ){
            LOGGER.debug("init.sql script successfully run");
        } catch (IOException | SQLException e){
            throw new DatabaseException(e);
        }
    }

    /**
     * Runs insert.sql for inserting some dummy data to the given database to enable developers to test the application.
     * @param connection the connection to the database
     * @throws DatabaseException if an error occurs while reading the scripts or while making the entries in the database.
     */
    public static void insertTestData(Connection connection) throws DatabaseException{

        try (InputStream insertStream = DataPrepper.class.getResourceAsStream("/sql/insert.sql");
             InputStreamReader insertISR = new InputStreamReader(insertStream);
             ResultSet rs = RunScript.execute(connection, insertISR))
        {
            LOGGER.debug("insert.sql successfully run");
        } catch(SQLException|IOException e){
            throw new DatabaseException(e);
        }
    }

    public static void deleteTestData(Connection connection) throws DatabaseException{
        try (InputStream deleteStream = DataPrepper.class.getResourceAsStream("/sql/delete.sql");
             InputStreamReader insertISR = new InputStreamReader(deleteStream);
             ResultSet rs = RunScript.execute(connection, insertISR))
        {
            LOGGER.debug("delete.sql successfully run");
        } catch(SQLException|IOException e){
            throw new DatabaseException(e);
        }
    }

    /**
     * For test-reasons!
     * Copies some files used for tests and makes an entry in the database.
     * @throws DatabaseException if an error occurs while making the new entries.
     */
    public static void setUpDefaultImages(Connection connection) throws DatabaseException {
        String destPath = System.getProperty("user.home") + "/.openbooth/BeispielBilder/";
        String shootingPath=System.getProperty("user.home") + "/openbooth/shooting1/";

        String image1Source = "/images/dummies/p1.jpg";
        String image2Source ="/images/dummies/p2.jpg";
        String logo2Source = "/images/logos/logo1.jpg";
        String green1Source ="/images/greenscreen/background/test_background1.jpg";
        String green2Source ="/images/greenscreen/background/test_background2.png";
        String green3Source ="/images/greenscreen/background/test_background3.jpg";
        String button1Source="/images/position/camerafront.png";
        String button2Source="/images/position/cameraside.png";
        String button3Source="/images/position/cameratop.png";

        String image1Dest = shootingPath+"image1.jpg";
        String image2Dest = shootingPath+"image2.jpg";
        String logo2Dest = destPath+"logo1.jpg";
        String green1Dest = destPath+"greenBg1.jpg";
        String green2Dest = destPath+"greenBg2.png";
        String green3Dest = destPath+"greenBg3.jpg";
        String button1Dest = destPath+"camerafront.png";
        String button2Dest = destPath+"cameraside.png";
        String button3Dest = destPath+"cameratop.png";


        try {
            FileTransfer ft=new FileTransfer();
            ft.transfer(image1Source,image1Dest);
            ft.transfer(image2Source, image2Dest);
            ft.transfer(logo2Source, logo2Dest);
            ft.transfer(green1Source,green1Dest);
            ft.transfer(green2Source,green2Dest);
            ft.transfer(green3Source,green3Dest);
            ft.transfer(button1Source,button1Dest);
            ft.transfer(button2Source,button2Dest);
            ft.transfer(button3Source,button3Dest);



            try (PreparedStatement stmt = connection.prepareStatement("UPDATE images SET imagepath=? where imageID=?;")) {
                stmt.setString(1, image1Dest);
                stmt.setInt(2, 1);
                stmt.execute();

                stmt.setString(1, image2Dest);
                stmt.setInt(2, 2);
                stmt.execute();
            }


            try(PreparedStatement stmt = connection.prepareStatement("UPDATE logos SET path=?, label=? where logoID=?;")) {
                stmt.setString(1, logo2Dest);
                stmt.setString(2, "Beispiel-Logo");
                stmt.setInt(3, 2);
                stmt.execute();
            }


            try(PreparedStatement stmt = connection.prepareStatement("UPDATE shootings SET folderpath=? WHERE shootingID=1")) {
                stmt.setString(1, shootingPath);
                stmt.execute();
            }


            try(PreparedStatement stmt = connection.prepareStatement("UPDATE backgrounds SET path=? WHERE backgroundID=?")) {
                stmt.setString(1, green1Dest);
                stmt.setInt(2, 1);
                stmt.execute();

                stmt.setString(1, green2Dest);
                stmt.setInt(2, 2);
                stmt.execute();

                stmt.setString(1, green3Dest);
                stmt.setInt(2, 3);
                stmt.execute();
            }



            try(PreparedStatement stmt = connection.prepareStatement("UPDATE positions SET buttonImagePath=? WHERE name=?")){

                stmt.setString(1, button1Dest);
                stmt.setString(2, "DB-Test-Position-1");
                stmt.execute();

                stmt.setString(1, button2Dest);
                stmt.setString(2, "DB-Test-Position-3");
                stmt.execute();

                stmt.setString(1, button3Dest);
                stmt.setString(2, "DB-Test-Position-2");
                stmt.execute();
            }

        } catch (IOException |SQLException e){
            throw new DatabaseException(e);
        }
    }

}
