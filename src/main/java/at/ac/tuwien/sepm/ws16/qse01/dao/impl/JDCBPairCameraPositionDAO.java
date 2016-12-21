package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.CameraDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.PairCameraPositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.PositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 database-Specific PairCameraPositionDAO Implementation
 */
@Repository
public class JDCBPairCameraPositionDAO implements PairCameraPositionDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(JDCBPairCameraPositionDAO.class);
    private Connection con;
    private CameraDAO cameraDAO;
    private PositionDAO positionDAO;

    @Autowired
    public JDCBPairCameraPositionDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        con = handler.getConnection();
        cameraDAO = new JDBCCameraDAO(H2Handler.getInstance());
        positionDAO = new JDBCPositionDAO(H2Handler.getInstance());
    }

    @Override
    public Profile.PairCameraPosition create(Profile.PairCameraPosition pairCameraPosition) throws PersistenceException {
        LOGGER.debug("Entering create method with parameter " + pairCameraPosition);

        if (pairCameraPosition==null) throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        try {
            //AutoID
            if (pairCameraPosition.getId() == Integer.MIN_VALUE) {
                sqlString = "INSERT INTO profile_camera_positions(profileId,cameraId,positionId,isGreenscreenReady) VALUES (?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,pairCameraPosition.getProfileId());
                stmt.setInt(2, pairCameraPosition.getCamera().getId());
                stmt.setInt(3, pairCameraPosition.getPosition().getId());
                stmt.setBoolean(4, pairCameraPosition.isGreenScreenReady());
                stmt.executeUpdate();
                //Get autoassigned id
                rs = stmt.getGeneratedKeys();
                if (rs.next()){pairCameraPosition.setId(rs.getInt(1));}
                LOGGER.debug("Create successfully with AutoID:" + pairCameraPosition.getId());
            }
            //NoAutoID
            else
            {
                sqlString = "INSERT INTO profile_camera_positions(profile_camera_positions_id,profileId,cameraId,positionId,isGreenscreenReady) VALUES (?,?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,pairCameraPosition.getId());
                stmt.setInt(2,pairCameraPosition.getProfileId());
                stmt.setInt(3, pairCameraPosition.getCamera().getId());
                stmt.setInt(4, pairCameraPosition.getPosition().getId());
                stmt.setBoolean(5, pairCameraPosition.isGreenScreenReady());
                stmt.executeUpdate();
                LOGGER.debug("Create successfully with AutoID:" + pairCameraPosition.getId());
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating object in persistence layer has failed.:" + e);
        }
        finally{
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        // Return persisted object
        return pairCameraPosition;
    }

    @Override
    public List<Profile.PairCameraPosition> createAll(List<Profile.PairCameraPosition> pairCameraPositions) throws PersistenceException {
        LOGGER.debug("Entering createAll method");
        List<Profile.PairCameraPosition> returnValue = new ArrayList<>();
        for (Profile.PairCameraPosition pairCameraPosition : pairCameraPositions) {
            try {
                returnValue.add(this.create(pairCameraPosition));
            }
            catch (PersistenceException e) {
                throw new PersistenceException("Error! CreateAll objects in persistence layer has failed.:" + e);
            }
        }
        LOGGER.debug("Persisted createAll method has completed successfully " + pairCameraPositions);
        return  returnValue;
    }

    @Override
    public boolean update(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        return false;
    }

    @Override
    public List<Profile.PairCameraPosition> readAll(int profileId) throws PersistenceException {
        LOGGER.debug("Entering readAll method");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        sqlString = "SELECT * FROM profile_camera_positions where profileId = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,profileId);
            rs = stmt.executeQuery();
            List<Profile.PairCameraPosition> returnList = new ArrayList<>();

            while (rs.next()) {
                Profile.PairCameraPosition pairCameraPosition =
                        new Profile.PairCameraPosition(
                                new Camera(rs.getInt("cameraId"),"","","",""),
                                (new JDBCPositionDAO(H2Handler.getInstance()).read(rs.getInt("positionId"))),
                                rs.getBoolean("isGreenscreenReady"));
                returnList.add(pairCameraPosition);
            }
            LOGGER.debug("Persisted object readingAll has been successfully. " + returnList);
            return returnList;
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Reading all objects in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of readAll method call has failed.:" + e);}
        }
    }

    @Override
    public boolean delete(int profileId,Profile.PairCameraPosition pairCameraPosition) throws PersistenceException {
        LOGGER.debug("Entering delete method with parameters " + pairCameraPosition);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "DELETE FROM profile_camera_positions"
                + " WHERE profileId = ? AND cameraId = ? AND positionId = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, profileId);
            stmt.setInt(2, pairCameraPosition.getCamera().getId());
            stmt.setInt(3, pairCameraPosition.getPosition().getId());
            stmt.executeUpdate();
            rs = stmt.getResultSet();
            // Check, if object has been deleted and return suitable boolean value
            if (rs.next()) {
                LOGGER.debug("Persisted object deletion has been successfully(returned value true)");
                return true;
            } else {
                LOGGER.debug("Provided object has been not deleted, since it doesn't exist in persistence data store(returned value false)");
                return false;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Deleting object in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);}
        }
    }

    @Override
    public boolean deleteAll(Profile profile) throws PersistenceException {
        LOGGER.debug("Entering deleteAll method");
        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "DELETE FROM profile_camera_positions"
                + " WHERE profileId = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, profile.getId());
            stmt.executeUpdate();
            rs = stmt.getResultSet();
            // Check, if objects have been deleted and return suitable boolean value
            if (rs.next()) {
                LOGGER.debug("Persisted objects deletion has been successfully(returned value true)");
                return true;
            } else {
                LOGGER.debug("Provided object has been not deleted, since it doesn't exist in persistence data store(returned value false)");
                return false;
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error! Deleting objects in persistence layer has failed.:" + e);
        } finally {
            // Return resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);
            }
        }
    }
}