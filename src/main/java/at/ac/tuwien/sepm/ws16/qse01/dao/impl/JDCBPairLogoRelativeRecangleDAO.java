package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.PairLogoRelativeRectangleDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.entities.RelativeRectangle;
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
 * H2 database-Specific PairLogoRelativeRectangleDAO Implementation
 */
@Repository
public class JDCBPairLogoRelativeRecangleDAO implements PairLogoRelativeRectangleDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(JDCBPairCameraPositionDAO.class);
    private Connection con;

    @Autowired
    public JDCBPairLogoRelativeRecangleDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        con = handler.getConnection();
    }

    @Override
    public Profile.PairLogoRelativeRectangle create(int profileId,Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        LOGGER.debug("Entering create methode with parameter " + pairLogoRelativeRectangle);

        if (pairLogoRelativeRectangle==null) throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        sqlString = "INSERT INTO profile_logo_rpositions(profileId,logoId,x1,y1,x2,y2) VALUES (?,?,?,?,?,?);";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,profileId);
            stmt.setInt(2,pairLogoRelativeRectangle.getLogo().getId());
            stmt.setDouble(3,pairLogoRelativeRectangle.getRelativeRectangle().getX());
            stmt.setDouble(4,pairLogoRelativeRectangle.getRelativeRectangle().getY());
            stmt.setDouble(5,pairLogoRelativeRectangle.getRelativeRectangle().getWidth());
            stmt.setDouble(6,pairLogoRelativeRectangle.getRelativeRectangle().getHeight());
            stmt.executeUpdate();
            LOGGER.debug("Persisted object creation successfully");
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating pairLogoRelativeRectangle object in persistence layer has failed.:" + e);
        }
        finally{
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        // Return persisted object
        return pairLogoRelativeRectangle;
    }

    @Override
    public List<Profile.PairLogoRelativeRectangle> createAll(Profile profile) throws PersistenceException {
        LOGGER.debug("Entering creatAll method");
        List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles = new ArrayList<>();
        for (Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : profile.getPairLogoRelativeRectangles()) {
            pairLogoRelativeRectangles.add(this.create(profile.getId(), pairLogoRelativeRectangle));
        }
        LOGGER.debug("Persisted createAll method has completed successfully " + pairLogoRelativeRectangles);
        return  pairLogoRelativeRectangles;
    }

    @Override
    public List<Profile.PairLogoRelativeRectangle> readAll(int profileId) throws PersistenceException {
        LOGGER.debug("Entering readAll method");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        sqlString = "SELECT * FROM profile_logo_rpositions where profileId = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,profileId);
            rs = stmt.executeQuery();
            List<Profile.PairLogoRelativeRectangle> returnList = new ArrayList<>();

            while (rs.next()) {
                Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle =
                        new Profile.PairLogoRelativeRectangle(
                                (new JDBCLogoDAO(H2Handler.getInstance()).read(rs.getInt("logoId"))),
                                new RelativeRectangle(
                                        rs.getDouble("x1"),
                                        rs.getDouble("y1"),
                                        rs.getDouble("y2"),
                                        rs.getDouble("x2"))
                                );
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
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of readAll method call has failed.:" + e);}
        }
    }

    @Override
    public boolean delete(int profileId,Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws PersistenceException {
        LOGGER.debug("Entering delete method with parameters " + pairLogoRelativeRectangle);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "DELETE FROM profile_logo_rpositions"
                + " WHERE profileId = ? AND x1 = ? AND y1 = ? AND x2 = ? AND y2 = 2;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setLong(1, profileId);
            stmt.setDouble(2,pairLogoRelativeRectangle.getRelativeRectangle().getX());
            stmt.setDouble(3,pairLogoRelativeRectangle.getRelativeRectangle().getY());
            stmt.setDouble(4,pairLogoRelativeRectangle.getRelativeRectangle().getWidth());
            stmt.setDouble(5,pairLogoRelativeRectangle.getRelativeRectangle().getHeight());
            stmt.executeUpdate();
            rs = stmt.getResultSet();
            // Check, if object has been updated and return suitable boolean value
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

        sqlString = "DELETE FROM profile_logo_rpositions"
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