package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  Class allows users to create/read an image from database.
 */
@Repository
public class JDBCImageDAO implements ImageDAO {
    final static Logger LOGGER = LoggerFactory.getLogger(JDBCImageDAO.class);

    private Connection con;

    @Autowired
    public JDBCImageDAO(DBHandler dbHandler) throws PersistenceException {
       con = dbHandler.getConnection();
    }

    @Override
    public Image create(Image f) {
        LOGGER.debug("Entering create method with parameters {}"+f);

        PreparedStatement stmt = null;
        String query = "INSERT INTO images(imagepath,shootingid,time) VALUES (?,?,?) ; ";



        Integer fid = 0;
        try {
            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1,f.getImagepath());
            stmt.setInt(2,f.getShootingid());

            stmt.setTimestamp (3,Timestamp.valueOf(LocalDateTime.now())); //new java.sql.Date(f.getDate().getTime()));
            //  stmt.setDate(4,new java.sql.Date(p.getBirthdate().getTime()));


            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                fid=rs.getInt(1);
            }

        } catch (SQLException e ) {
            new IllegalArgumentException("Create failed",e);
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Create",e);
                }
            }
        }
        f.setImageID(fid);
        return f;
    }

    @Override
    public Image read(int id) {
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
            new IllegalArgumentException("Select failed",e);
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Select",e);
                }
            }
        }
        return image;
    }

    @Override
    public String getLastImgPath(int shootingid) {
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
            }

        } catch (SQLException e ) {
            new IllegalArgumentException("Select failed",e);
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Select",e);
                }
            }
        }
        return imagepath;
    }

    @Override
    public List<String> getAllImagePaths(int shootingid) {
       List<String> paths = new ArrayList<String>();
        LOGGER.debug("Entering getAllImagePaths method with parameter: shootingid = "+shootingid);

        PreparedStatement stmt = null;
        String query = "select * from images where shootingid = ? ;";

        try {
            stmt = con.prepareStatement(query);
            stmt.setInt(1,shootingid);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                paths.add(rs.getString("imagepath"));
            }

        } catch (SQLException e ) {
            new IllegalArgumentException("Select failed",e);
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Select",e);
                }
            }
        }
        return paths;
    }

    @Override
    public int getNextImageID() {
        LOGGER.debug("Entering getNextImageID method");

        PreparedStatement stmt = null;
        String query = "select current_value from information_schema.sequences where id = ? ;";

        int nextImageID = 0;
        try {
            stmt = con.prepareStatement(query);
            stmt.setInt(1,17); // 17 ist die ID von imageID-sequence

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                nextImageID = rs.getInt("current_value")+1;
            }

        } catch (SQLException e ) {
            new IllegalArgumentException("Select failed",e);
        } catch(NullPointerException e){
            throw new IllegalArgumentException();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Select",e);
                }
            }
        }
        return nextImageID;
    }
}
