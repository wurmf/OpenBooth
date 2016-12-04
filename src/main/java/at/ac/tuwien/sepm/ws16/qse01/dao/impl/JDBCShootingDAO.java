package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aniela on 23.11.2016.
 */
public class JDBCShootingDAO implements ShootingDAO {

    private Connection con;

    public JDBCShootingDAO() throws Exception {
        con = H2Handler.getInstance().getConnection();
    }

      private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);

@Override
    public void add_session(Shooting shouting) throws PersistenceException {
      try {
          PreparedStatement stmt=con.prepareStatement("insert into Shootings( profileId,  FOLDERPATH, isactive) values("+ shouting.getPropertyId()+
                  shouting.getStorageFile()+", "+ shouting.getIsactiv()+ " )", java.sql.Statement.RETURN_GENERATED_KEYS);

      } catch (SQLException e) {
            LOGGER.info("Shooting",e.getMessage());
          throw new PersistenceException(e);
        }
    }

@Override
    public Shooting search_isactive() throws PersistenceException {
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
    public void end_session() {
   try {
            String prepered="update Shootings set isactive=? where isactive= ?";
            PreparedStatement stmt = con.prepareStatement(prepered);

            stmt.setBoolean(1,false);
            stmt.setBoolean(2,true);
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.info("ShootingDAO", e.getMessage());
        }
    }

}
