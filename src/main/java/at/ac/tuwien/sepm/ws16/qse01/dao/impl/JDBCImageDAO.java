package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  Class allows users to create/read an image from database.
 */
@Repository
public class JDBCImageDAO implements ImageDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCImageDAO.class);

    private Connection con;

    @Autowired
    public JDBCImageDAO(DBHandler dbHandler) throws PersistenceException {
       con = dbHandler.getConnection();
    }

    @Override
    public Image create(Image f) throws PersistenceException {
        LOGGER.debug("Entering create method with parameters {}"+f);

        PreparedStatement stmt = null;
        String query = "INSERT INTO images(imagepath,shootingid,time) VALUES (?,?,?) ; ";


        Integer fid = 0;
        try {
            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1,f.getImagepath());
            stmt.setInt(2,f.getShootingid());

            stmt.setTimestamp (3,Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                fid=rs.getInt(1);
            }

        } catch (SQLException e ) {
            throw new PersistenceException("Create failed: "+e.getMessage());
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.debug("Closing create failed: " + e.getMessage());
                }
            }
        }
        f.setImageID(fid);
        return f;
    }

    @Override
    public Image read(int id) throws PersistenceException {
        LOGGER.debug("Entering read method with parameter: imageID = "+id);

        PreparedStatement stmt = null;
        String query = "select * from images where imageID = ? ";
        Image image = null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setInt(1,id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                image = new Image();
                image.setImageID(rs.getInt("imageID"));
                image.setImagepath(rs.getString("imagepath"));
                image.setShootingid(rs.getInt("shootingid"));
                image.setDate(rs.getTimestamp("time"));
            }

        } catch (SQLException e ) {
            throw new PersistenceException("Reading failed: "+e.getMessage());
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.debug("Closing read failed: " + e.getMessage());
                }
            }
        }
        return image;
    }

    @Override
    public void update(Image img) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters {}"+img);

        PreparedStatement stmt = null;
        String query = "UPDATE images set imagepath = ?,shootingid = ?, time=? WHERE imageID = ? ; ";

        try {
            stmt = con.prepareStatement(query);

            stmt.setString(1,img.getImagepath());
            stmt.setInt(2,img.getShootingid());

            stmt.setTimestamp (3, new Timestamp(img.getDate().getTime()));

            stmt.executeUpdate();


        } catch (SQLException e ) {
            throw new PersistenceException("Update failed: "+e.getMessage());
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.debug("Closing update failed: " + e.getMessage());
                }
            }
        }

    }

    @Override
    public String getLastImgPath(int shootingid) throws PersistenceException {
        LOGGER.debug("Entering getLastImgPath method with parameter: shootingid = "+shootingid);

        PreparedStatement stmt = null;
        String query = "select * from images where shootingid = ? order by time DESC LIMIT 1 ";
        String imagepath = null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setInt(1,shootingid);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
               imagepath = rs.getString("imagepath");
                LOGGER.debug("imagepath ="+imagepath);
            }

        } catch (SQLException e ) {
            throw new PersistenceException("GetLastImgPath failed: "+e.getMessage());
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.debug("Closing getLastImgPath failed: " + e.getMessage());
                }
            }
        }
        return imagepath;
    }


    @Override
    public int getNextImageID() throws PersistenceException {
        LOGGER.debug("Entering getNextImageID method");

        PreparedStatement stmt = null;
        String query = "select current_value from information_schema.sequences where sequence_name LIKE ?;";

        int nextImageID = 0;
        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1,"IMAGES_SEQ");

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nextImageID = rs.getInt("current_value")+1;
            }

        } catch (SQLException e ) {
            throw new PersistenceException("getNextImageID failed: "+e.getMessage());
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.debug("Closing getNextImageID failed: " + e.getMessage());
                }
            }
        }
        return nextImageID;
    }

    @Override
    public void delete(int imageID) throws PersistenceException {
        LOGGER.debug("Entering delete method in DAO with imageID ="+imageID);
        String sql = "DELETE FROM Images WHERE  IMAGEID=?";
        PreparedStatement stmt = null;

        try{
            //delete image from filesystem
            Image img = read(imageID);
            if(new File(img.getImagepath()).isFile()) {
                new File(img.getImagepath()).delete();
                LOGGER.debug("image deleted from filesystem!");
            }else if(new File(System.getProperty("user.dir") + "/src/main/resources" + img.getImagepath()).isFile()) {
                new File(System.getProperty("user.dir") + "/src/main/resources" + img.getImagepath()).delete();
                LOGGER.debug("image deleted from filesystem!");
            }else{
                LOGGER.debug("This image does not exist in filesystem!");
            }

            stmt= con.prepareStatement(sql);
            stmt.setInt(1,imageID);
            stmt.execute();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.debug("Closing delete failed: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public List<Image> getAllImages(int shootingid) throws PersistenceException {
        List<Image> imageList = new ArrayList<>();
        PreparedStatement stmt = null;
        String query = "select * from images where shootingid = ? order by imagepath;";

        try {
            stmt = con.prepareStatement(query);
            stmt.setInt(1,shootingid);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Image i = new Image(rs.getInt("IMAGEID"),rs.getString("IMAGEPATH"),shootingid,rs.getTime("TIME"));
                imageList.add(i);
            }

        } catch (SQLException e ) {
            throw new PersistenceException(e.getMessage());
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.debug("Closing getAllImages failed: " + e.getMessage());
                }
            }
        }
        return imageList;
    }




}
