package org.openbooth.dao.impl;

import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.PositionDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 database-Specific PositionDAO Implementation
 */
@Repository
public class JDBCPositionDAO implements PositionDAO{
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCPositionDAO.class);
    private Connection con;

    @Autowired
    public JDBCPositionDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            LOGGER.error("Constructor - ",e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public Position create(Position position) throws PersistenceException {
        LOGGER.debug("Entering create method with parameters " + position);

        if(position==null)
            throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        try {
            //AutoID
            if(position.getId()== Integer.MIN_VALUE)
            {
                sqlString = "INSERT INTO positions(name,buttonImagePath) VALUES (?,?);";
                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,position.getName());
                stmt.setString(2,position.getButtonImagePath());
                stmt.executeUpdate();
                //Get autoassigned id
                rs = stmt.getGeneratedKeys();
                if (rs.next())
                    {position.setId(rs.getInt(1));}
                LOGGER.debug("Create successfully with AutoID:" + position.getId());
            }
            //NoAutoID
            else
            {
                sqlString = "INSERT INTO positions(positionID,name,buttonImagePath,isDeleted) VALUES (?,?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,position.getId());
                stmt.setString(2,position.getName());
                stmt.setString(3,position.getButtonImagePath());
                stmt.setBoolean(4,position.isDeleted());
                stmt.executeUpdate();
                LOGGER.debug("Create successfully without AutoID:" + position.getId());
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating object in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try
                {if (stmt != null)
                    stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        // Return persisted object
        return position;
    }

    @Override
    public boolean update(Position position) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + position);
        if(position==null)
            throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE positions SET name = ?,buttonImagePath = ?, isDeleted = ? WHERE positionID = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,position.getName());
            stmt.setString(2,position.getButtonImagePath());
            stmt.setBoolean(3,position.isDeleted());
            stmt.setInt(4,position.getId());
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
    public Position read(int id) throws PersistenceException{
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM positions WHERE positionID = ? AND isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                Position position = new Position(rs.getInt("positionID"), rs.getString("name"), rs.getString("buttonImagePath"), rs.getBoolean("isDeleted"));
                LOGGER.debug("Read has been successfully. " + position);
                return position;
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
    public List<Position> readAll() throws PersistenceException{
        LOGGER.debug("Entering readAll method");
        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM positions where isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            rs = stmt.executeQuery();
            List<Position> returnList = new ArrayList<>();

            while (rs.next()) {
                Position position = this.read(rs.getInt("positionID"));
                returnList.add(position);
            }
            LOGGER.debug("ReadAll has been successfully. " + returnList);
            return returnList;
        } catch (SQLException e) {
            throw new PersistenceException("Error! ReadAll in persistence layer has failed.:" + e);
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
    public boolean delete(Position position) throws PersistenceException{
        LOGGER.debug("Entering delete method with parameters " + position);
        if (position==null)
            throw new IllegalArgumentException("Error!:Called delete method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE positions SET isDeleted = 'true' WHERE positionID = ? AND isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,position.getId());
            int returnUpdateCount  = stmt.executeUpdate();

            // Check, if object has been updated and return suitable boolean value
            if (returnUpdateCount == 1){
                LOGGER.debug("Delete has been successfully(returned value true)");
                return true;
            }
            else if (returnUpdateCount == 0){
                LOGGER.debug("Deleted nothing, since it didn't exist in persistence data store(returned value false)");
                return false;
            }
            else {
                throw new PersistenceException("Error! Deleting in persistence layer has failed.:Consistence of persistence store is broken!This should not happen!");
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Deleting in persistence layer has failed.:" + e);
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
}
