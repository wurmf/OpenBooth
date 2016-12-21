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
            //pairCameraPositionDAO.createAll(profile);
            //pairLogoRelativeRectangleDAO.createAll(profile);
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
        String sqlString = "UPDATE profiles"
                + " SET name = ?, isPrintEnabled = ?, isFilterEnabled = ?, isGreenscreenEnabled = ?, isMobilEnabled = ?, watermark = ?"
                + " WHERE profileID = ?;";

        PreparedStatement stmt = null;

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
            ResultSet rs = stmt.getResultSet();

            if (rs.next()){ //TODO: Sobald ein watermark upgedated wird, bekommt man hier ein NULLPOINTEREXCEPTION
                //pairCameraPositionDAO.deleteAll(profile);
                //pairLogoRelativeRectangleDAO.deleteAll(profile);
                //pairCameraPositionDAO.createAll(profile);
                //pairLogoRelativeRectangleDAO.createAll(profile);
                return true;
            }
            else {return false;}
        } catch (SQLException e) {
            throw new PersistenceException("Error! Updating in persistence layer has failed.:" + e);
        }
        finally {
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of update method call has failed.:" + e);}
        }
    }

    @Override
    public Profile read(int id) throws PersistenceException{
        LOGGER.debug("Entering read method with parameter profileid=" + id);
        String sqlString = "SELECT profileID,name,isPrintEnabled,isFilterEnabled,isGreenscreenEnabled,isDeleted,watermark,isMobilEnabled"
                + " FROM profiles WHERE profileID = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()) {
                return null;
                /*new PersistenceException("No data existing to read: Profile with " + id + " isn't persisted yet!");*/
            }
            Profile p= new Profile(
                    rs.getInt("profileID"),
                    rs.getString("name"),
                    pairCameraPositionDAO.readAll(id),
                    pairLogoRelativeRectangleDAO.readAllWithProfileID(id),
                    rs.getBoolean("isPrintEnabled"),
                    rs.getBoolean("isFilterEnabled"),
                    rs.getBoolean("isGreenscreenEnabled"),
                    rs.getBoolean(("isMobilEnabled")),
                    rs.getBoolean("isDeleted")
                    );
            p.setWatermark(rs.getString("watermark"));
            return p;
        } catch (SQLException e) {
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
            List<Profile> pList = new ArrayList<>();

            while (rs.next()) {
                Profile profile = this.read(rs.getInt("profileID"));
                pList.add(profile);
            }
            return pList;
        } catch (SQLException e) {
            throw new PersistenceException("Error! Reading all profiles in persistence layer has failed.:" + e);
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
        String sqlString = "UPDATE profiles SET isDeleted = 'true' WHERE profileID = ? AND isDeleted = 'false';";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,profile.getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getResultSet();
            if (rs.next()){
                pairCameraPositionDAO.deleteAll(profile);
                //pairLogoRelativeRectangleDAO.deleteAll(profile);
                return true;}
            else {return false;}
        } catch (SQLException e) {
            throw new PersistenceException("Error! Deleting in persistence layer has failed.:" + e);
        }
        finally {
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);}
        }
    }
}
