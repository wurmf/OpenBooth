package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;

import at.ac.tuwien.sepm.ws16.qse01.dao.ProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 database-Specific ProfileDAO Implementation
 */
public class JDBCProfileDAO implements ProfileDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(JDBCProfileDAO.class);
    private Connection con;
    public JDBCProfileDAO() throws PersistenceException {
        LOGGER.debug("Entering constructor");
        try {
            con = H2Handler.getInstance().getConnection();
        } catch (Exception e) {
            throw new PersistenceException("Error!:Establishing connection to persistence store has failed." + e);
        }
    }

    @Override
    public Profile create(Profile profile) throws PersistenceException{
        LOGGER.debug("Entering create method with parameters " + profile);
        if(profile==null)throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        String sqlString = "INSERT INTO profiles (profileID,name) VALUES (?,?);";
        PreparedStatement stmt = null;
        try {
            stmt = this.con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1,profile.getId());
            stmt.setString(2,profile.getName());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){profile.setId(rs.getInt(1));}
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
    public void update(Profile profile) throws PersistenceException {
        LOGGER.debug("Entering update method with parameters " + profile);
        if(profile==null)throw new IllegalArgumentException("Error! Called update method with null pointer.");
        String sqlString = "UPDATE profiles SET name = ?,isActive = ? WHERE profileID = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setString(1,profile.getName());
            stmt.setBoolean(2,profile.isActive());
            stmt.setInt(3,profile.getId());
            stmt.executeUpdate();
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
        String sqlString = "SELECT * FROM profiles WHERE profileID = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()) {
                return null;
                /*new PersistenceException("No data existing to read: Profile with " + id + " isn't persisted yet!");*/
            }
            return new Profile(rs.getInt("profileID"),rs.getString("name"),rs.getBoolean("isActive"));
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
        String sqlString = "SELECT * FROM profiles;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            ResultSet rs = stmt.executeQuery();
            List<Profile> pList = new ArrayList<>();

            while (rs.next()) {
                Profile profile = new Profile(rs.getInt("profileID"),rs.getString("name"),rs.getBoolean("isActive"));
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
    public void delete(Profile profile) throws PersistenceException{
        LOGGER.debug("Entering delete method with parameters " + profile);
        String sqlString = "DELETE * from profiles WHERE profileID = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = this.con.prepareStatement(sqlString);
            stmt.setInt(1,profile.getId());
            stmt.executeUpdate();
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
