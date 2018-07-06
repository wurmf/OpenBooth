package org.openbooth.util.dbhandler.prep;

import org.h2.tools.RunScript;
import org.openbooth.util.FileTransfer;
import org.openbooth.util.exceptions.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataPrepper {



    /**
     * Runs the create.sql-script on the database.
     * @throws DatabaseException if the scripts cannot be read or an error occurs while writing to the database.
     */
    public void firstStartup(Connection connection, boolean testState) throws DatabaseException {
        try {
            InputStream createStream =  this.getClass().getResourceAsStream("/sql/create.sql");
            InputStream initStream =    this.getClass().getResourceAsStream("/sql/init.sql");

            InputStreamReader createISR =   new InputStreamReader(createStream, "UTF-8");
            InputStreamReader initISR =     new InputStreamReader(initStream, "UTF-8");

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
            throw new DatabaseException(e);
        }


        //TODO: Delete following before building module for client.
        if(!testState) {
            insertDummyData(connection);
            setUpDefaultImgs(connection);
        }
    }

    /**
     * Insert some dummy data to enable developers to test the application.
     * @throws DatabaseException if an error occurs while reading the scripts or while making the entries in the database.
     */
    private void insertDummyData(Connection connection) throws DatabaseException{

        try (InputStream insertStream=this.getClass().getResourceAsStream("/sql/insert.sql");InputStreamReader insertISR=new InputStreamReader(insertStream);ResultSet rs=RunScript.execute(connection, insertISR)) {
            // No action required, already Runscript.execute does what is required
        } catch(SQLException|IOException e){
            throw new DatabaseException(e);
        }
    }

    /**
     * For test-reasons!
     * Copies some files used for tests and makes an entry in the database.
     * @throws DatabaseException if an error occurs while making the new entries.
     */
    private void setUpDefaultImgs(Connection connection) throws DatabaseException {
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

        PreparedStatement stmt=null;

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

            stmt=connection.prepareStatement("UPDATE images SET imagepath=? where imageID=?;");
            stmt.setString(1,image1Dest);
            stmt.setInt(2,1);
            stmt.execute();

            stmt.setString(1,image2Dest);
            stmt.setInt(2,2);
            stmt.execute();

            stmt.close();

            stmt=connection.prepareStatement("UPDATE logos SET path=?, label=? where logoID=?;");
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
        } catch (IOException |SQLException e){
            throw new DatabaseException(e);
        } finally{
            if(stmt!=null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    //close quietly
                }
            }
        }
    }

}
