package org.openbooth.dao.impl;

import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.CameraDAO;
import org.openbooth.dao.PairCameraPositionDAO;
import org.openbooth.dao.PositionDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Profile;
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
public class JDBCPairCameraPositionDAO implements PairCameraPositionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCPairCameraPositionDAO.class);
    private Connection con;
    private CameraDAO cameraDAO;
    private PositionDAO positionDAO;

    public static final String TABLE_NAME = "profile_camera_positions";
    public static final String ID_COLUMN = "profile_camera_positions_id";
    public static final String PROFILE_ID_COLUMN = "profileId";
    public static final String CAMERA_ID_COLUMN = "cameraId";
    public static final String POSITION_ID_COLUMN = "positionId";
    public static final String GREEN_SCREEN_READY_COLUMN = "isGreenscreenReady";


    @Autowired
    public JDBCPairCameraPositionDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            LOGGER.error("Constructor - ",e);
            throw new PersistenceException(e);
        }
        cameraDAO = new JDBCCameraDAO(handler);
        positionDAO = new JDBCPositionDAO(handler);
    }

    @Override
    public Profile.PairCameraPosition create(Profile.PairCameraPosition pairCameraPosition) throws PersistenceException {
        LOGGER.debug("Entering create method with parameter " + pairCameraPosition);

        if (pairCameraPosition==null)
            throw new IllegalArgumentException("Error!:Called create method with null pointer.");
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
                if (rs.next())
                    {pairCameraPosition.setId(rs.getInt(1));}
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
            try {
                if (stmt != null)
                    stmt.close();}
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
    public boolean update(Profile.PairCameraPosition pairCameraPosition)throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + pairCameraPosition);
        if(pairCameraPosition==null)
            throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE profile_camera_positions SET profileId = ?,cameraId = ?,positionId = ?,isGreenscreenReady = ? WHERE profile_camera_positions_id = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,pairCameraPosition.getProfileId());
            stmt.setInt(2,pairCameraPosition.getCamera().getId());
            stmt.setInt(3,pairCameraPosition.getPosition().getId());
            stmt.setBoolean(4,pairCameraPosition.isGreenScreenReady());
            stmt.setInt(5,pairCameraPosition.getId());
            stmt.executeUpdate();
            int returnUpdateCount = stmt.executeUpdate();

            // Check, if object has been updated and return suitable boolean value or throw Exception
            if (returnUpdateCount == 1){
                LOGGER.debug("Update has been successfully(return value true)");
                return true;
            }
            else if (returnUpdateCount == 0) {
                LOGGER.debug("Updated nothing , since it doesn't exist in persistence data store(return value false)");
                return false;}
            else {
                throw new PersistenceException("Error! Updating in persistence layer has failed.:Consistence of persistence store is broken! This should not happen!");
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Update in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {
                if (stmt != null)
                    stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of update method call has failed.:" + e);}
        }
    }

    @Override
    public Profile.PairCameraPosition read(int id) throws PersistenceException {
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM profile_camera_positions WHERE profile_camera_positions_id = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                Profile.PairCameraPosition pairCameraPosition
                        = new Profile.PairCameraPosition(
                        rs.getInt("profile_camera_positions_id"),
                        rs.getInt("profileId"),
                        cameraDAO.read(rs.getInt("cameraID")),
                        positionDAO.read(rs.getInt("positionID")),
                        rs.getBoolean("isGreenscreenReady")
                );
                LOGGER.debug("Read has been successfully. " + pairCameraPosition);
                return pairCameraPosition;
            }
            else {
                LOGGER.debug("Read nothing, since it doesn't exist in persistence data store(return null)");
                return null;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Read in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {
                if (stmt != null)
                    stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of read method call has failed.:" + e);}
        }
    }

    @Override
    public List<Profile.PairCameraPosition> readAllWithProfileID(int profileId) throws PersistenceException {
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
                        this.read(rs.getInt("profile_camera_positions_id"));
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
            try {
                if (stmt != null)
                    stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of readAll method call has failed.:" + e);}
        }
    }

    @Override
    public boolean delete(Profile.PairCameraPosition pairCameraPosition) throws PersistenceException {
        LOGGER.debug("Entering delete method with parameters " + pairCameraPosition);

        if (pairCameraPosition==null)
            throw new IllegalArgumentException("Error!:Called delete method with null pointer.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "DELETE FROM profile_camera_positions"
                + " WHERE profile_camera_positions_id = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, pairCameraPosition.getId());
            int returnUpdateCount  = stmt.executeUpdate();

            // Check, if row has been deleted and return suitable boolean value
            if (returnUpdateCount == 1){
                LOGGER.debug("Delete has been successfully(returned value true)");
                return true;
            }
            else if (returnUpdateCount == 0){
                LOGGER.debug("Deleted nothing, since it didn't exist in persistence data store(returned value false)");
                return false;
            }
            else {
                throw new PersistenceException("Error! Deleting in persistence layer has failed.:Consistence of persistence store is broken! This should not happen!");
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Deleting object in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {
                if (stmt != null)
                    stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);}
        }
    }

    @Override
    public boolean deleteAllWithProfileID(int profileId) throws PersistenceException {
        LOGGER.debug("Entering deleteAll method");
        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "DELETE FROM profile_camera_positions"
                + " WHERE profileId = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, profileId);
            stmt.executeUpdate();
            rs = stmt.getResultSet();
            // Check, if objects have been deleted and return suitable boolean value
            if (rs.next()) {
                LOGGER.debug("DeleteAllWithProfileID has been successfully(returned value true)");
                return true;
            } else {
                LOGGER.debug("Provided object has been not deleted, since it doesn't exist in persistence data store(returned value false)");
                return false;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Deleting objects in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {
                if (stmt != null)
                    stmt.close();
            }
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);
            }
        }
    }
}