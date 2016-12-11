package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.PositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;

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
    private final static Logger LOGGER = LoggerFactory.getLogger(JDBCProfileDAO.class);
    private Connection con;

    @Autowired
    public JDBCPositionDAO(DBHandler handler) throws PersistenceException {
        LOGGER.debug("Entering constructor");
        con = handler.getConnection();
    }

    @Override
    public Position create(Position position) throws PersistenceException {
        LOGGER.debug("Entering create method with parameters " + position);

        if(position==null) throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        PreparedStatement stmt = null;
        ResultSet rs;
        String sqlString;

        try {
            //AutoID
            if(position.getId()== Long.MIN_VALUE)
            {
                sqlString = "INSERT INTO positions(name,buttonImagePath) VALUES (?,?);";
                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,position.getName());
                stmt.setString(2,position.getButtonImagePath());
                stmt.executeUpdate();
                //Get autoassigned id
                rs = stmt.getGeneratedKeys();
                if (rs.next()){position.setId(rs.getInt(1));}
                LOGGER.debug("Persisted object creation successfully with AutoID:" + position.getId());
            }
            //NoAutoID
            else
            {
                sqlString = "INSERT INTO positions(positionID,name,isDeleted) VALUES (?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,position.getId());
                stmt.setString(2,position.getName());
                stmt.setBoolean(3,position.isDeleted());
                stmt.executeUpdate();
                LOGGER.debug("Persisted objected creation successfully without AutoID:" + position.getId());
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating object in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        // Return persisted object
        return position;
    }

    @Override
    public boolean update(Position position) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + position);
        if(position==null)throw new IllegalArgumentException("Error! Called update method with null pointer.");
        LOGGER.debug("Passed:Checking parameters according to specification.");

        ResultSet rs;
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
            rs = stmt.getResultSet();
            // Check, if object has been updated and return suitable boolean value
            if (rs.next()){
                LOGGER.debug("Persisted object update has been successfully(return value true)");
                return true;
                }
            else {
                LOGGER.debug("Provided object has been not updated, since it doesn't exist in persistence data store(return value false)");
                return false;}
        } catch (SQLException e) {
            throw new PersistenceException("Error! Updating object in persistence layer has failed.:" + e);
        }
        finally {
            // Return resources
            try {if (stmt != null) stmt.close();}
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

        sqlString = "SELECT * FROM positions WHERE positionID = ?;";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                Position position = new Position(rs.getInt("positionID"), rs.getString("name"), rs.getString("buttonImagePath"), rs.getBoolean("isDeleted"));
                LOGGER.debug("Persisted object reading has been successfully. " + position);
                return position;
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
    public boolean delete(Position position) throws PersistenceException{
        LOGGER.debug("Entering delete method with parameters " + position);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "UPDATE positions SET isDeleted = 'true' WHERE positionID = ? AND isDeleted = 'false';";

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,position.getId());
            stmt.executeUpdate();
            rs = stmt.getResultSet();
            // Check, if object has been updated and return suitable boolean value
            if (rs.next()){
                LOGGER.debug("Persisted object deletion has been successfully(returned value true)");
                return true;
            }
            else {
                LOGGER.debug("Provided object has been not deleted, since it doesn't exist in persistence data store(returned value false)");
                return false;
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
