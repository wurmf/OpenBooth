package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.util.exceptions.DatabaseException;
import at.ac.tuwien.sepm.ws16.qse01.dao.BackgroundCategoryDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 database-Specific BackgroundCategoryDAO Implementation
 */
@Repository
public class JDBCBackgroundCategoryDAO implements BackgroundCategoryDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCLogoDAO.class);
    private Connection con;

    @Autowired
    public JDBCBackgroundCategoryDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            LOGGER.error("Constructor - ",e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public Background.Category create(Background.Category backgroundCategory) throws PersistenceException {
        LOGGER.debug("Entering create method with parameter " + backgroundCategory);

        if (backgroundCategory==null)
            throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        try {
            //AutoID
            if(backgroundCategory.getId()==Integer.MIN_VALUE)
            {
                sqlString = "INSERT INTO backgroundcategories(name) VALUES (?);";
                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,backgroundCategory.getName());
                stmt.executeUpdate();
                //Get autoassigned id
                rs = stmt.getGeneratedKeys();
                if (rs.next())
                {backgroundCategory.setId(rs.getInt(1));}
                LOGGER.debug("Persisted object creation successfully with AutoID:" + backgroundCategory.getId());
            }
            //No AutoID
            else {
                sqlString = "INSERT INTO backgroundcategories(backgroundcategoryID,name,isDeleted) VALUES (?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,backgroundCategory.getId());
                stmt.setString(2,backgroundCategory.getName());
                stmt.setBoolean(3,backgroundCategory.isDeleted());
                stmt.executeUpdate();
                LOGGER.debug("Persisted object creation successfully without AutoID:" + backgroundCategory.getId());
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating object in persistence layer has failed.:" + e);
        }
        finally{
            // Return resources
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        // Return persisted object
        return backgroundCategory;
    }

    @Override
    public boolean update(Background.Category backgroundCategory) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + backgroundCategory);
        if(backgroundCategory==null)
            throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE backgroundcategories SET name = ? WHERE backgroundcategoryID = ?;";
        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,backgroundCategory.getName());
            stmt.setInt(2,backgroundCategory.getId());
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
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of update method call has failed.:" + e);}
        }
    }

    @Override
    public Background.Category read(int id) throws PersistenceException {
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM backgroundcategories WHERE backgroundcategoryID = ?;";
        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                Background.Category backgroundcategory
                        = new Background.Category(
                                rs.getInt("backgroundcategoryID"),
                                rs.getString("name"),
                                rs.getBoolean("isDeleted"));
                LOGGER.debug("Persisted object reading has been successfully. " + backgroundcategory);
                return backgroundcategory;
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
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of read method call has failed.:" + e);}
        }
    }

    @Override
    public List<Background.Category> readAll() throws PersistenceException {
        LOGGER.debug("Entering readAll method");
        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM backgroundcategories where isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            rs = stmt.executeQuery();
            List<Background.Category> returnList = new ArrayList<>();

            while (rs.next()) {
                Background.Category backgroundcategory = this.read(rs.getInt("backgroundcategoryID"));
                returnList.add(backgroundcategory);
            }
            LOGGER.debug("Persisted object readingAll has been successfully. " + returnList);
            return returnList;
        } catch (SQLException e) {
            throw new PersistenceException("Error! Reading all objects in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of readAll method call has failed.:" + e);}
        }
    }

    @Override
    public boolean delete(Background.Category backgroundCategory) throws PersistenceException {
        LOGGER.debug("Entering delete method with parameters " + backgroundCategory);
        if (backgroundCategory==null)
            throw new IllegalArgumentException("Error!:Called delete method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");
        PreparedStatement stmt = null;
        String sqlString;
        sqlString = "UPDATE backgroundcategories SET isDeleted = 'true' WHERE backgroundcategoryID = ? AND isDeleted = 'false' ;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,backgroundCategory.getId());
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
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);}
        }
    }
}
