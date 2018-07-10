package org.openbooth.dao.impl;

import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.LogoDAO;
import org.openbooth.dao.PairLogoRelativeRectangleDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Profile;
import org.openbooth.entities.RelativeRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 database-Specific PairLogoRelativeRectangleDAO Implementation
 */
@Repository
public class JDBCPairLogoRelativeRectangleDAO implements PairLogoRelativeRectangleDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCPairLogoRelativeRectangleDAO.class);
    private Connection con;
    private LogoDAO logoDAO;

    public static final String TABLE_NAME = "profile_logo_relativeRectangles";
    public static final String ID_COLUMN = "profile_logo_relativeRectangles_id";
    public static final String PROFILE_ID_COLUMN = "profileId";
    public static final String LOGO_ID_COLUMN = "logoId";
    public static final String X_COLUMN = "x";
    public static final String Y_COLUMN = "y";
    public static final String WIDTH_COLUMN = "width";
    public static final String HEIGHT_COLUMN = "height";

    @Autowired
    public JDBCPairLogoRelativeRectangleDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            LOGGER.error("Constructor - ",e);
            throw new PersistenceException(e);
        }
        logoDAO = new JDBCLogoDAO(handler);
    }

    @Override
    public Profile.PairLogoRelativeRectangle create(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        LOGGER.debug("Entering create method with parameter " + pairLogoRelativeRectangle);

        if (pairLogoRelativeRectangle==null)
            throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        try {
            //AutoID
            if(pairLogoRelativeRectangle.getId()==Integer.MIN_VALUE) {
                sqlString = "INSERT INTO profile_logo_relativeRectangles(profileId,logoId,x,y,width,height) VALUES (?,?,?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, pairLogoRelativeRectangle.getProfileId());
                stmt.setInt(2, pairLogoRelativeRectangle.getLogo().getId());
                stmt.setDouble(3, pairLogoRelativeRectangle.getRelativeRectangle().getX());
                stmt.setDouble(4, pairLogoRelativeRectangle.getRelativeRectangle().getY());
                stmt.setDouble(5, pairLogoRelativeRectangle.getRelativeRectangle().getWidth());
                stmt.setDouble(6, pairLogoRelativeRectangle.getRelativeRectangle().getHeight());
                stmt.executeUpdate();
                //Get autoassigned id
                rs = stmt.getGeneratedKeys();
                if (rs.next())
                    {pairLogoRelativeRectangle.setId(rs.getInt(1));}
                LOGGER.debug("Create successfully with AutoID:" + pairLogoRelativeRectangle.getId());
            }
            //NoAutoID
            else {
                sqlString = "INSERT INTO profile_logo_relativeRectangles(profile_logo_relativeRectangles_id,profileId,logoId,x,y,width,height) VALUES (?,?,?,?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1, pairLogoRelativeRectangle.getId());
                stmt.setInt(2, pairLogoRelativeRectangle.getProfileId());
                stmt.setInt(3, pairLogoRelativeRectangle.getLogo().getId());
                stmt.setDouble(4, pairLogoRelativeRectangle.getRelativeRectangle().getX());
                stmt.setDouble(5, pairLogoRelativeRectangle.getRelativeRectangle().getY());
                stmt.setDouble(6, pairLogoRelativeRectangle.getRelativeRectangle().getWidth());
                stmt.setDouble(7, pairLogoRelativeRectangle.getRelativeRectangle().getHeight());
                stmt.executeUpdate();
                LOGGER.debug("Create successfully without AutoID:" + pairLogoRelativeRectangle.getId());
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
        return pairLogoRelativeRectangle;
    }

    @Override
    public List<Profile.PairLogoRelativeRectangle> createAll(List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles) throws PersistenceException {
        LOGGER.debug("Entering createAll method");
        List<Profile.PairLogoRelativeRectangle> returnValue = new ArrayList<>();
        for (Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : pairLogoRelativeRectangles) {
            try {
                returnValue.add(this.create(pairLogoRelativeRectangle));
            }
            catch (PersistenceException e) {
                throw new PersistenceException("Error! CreateAll objects in persistence layer has failed.:" + e);
                }
            }
        LOGGER.debug("Persisted createAll method has completed successfully " + pairLogoRelativeRectangles);
        return returnValue;
    }

    @Override
    public boolean update(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + pairLogoRelativeRectangle);
        if(pairLogoRelativeRectangle==null)
            throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE profile_logo_relativeRectangles SET profileId = ?,logoId = ?,x = ?,y = ?,width = ?,height = ? WHERE profile_logo_relativeRectangles_id = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,pairLogoRelativeRectangle.getProfileId());
            stmt.setInt(2,pairLogoRelativeRectangle.getLogo().getId());
            stmt.setDouble(3,pairLogoRelativeRectangle.getRelativeRectangle().getX());
            stmt.setDouble(4,pairLogoRelativeRectangle.getRelativeRectangle().getY());
            stmt.setDouble(5,pairLogoRelativeRectangle.getRelativeRectangle().getWidth());
            stmt.setDouble(6,pairLogoRelativeRectangle.getRelativeRectangle().getHeight());
            stmt.setInt(7,pairLogoRelativeRectangle.getId());
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
    public Profile.PairLogoRelativeRectangle read(int id) throws PersistenceException{
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM profile_logo_relativeRectangles WHERE profile_logo_relativeRectangles_id = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle
                        = new Profile.PairLogoRelativeRectangle(
                                rs.getInt("profile_logo_relativeRectangles_id"),
                                rs.getInt("profileId"),
                                logoDAO.read(rs.getInt("logoId")),
                                new RelativeRectangle(
                                        rs.getDouble("x"),
                                        rs.getDouble("y"),
                                        rs.getDouble("width"),
                                        rs.getDouble("height"))
                );
                LOGGER.debug("Read has been successfully. " + pairLogoRelativeRectangle);
                return pairLogoRelativeRectangle;
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
    public List<Profile.PairLogoRelativeRectangle> readAllWithProfileID(int profileId) throws PersistenceException {
        LOGGER.debug("Entering readAll method");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        sqlString = "SELECT * FROM profile_logo_relativeRectangles where profileId = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,profileId);
            rs = stmt.executeQuery();
            List<Profile.PairLogoRelativeRectangle> returnList = new ArrayList<>();

            while (rs.next()) {
                Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle =
                        this.read(rs.getInt("profile_logo_relativeRectangles_id"));
                returnList.add(pairLogoRelativeRectangle);
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
    public boolean delete(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        LOGGER.debug("Entering delete method with parameters " + pairLogoRelativeRectangle);

        if (pairLogoRelativeRectangle==null)
            throw new IllegalArgumentException("Error!:Called delete method with null pointer.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "DELETE FROM profile_logo_relativeRectangles"
                + " WHERE profile_logo_relativeRectangles_id = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, pairLogoRelativeRectangle.getId());
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

        sqlString = "DELETE FROM profile_logo_relativeRectangles"
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
                LOGGER.debug("Provided object(s) has/have been not deleted, since it/they do(es)n't exist in persistence data store(returned value false)");
                return false;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Deleting object(s) in persistence layer has failed.:" + e);
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