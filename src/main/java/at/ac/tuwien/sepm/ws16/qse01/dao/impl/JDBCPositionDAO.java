package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.PositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public Position create(Position position) throws PersistenceException {
        LOGGER.debug("Entering create method with parameters " + position);
        if(position==null)throw new IllegalArgumentException("Error!:Called create method with null pointer.");

        PreparedStatement stmt = null;
        try {
            //AutoID
            if(position.getId()== Integer.MIN_VALUE)
            {
                String sqlString = "INSERT INTO positions(name) VALUES (?);";
                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,position.getName());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()){position.setId(rs.getInt(1));}
                LOGGER.debug("Persisted Position successfully with AutoID:" + position.getId());
            }
            //NoAutoID
            else
            {
                String sqlString = "INSERT INTO positions(positionID,name,isDeleted) VALUES (?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setInt(1,position.getId());
                stmt.setString(2,position.getName());
                stmt.setBoolean(3,position.isDeleted());
                stmt.executeUpdate();
                LOGGER.debug("Persisted Position successfully without AutoID:" + position.getId());
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Creating in persistence layer has failed.:" + e);
        }
        finally {
            try {if (stmt != null) stmt.close();}
            catch (SQLException e) {
                throw new PersistenceException("Error! Closing resource at end of creating method call has failed.:" + e);}
        }
        return position;
    }

    @Override
    public boolean update(Position position) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + position);
        if(position==null)throw new IllegalArgumentException("Error! Called update method with null pointer.");
        String sqlString = "UPDATE positions SET name = ?,isDeleted = ? WHERE positionID = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,position.getName());
            stmt.setBoolean(2,position.isDeleted());
            stmt.setInt(3,position.getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()){return true;} else {return false;}
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
    public Position read(int id) throws PersistenceException{
        LOGGER.debug("Entering read method with parameter id=" + id);
        String sqlString = "SELECT * FROM positions WHERE postionID = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()) {
                return null;
            }
            return new Position(rs.getInt("positionID"),rs.getString("name"),rs.getBoolean("isDeleted"));
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
    public List<Position> readAll() throws PersistenceException{
        LOGGER.debug("Entering readAll method");
        String sqlString = "SELECT * FROM positions where isDeleted = 'false';";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            ResultSet rs = stmt.executeQuery();
            List<Position> pList = new ArrayList<>();

            while (rs.next()) {
                Position position = new Position(rs.getInt("positionID"),rs.getString("name"),rs.getBoolean("isDeleted"));
                pList.add(position);
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
    public boolean delete(Position position) throws PersistenceException{
        LOGGER.debug("Entering delete method with parameters " + position);
        String sqlString = "DELETE from position WHERE positionID = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,position.getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()){return true;} else {return false;}
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
