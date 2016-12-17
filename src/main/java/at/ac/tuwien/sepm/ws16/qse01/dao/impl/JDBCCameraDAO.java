package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.CameraDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JDBCCameraDAO implements CameraDAO{

    private Connection con;
    static final Logger LOGGER = LoggerFactory.getLogger(JDBCCameraDAO.class);


    @Autowired
    public JDBCCameraDAO(DBHandler dbHandler) throws PersistenceException {
        con = dbHandler.getConnection();
    }

    @Override
    public Camera create(Camera camera) throws PersistenceException {
        LOGGER.debug("Entering create method with parameters {}"+camera);

        PreparedStatement stmt = null;
        String query = "INSERT INTO cameras(label,modelName,portNumber,serialNumber) VALUES (?,?,?,?) ; ";


        Integer id = -1;
        try {
            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1,camera.getLable());
            stmt.setString(2,camera.getModel());

            stmt.setString (3,camera.getPort());
            stmt.setString (4,camera.getSerialnumber());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                id=rs.getInt(1);
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
        camera.setId(id);
        return camera;
    }

    @Override
    public void delete(int cameraID) throws PersistenceException {
        String sql = "DELETE FROM CAMERAS WHERE  CAMERAID=?";
        PreparedStatement stmt = null;

        try{
            stmt= con.prepareStatement(sql);
            stmt.setInt(1,cameraID);
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
    public List<Camera> readActive() throws PersistenceException {
        List<Camera> cameraList = new ArrayList<>();
        PreparedStatement stmt = null;
        String query = "select * from cameras where ISACTIVE = TRUE ;";

        try {
            stmt = con.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Camera c = new Camera(rs.getInt("CAMERAID"),rs.getString("LABEL"),rs.getString("PORTNUMBER"),rs.getString("MODELNAME"),rs.getString("SERIALNUMBER"));
                cameraList.add(c);
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
                    LOGGER.debug("Closing readActive failed: " + e.getMessage());
                }
            }
        }
        return cameraList;
    }

    @Override
    public void setActive(int cameraID) throws PersistenceException {
        PreparedStatement stmt=null;
        try {
            String prepered="update cameras set isactive=true where cameraid= ?";
            stmt = con.prepareStatement(prepered);

            stmt.setInt(1,cameraID);
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.error("CameraDAO", e.getMessage());
            throw new PersistenceException(e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void setInactive(int cameraID) throws PersistenceException {
        PreparedStatement stmt=null;
        try {
            String prepered="update cameras set isactive=false where cameraid= ?";
            stmt = con.prepareStatement(prepered);

            stmt.setInt(1,cameraID);
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.error("CameraDAO", e.getMessage());
            throw new PersistenceException(e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void setAllInactive() throws PersistenceException {
        PreparedStatement stmt=null;
        try {
            String prepered="update cameras set isactive=false";
            stmt = con.prepareStatement(prepered);

            stmt.execute();

        } catch (SQLException e) {
            LOGGER.error("CameraDAO", e.getMessage());
            throw new PersistenceException(e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public Camera exists(Camera camera) throws PersistenceException {
        PreparedStatement stmt = null;
        String query = "select * from cameras where PORTNUMBER = ? AND MODELNAME = ? ;";
        Camera ret=null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1,camera.getPort());
            stmt.setString(2,camera.getModel());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ret=new Camera(rs.getInt("CAMERAID"),rs.getString("LABEL"),rs.getString("PORTNUMBER"),rs.getString("MODELNAME"),rs.getString("SERIALNUMBER"));
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
                    LOGGER.debug("exists - " + e);
                }
            }
        }
        return ret;
    }
}
