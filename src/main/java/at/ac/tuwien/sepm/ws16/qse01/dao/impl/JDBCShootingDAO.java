package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * Created by Aniela on 23.11.2016.
 */
@Repository
public class JDBCShootingDAO implements ShootingDAO {

    private Connection con;

    @Autowired
    public JDBCShootingDAO(DBHandler dbHandler) throws PersistenceException {
        con = dbHandler.getConnection();
    }

      private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);

@Override
    public void create(Shooting shouting) throws PersistenceException {
      try {
          String sql="insert into Shootings( profileId,  FOLDERPATH, isactive) values(?,?,?)";
          PreparedStatement stmt = null;
              stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
              //stmt.setInt(1,profile.getId());
              stmt.setInt(1,shouting.getId());
              stmt.setString(2,shouting.getStorageDir());
              stmt.setBoolean(3,shouting.getActive());
              stmt.executeUpdate();

      } catch (SQLException e) {
            LOGGER.info("Shooting",e.getMessage());
          throw new PersistenceException(e);
        }
    }

@Override
    public Shooting searchIsActive() throws PersistenceException {
      Shooting shouting = new Shooting(0,"",false);
         try {//exists
                PreparedStatement stmt = con.prepareStatement("select * from Shootings where isactive = true");
                //stmt.setString(1,name);
                ResultSet rst = stmt.executeQuery();
                while (rst.next()){
                    shouting = new Shooting(rst.getInt(2), rst.getString(3), rst.getBoolean(4));
                }
            } catch (SQLException e) {
                LOGGER.info("ShootingDAO",e.getMessage());
                throw new PersistenceException(e);
            }
        return shouting;
    }

    @Override
    public void endShooting() throws PersistenceException {
   try {
            String prepered="update Shootings set isactive=? where isactive= ?";
            PreparedStatement stmt = con.prepareStatement(prepered);

            stmt.setBoolean(1,false);
            stmt.setBoolean(2,true);
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.info("ShootingDAO", e.getMessage());
            throw new PersistenceException(e.getMessage());
        }
    }

}
