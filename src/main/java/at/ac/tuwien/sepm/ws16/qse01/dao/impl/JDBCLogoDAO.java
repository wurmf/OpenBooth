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
            if(logo.getId()==Long.MIN_VALUE)
            {
                sqlString = "INSERT INTO logos(path) VALUES (?);";
                stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,logo.getPath());
                stmt.executeUpdate();
                //Get autoassigned id
                rs = stmt.getGeneratedKeys();
                if (rs.next()){logo.setId(rs.getLong(1));}
                LOGGER.debug("Persisted logo object successfully with AutoID:" + logo.getId());
            }
            //No AutoID
            else {
                sqlString = "INSERT INTO logos(logoID,path,isDeleted) VALUES (?,?,?);";
                stmt = this.con.prepareStatement(sqlString);
                stmt.setLong(1,logo.getId());
                stmt.setString(2,logo.getPath());
                stmt.setBoolean(3,logo.isDeleted());
                stmt.executeUpdate();
                LOGGER.debug("Persisted logo object successfully without AutoID:" + logo.getId());
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

        sqlString = "UPDATE logos SET path = ?, isDeleted = ? WHERE logoId = ?;";
        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,logo.getPath());
            stmt.setBoolean(2,logo.isDeleted());
            stmt.setLong(3,logo.getId());
            stmt.executeUpdate();
            rs = stmt.getResultSet();
            // Check, if object has been updated and return suitable boolean value
            if (rs.next()){return true;} else {return false;}
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
    public Logo read(long id) throws PersistenceException {
        LOGGER.debug("Entering read method with parameter id=" + id);

        ResultSet rs;
        String sqlString;
        PreparedStatement stmt = null;

        sqlString = "SELECT * FROM logos WHERE logoID = ?;";
        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setLong(1,id);
            rs = stmt.executeQuery();
            if(!rs.next()) {
                return null;
            }
            else {
                return new Logo(rs.getLong("logoID"),rs.getString("path"),rs.getBoolean("isDeleted"));
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
        return null;
    }

    @Override
    public boolean delete(Logo logo) throws PersistenceException {
        return false;
    }
}
