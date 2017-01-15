package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.util.exceptions.DatabaseException;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * ShootingDaoImpl
 */
@Repository
public class JDBCShootingDAO implements ShootingDAO {

    private Connection con;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);

    @Autowired
    public JDBCShootingDAO(DBHandler dbHandler) throws PersistenceException {
        try {
            con = dbHandler.getConnection();
        } catch (DatabaseException e) {
            LOGGER.error("Constructor - ",e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public Shooting create(Shooting shouting) throws PersistenceException {
        PreparedStatement stmt = null;
        if(shouting==null) {
            LOGGER.debug("create - caught nullpointerShooting");
            throw new IllegalArgumentException("Error!:Called create method with null pointer.");
        }



        try {
            String sql="insert into Shootings(profileId,  FOLDERPATH, isactive) values(?,?,?)";

            stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //stmt.setInt(1,profile.getId());
            stmt.setInt(1,shouting.getProfileid());
            stmt.setString(2,shouting.getStorageDir());
            stmt.setBoolean(3,shouting.getActive());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){shouting.setId(rs.getInt(1));}

        } catch (SQLException e) {
            LOGGER.info("create - ",e);
            throw new PersistenceException(e);
        } catch(IllegalArgumentException i) {
            LOGGER.error("create - ",i);
            throw new PersistenceException(i);
        } finally{
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Select",e);
                }
            }
        }
        return shouting;
    }

    @Override
    public Shooting searchIsActive() throws PersistenceException {

        PreparedStatement stmt =null;
        Shooting shouting = new Shooting(0,0,"",false);
        try {//exists
            stmt = con.prepareStatement("select * from Shootings where isactive = true");
            //stmt.setString(1,name);
            ResultSet rst = stmt.executeQuery();
            while (rst.next()){
                shouting = new Shooting(rst.getInt("SHOOTINGID"), rst.getInt("PROFILEID"),rst.getString("FOLDERPATH"), rst.getBoolean("ISACTIVE"));
            }
        } catch (SQLException e) {
            LOGGER.error("searchIsActive - ",e);
            throw new PersistenceException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                   LOGGER.error("searchIsActive - ",e);
                }
            }
        }
        return shouting;
    }

    @Override
    public void endShooting() throws PersistenceException {
        PreparedStatement stmt=null;
        try {
            String prepered="update Shootings set isactive=? where isactive= ?";
            stmt = con.prepareStatement(prepered);

            stmt.setBoolean(1,false);
            stmt.setBoolean(2,true);
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.error("endShooting - ", e);
            throw new PersistenceException(e);
        } catch(AssertionError i) {
            LOGGER.error("endShooting - ",i);
            throw new PersistenceException("No Activ Shooting Found");
        }finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("endShooting - ",e);
                }
            }
        }
    }

    @Override
    public void updateProfile(Shooting shooting) throws PersistenceException {
        PreparedStatement stmt=null;
        try {
            String prepered="update Shootings set PROFILEID=? where SHOOTINGID= ?";
            stmt = con.prepareStatement(prepered);

            stmt.setInt(1,shooting.getProfileid());
            stmt.setInt(2,shooting.getId());
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.info("updateProfile - ", e);
            throw new PersistenceException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("updateProfile - ",e);
                }
            }
        }
    }

}
