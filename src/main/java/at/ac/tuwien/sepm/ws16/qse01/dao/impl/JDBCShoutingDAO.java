package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShoutingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shouting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aniela on 23.11.2016.
 */
public class JDBCShoutingDAO implements ShoutingDAO {

    private Connection con;

    public JDBCShoutingDAO() throws Exception {
        con = H2Handler.getInstance().getConnection();
    }

      private static final Logger LOGGER = LoggerFactory.getLogger(ShoutingDAO.class);


    public void add_session(Shouting shouting) throws PersistenceException {
      try {
          PreparedStatement stmt=con.prepareStatement("insert into Shouting( profileId, storageFile, isactive) values("+ shouting.getPropertyId()+
                  shouting.getStorageFile()+", "+ shouting.getIsactiv()+ " )", java.sql.Statement.RETURN_GENERATED_KEYS);

      } catch (SQLException e) {
            LOGGER.info("Shoouting",e.getMessage());
          throw new PersistenceException(e);
        }
    }


    public Shouting search_isactive() throws PersistenceException {
      Shouting shouting = new Shouting(0,"",false);
         try {//exists
                PreparedStatement stmt = con.prepareStatement("select * from Shouting where isactive = false");
                //stmt.setString(1,name);
                ResultSet rst = stmt.executeQuery();
                while (rst.next()){
                    shouting = new Shouting(rst.getInt(2), rst.getString(3), rst.getBoolean(4));
                }
            } catch (SQLException e) {
                LOGGER.info("ShoutingDAO",e.getMessage());
                throw new PersistenceException(e);
            }
        return shouting;
    }

    public void end_session() {
   try {
            String prepered="update Shouting set isactive=? where isactive=?";
            PreparedStatement stmt = con.prepareStatement(prepered);

            stmt.setBoolean(1,false);
            stmt.setBoolean(2,true);
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.info("ShoutingDAO", e.getMessage());
        }
    }

}
