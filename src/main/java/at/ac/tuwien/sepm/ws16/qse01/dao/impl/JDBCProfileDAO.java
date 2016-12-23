package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.PairCameraPositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.PairLogoRelativeRectangleDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.ProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 database-Specific ProfileDAO Implementation
 */
@Repository
public class JDBCProfileDAO implements ProfileDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(JDBCProfileDAO.class);
    private Connection con;
    private PairCameraPositionDAO pairCameraPositionDAO;
    private PairLogoRelativeRectangleDAO pairLogoRelativeRectangleDAO;

    @Autowired
    public JDBCProfileDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        con = handler.getConnection();
        pairCameraPositionDAO = new JDCBPairCameraPositionDAO(H2Handler.getInstance());
        pairLogoRelativeRectangleDAO = new JDCBPairLogoRelativeRectangleDAO(H2Handler.getInstance());
    }

    @Override
    public Profile create(Profile profile) throws PersistenceException{
        LOGGER.debug("Entering create method with parameters " + profile);
        if(profile==null)throw new IllegalArgumentException("Error!:Called create method with null pointer.");

        PreparedStatement stmt = null;
        try {
            //AutoID
            if(profile.getId()== Integer.MIN_VALUE)
            {
                String sqlString = "INSERT INTO"
                        + " profiles (name,isPrintEnabled,isFilterEnabled,isGreenscreenEnabled,isMobilEnabled,watermark)"
                        + " VALUES (?,?,?,?,?,?);";


                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,profile.getName());
                stmt.setBoolean(2,profile.isPrintEnabled());
                stmt.setBoolean(3,profile.isFilerEnabled());
                stmt.setBoolean(4,profile.isGreenscreenEnabled());
                stmt.setBoolean(5,profile.isMobilEnabled());
                stmt.setString(6,profile.getWatermark());

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()){profile.setId(rs.getInt(1));}
                LOGGER.debug("Persisted Profile successfully with AutoID:" + profile.getId());
            }
            //NoAutoID
            else
                {
                String sqlString = "INSERT INTO"
                    + " profiles (profileID,name,isPrintEnabled,isFilterEnabled,isGreenscreenEnabled,isMobilEnabled,watermark)"
                    + " VALUES (?,?,?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,profile.getId());
                stmt.setString(2,profile.getName());
                stmt.setBoolean(3,profile.isPrintEnabled());
                stmt.setBoolean(4,profile.isFilerEnabled());
                stmt.setBoolean(5,profile.isGreenscreenEnabled());
                stmt.setBoolean(6,profile.isMobilEnabled());
                stmt.setString(7,profile.getWatermark());
                stmt.executeUpdate();
                LOGGER.debug("Persisted Profile successfully without AutoID:" + profile.getId());
                }

            List<Profile.PairCameraPosition> returnPairCameraPositionList = new ArrayList<>();
            for (Profile.PairCameraPosition pairCameraPosition : profile.getCameraPositions())
            {
                pairCameraPosition.setProfileId(profile.getId());
                returnPairCameraPositionList.add(pairCameraPositionDAO.create(pairCameraPosition));
            }

            List<Profile.PairLogoRelativeRectangle> returnPairLogoRelativeRectangleList = new ArrayList<>();
            for (Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : profile.getPairLogoRelativeRectangles())
            {
                pairLogoRelativeRectangle.setProfileId(profile.getId());
                returnPairLogoRelativeRectangleList.add(pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangle));
            }
            profile.setCameraPositions(returnPairCameraPositionList);
            profile.setPairLogoRelativeRectangles(returnPairLogoRelativeRectangleList);
            LOGGER.debug("Completed Profile creation persistence successfully " + profile.getId());
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating in persistence layer has failed.:" + e);
        }
        finally {
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        return profile;
    }

    @Override
    public boolean update(Profile profile) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + profile);
        if(profile==null)throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE profiles"
                + " SET name = ?, isPrintEnabled = ?, isFilterEnabled = ?, isGreenscreenEnabled = ?, isMobilEnabled = ?, watermark = ?"
                + " WHERE profileID = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,profile.getName());
            stmt.setBoolean(2,profile.isPrintEnabled());
            stmt.setBoolean(3,profile.isFilerEnabled());
            stmt.setBoolean(4,profile.isGreenscreenEnabled());
            stmt.setBoolean(5,profile.isMobilEnabled());
            stmt.setString(6,profile.getWatermark());
            stmt.setLong(7,profile.getId());
            stmt.executeUpdate();
            int returnUpdateCount = stmt.executeUpdate();

            // Check, if object has been updated and return suitable boolean value or throw Exception
            if (returnUpdateCount == 1){
                LOGGER.debug("Update has been successfully(return value true)");

                // updating pairCameraPositionList
                List<Profile.PairCameraPosition> newPairCameraPositions
                        = profile.getCameraPositions();
                List<Profile.PairCameraPosition> oldPairCameraPositions
                        = pairCameraPositionDAO.readAllWithProfileID(profile.getId());
                for (Profile.PairCameraPosition pairCameraPosition : newPairCameraPositions)
                    {
                        if(oldPairCameraPositions.contains(pairCameraPosition)){
                            pairCameraPositionDAO.update(pairCameraPosition);
                        }
                        else {
                            pairCameraPosition.setProfileId(profile.getId());
                            pairCameraPositionDAO.create(pairCameraPosition);
                        }
                    }
                for (Profile.PairCameraPosition pairCameraPosition : oldPairCameraPositions)
                    {
                        if(!newPairCameraPositions.contains(pairCameraPosition)){
                            pairCameraPositionDAO.delete(pairCameraPosition);
                        }
                    }

                // updating pairLogoRelativeRectangleList
                List<Profile.PairLogoRelativeRectangle> newPairLogoRelativeRectangles
                        = profile.getPairLogoRelativeRectangles();
                List<Profile.PairLogoRelativeRectangle> oldPairLogoRelativeRectangles
                        = pairLogoRelativeRectangleDAO.readAllWithProfileID(profile.getId());
                for (Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : newPairLogoRelativeRectangles)
                    {
                        if(oldPairLogoRelativeRectangles.contains(pairLogoRelativeRectangle)){
                            pairLogoRelativeRectangleDAO.update(pairLogoRelativeRectangle);
                        }
                        else{
                            pairLogoRelativeRectangle.setProfileId(profile.getId());
                            pairLogoRelativeRectangleDAO.create(pairLogoRelativeRectangle);
                        }
                    }
                for (Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : oldPairLogoRelativeRectangles)
                    {
                        if(!newPairLogoRelativeRectangles.contains(pairLogoRelativeRectangle)){
                            pairLogoRelativeRectangleDAO.delete(pairLogoRelativeRectangle);
                        }
                    }
                return true;
            }
            else if (returnUpdateCount == 0) {
                LOGGER.debug("Updated nothing , since it doesn't exist in persistence data store(return value false)");
                return false;}
            else {
                throw new PersistenceException("Error! Updating in persistence layer has failed.:Consistence of persistence store is broken! This should not happen!");
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error! Updating in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of update method call has failed.:" + e);}
        }
    }

    @Override
    public Profile read(int id) throws PersistenceException{
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM profiles WHERE profileID = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Profile profile = new Profile(
                        rs.getInt("profileID"),
                        rs.getString("name"),
                        pairCameraPositionDAO.readAllWithProfileID(id),
                        pairLogoRelativeRectangleDAO.readAllWithProfileID(id),
                        rs.getBoolean("isPrintEnabled"),
                        rs.getBoolean("isFilterEnabled"),
                        rs.getBoolean("isGreenscreenEnabled"),
                        rs.getBoolean("isMobilEnabled"),
                        rs.getString("watermark"),
                        rs.getBoolean("isDeleted")
            );
            return profile;
            }
            else {
                LOGGER.debug("Read nothing, since it doesn't exist in persistence data store(return null)");
                return null;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Reading in persistence layer has failed.:" + e);
        }
        finally {
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of read method call has failed.:" + e);}
        }
    }

    @Override
    public List<Profile> readAll() throws PersistenceException{
        LOGGER.debug("Entering readAll method");
        String sqlString = "SELECT * FROM profiles where isDeleted ='false';";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            ResultSet rs = stmt.executeQuery();
            List<Profile> returnList = new ArrayList<>();

            while (rs.next()) {
                Profile profile = this.read(rs.getInt("profileID"));
                returnList.add(profile);
            }
            LOGGER.debug("Persisted object readingAll has been successfully. " + returnList);
            return returnList;
        } catch (SQLException e) {
            throw new PersistenceException("Error! ReadAll objects in persistence layer has failed.:" + e);
        }
        finally {
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of readAll method call has failed.:" + e);}
        }
    }

    @Override
    public boolean delete(Profile profile) throws PersistenceException{
        LOGGER.debug("Entering delete method with parameters " + profile);

        if (profile==null) throw new IllegalArgumentException("Error!:Called delete method with null pointer.");

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE profiles SET isDeleted = 'true' WHERE profileID = ? AND isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,profile.getId());
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
            throw new PersistenceException("Error! Deleting in persistence layer has failed.:" + e);
        }
        finally {
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);}
        }
    }
}
