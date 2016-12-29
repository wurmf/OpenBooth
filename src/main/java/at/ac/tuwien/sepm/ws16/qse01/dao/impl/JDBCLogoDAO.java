package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.LogoDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 database-Specific LogoDAO Implementation
 */
@Repository
public class JDBCLogoDAO implements LogoDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(JDBCLogoDAO.class);
    private Connection con;

    @Autowired
    public JDBCLogoDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        con = handler.getConnection();
    }

    @Override
    public Logo create(Logo logo) throws PersistenceException {
        LOGGER.debug("Entering create methode with parameter " + logo);

        if (logo==null) throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        try {
            //AutoID
            if(logo.getId()==Integer.MIN_VALUE)
            {
                sqlString = "INSERT INTO logos(label,path) VALUES (?,?);";
                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,logo.getLabel());
                stmt.setString(2,logo.getPath());
                stmt.executeUpdate();
                //Get autoassigned id
                rs = stmt.getGeneratedKeys();
                if (rs.next()){logo.setId(rs.getInt(1));}
                LOGGER.debug("Persisted object creation successfully with AutoID:" + logo.getId());
            }
            //No AutoID
            else {
                sqlString = "INSERT INTO logos(logoID,label, path,isDeleted) VALUES (?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,logo.getId());
                stmt.setString(2,logo.getLabel());
                stmt.setString(3,logo.getPath());
                stmt.setBoolean(4,logo.isDeleted());
                stmt.executeUpdate();
                LOGGER.debug("Persisted object creation successfully without AutoID:" + logo.getId());
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating logo object in persistence layer has failed.:" + e);
        }
        finally{
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        // Return persisted object
        return logo;
    }

    @Override
    public boolean update(Logo logo) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + logo);
        if(logo==null)throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE logos SET label = ?, path = ?, isDeleted = ? WHERE logoId = ?;";
        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,logo.getLabel());
            stmt.setString(2,logo.getPath());
            stmt.setBoolean(3,logo.isDeleted());
            stmt.setInt(4,logo.getId());
            int returnUpdateCount = stmt.executeUpdate();

            // Check, if object has been updated and return suitable boolean value or throw Exception
            if (returnUpdateCount == 1){
                LOGGER.debug("Persisted object update has been successfully(return value true)");
                return true;
                }
            else if (returnUpdateCount == 0){
                LOGGER.debug("Provided object has been not updated, since it doesn't exist in persistence data store(return value false)");
                return false;}
            else {
                throw new PersistenceException("Error! Updating in persistence layer has failed.:Consistence of persistence store is brocken!This should not happend!");
            }
        }
        catch (SQLException e) {
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
    public Logo read(int id) throws PersistenceException {
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM logos WHERE logoID = ? AND isDeleted = 'false';";
        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                Logo logo = new Logo(rs.getInt("logoID"),rs.getString("label"),rs.getString("path"),rs.getBoolean("isDeleted"));
                if (logo.getPath() == null){logo.setPath("");}
                LOGGER.debug("Persisted object reading has been successfully. " + logo);
                return logo;
            }
            else {
                LOGGER.debug("Persisted object reading has been not successfully, since it doesn't exist in persistence data store(return null)");
                return null;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Reading object in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of read method call has failed.:" + e);}
        }
    }

    @Override
    public List<Logo> readAll() throws PersistenceException {
        LOGGER.debug("Entering readAll method");
        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM logos where isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            rs = stmt.executeQuery();
            List<Logo> returnList = new ArrayList<>();

            while (rs.next()) {
                Logo logo = this.read(rs.getInt("logoID"));
                returnList.add(logo);
            }
            LOGGER.debug("Persisted object readingAll has been successfully. " + returnList);
            return returnList;
        } catch (SQLException e) {
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
    public boolean delete(Logo logo) throws PersistenceException {
        LOGGER.debug("Entering delete method with parameters " + logo);
        if (logo==null) throw new IllegalArgumentException("Error!:Called delete method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");
        PreparedStatement stmt = null;
        String sqlString;
        sqlString = "UPDATE logos SET isDeleted = 'true' WHERE logoID = ? AND isDeleted = 'false' ;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,logo.getId());
            int returnUpdateCount  = stmt.executeUpdate();

            // Check, if object has been updated and return suitable boolean value
            if (returnUpdateCount == 1){
                LOGGER.debug("Persisted object deletion has been successfully(returned value true)");
                return true;
            }
            else if (returnUpdateCount == 0){
                LOGGER.debug("Provided object has been not deleted, since it doesn't exist in persistence data store(returned value false)");
                return false;
            }
            else {
                throw new PersistenceException("Error! Deleting in persistence layer has failed.:Consistence of persistence store is brocken!This should not happend!");
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error! Deleting object in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);}
        }
    }
}
