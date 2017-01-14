package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.util.exceptions.DatabaseException;
import at.ac.tuwien.sepm.ws16.qse01.dao.BackgroundCategoryDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.BackgroundDAO;
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
 * BackgroundDAO
 */
@Repository
public class JDBCBackgroundDAO implements BackgroundDAO{
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCBackgroundDAO.class);
    private Connection con;
    private BackgroundCategoryDAO backgroundCategoryDAO;

    @Autowired
    public JDBCBackgroundDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            LOGGER.error("Constructor - ",e);
            throw new PersistenceException(e);
        }
        backgroundCategoryDAO = new JDBCBackgroundCategoryDAO(handler);
    }

    @Override
    public Background create(Background background) throws PersistenceException {
        LOGGER.debug("Entering create method with parameters " + background);
        if(background==null)
            throw new IllegalArgumentException("Error!:Called create method with null pointer.");

        PreparedStatement stmt = null;

        try {
            //AutoID
            if(background.getId()== Integer.MIN_VALUE)
            {
                String sqlString = "INSERT INTO"
                        + " backgrounds (name,path,backgroundcategoryID)"
                        + " VALUES (?,?,?);";

                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,background.getName());
                stmt.setString(2,background.getPath());
                stmt.setInt(3,background.getCategory().getId());

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {background.setId(rs.getInt(1));}
                LOGGER.debug("Persisted Profile successfully with AutoID:" + background.getId());
            }
            //NoAutoID
            else
            {
                String sqlString = "INSERT INTO"
                        + " backgrounds (backgroundID,name,path,backgroundcategoryID)"
                        + " VALUES (?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,background.getId());
                stmt.setString(2,background.getName());
                stmt.setString(3,background.getPath());
                stmt.setInt(4,background.getCategory().getId());

                stmt.executeUpdate();
                LOGGER.debug("Persisted object creation successfully without AutoID:" + background);
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating in persistence layer has failed.:" + e);
        }
        finally {
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        return background;
    }

    @Override
    public boolean update(Background background) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + background);
        if(background==null)
            throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE backgrounds"
                + " SET name = ?, path = ?, backgroundcategoryID = ?"
                + " WHERE backgroundID = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,background.getName());
            stmt.setString(2,background.getPath());
            stmt.setInt(3,background.getCategory().getId());
            stmt.setInt(4,background.getId());
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
        } catch (SQLException e) {
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
    public Background read(int id) throws PersistenceException {
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM backgrounds WHERE backgroundID = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Background background = new Background(
                        rs.getInt("backgroundID"),
                        rs.getString("name"),
                        rs.getString("path"),
                        backgroundCategoryDAO.read(id),
                        rs.getBoolean("isDeleted")
                );
                if(background.getPath() == null)
                    {background.setPath("");}
                return background;
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
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of read method call has failed.:" + e);}
        }
    }

    @Override
    public List<Background> readAll() throws PersistenceException {
        LOGGER.debug("Entering readAll method");
        String sqlString = "SELECT * FROM backgrounds where isDeleted ='false';";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            ResultSet rs = stmt.executeQuery();
            List<Background> returnList = new ArrayList<>();

            while (rs.next()) {
                Background background = this.read(rs.getInt("backgroundID"));
                returnList.add(background);
            }
            LOGGER.debug("Persisted object readingAll has been successfully. " + returnList);
            return returnList;
        } catch (SQLException e) {
            throw new PersistenceException("Error! ReadAll objects in persistence layer has failed.:" + e);
        }
        finally {
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of readAll method call has failed.:" + e);}
        }
    }

    @Override
    public List<Background> readAllWithCategory(int id) throws PersistenceException {
        LOGGER.debug("Entering readAllWithCategory method");
        String sqlString = "SELECT * FROM backgrounds where backgroundcategoryID = ? AND isDeleted ='false';";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            List<Background> returnList = new ArrayList<>();

            while (rs.next()) {
                Background background = this.read(rs.getInt("backgroundID"));
                returnList.add(background);
            }
            LOGGER.debug("Persisted object readAllWithCategory has been successfully. " + returnList);
            return returnList;
        } catch (SQLException e) {
            throw new PersistenceException("Error! readAllWithCategory objects in persistence layer has failed.:" + e);
        }
        finally {
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of readAllWithCategory method call has failed.:" + e);}
        }
    }

    @Override
    public boolean delete(Background background) throws PersistenceException {
        LOGGER.debug("Entering delete method with parameters " + background);

        if (background==null)
            throw new IllegalArgumentException("Error!:Called delete method with null pointer.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE backgrounds SET isDeleted = 'true' WHERE backgroundID = ? AND isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,background.getId());
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
            try
            {if (stmt != null)
                stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of deleting method call has failed.:" + e);}
        }
    }
}
