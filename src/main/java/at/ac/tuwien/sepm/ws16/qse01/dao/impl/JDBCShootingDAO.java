package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * Created by Aniela on 23.11.2016.
 */
@Repository
public class JDBCShootingDAO implements ShootingDAO {

    private Connection con;

    public JDBCShootingDAO() throws Exception {
        con = H2Handler.getInstance().getConnection();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);

    @Override
    public void create(Shooting shouting) throws PersistenceException {
        PreparedStatement stmt = null;
        try {
            String sql="insert into Shootings( profileId,  FOLDERPATH, isactive) values(?,?,?)";

            stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //stmt.setInt(1,profile.getId());
            stmt.setInt(1,shouting.getId());
            stmt.setString(2,shouting.getStorageDir());
            stmt.setBoolean(3,shouting.getActive());
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.info("Shooting",e.getMessage());
            throw new PersistenceException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Select",e);
                }
            }
        }
    }

    @Override
    public Shooting searchIsActive() throws PersistenceException {

        PreparedStatement stmt =null;
        Shooting shouting = new Shooting(0,"",false);
        try {//exists
            stmt = con.prepareStatement("select * from Shootings where isactive = true");
            //stmt.setString(1,name);
            ResultSet rst = stmt.executeQuery();
            while (rst.next()){
                shouting = new Shooting(rst.getInt(2), rst.getString(3), rst.getBoolean(4));
            }
        } catch (SQLException e) {
            LOGGER.info("ShootingDAO",e.getMessage());
            throw new PersistenceException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Select",e);
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
            LOGGER.info("ShootingDAO", e.getMessage());
            throw new PersistenceException(e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    new IllegalArgumentException("Select",e);
                }
            }
        }
    }

}
