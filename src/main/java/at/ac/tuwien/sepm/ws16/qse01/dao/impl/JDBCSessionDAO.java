package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.SessionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aniela on 23.11.2016.
 */
public class JDBCSessionDAO implements SessionDAO {

    private Connection con;

    public JDBCSessionDAO() throws Exception {
        con = H2Handler.getInstance().getConnection();
    }

      private static final Logger LOGGER = LoggerFactory.getLogger(SessionDAO.class);

    @Override
    public void add_session(Session session) throws PersistenceException {
      try {
          PreparedStatement stmt=con.prepareStatement("insert into Session( profileId, storageFile, isactive) values("+session.getPropertyId()+
                  session.getStorageFile()+", "+ session.getIsactiv()+ " )", java.sql.Statement.RETURN_GENERATED_KEYS);

      } catch (SQLException e) {
            LOGGER.info("SessionDAO",e.getMessage());
          throw new PersistenceException(e);
        }
    }

    @Override
    public Session search_isactive() throws PersistenceException {
      Session session= new Session(0,"",false);
         try {//exists
                PreparedStatement stmt = con.prepareStatement("select * from Session where isactive = false");
                //stmt.setString(1,name);
                ResultSet rst = stmt.executeQuery();
                while (rst.next()){
                    session = new Session(rst.getInt(2), rst.getString(3), rst.getBoolean(4));
                }
            } catch (SQLException e) {
                LOGGER.info("SessionDAO",e.getMessage());
                throw new PersistenceException(e);
            }
        return session;
    }

    @Override
    public void end_session() {
   try {
            String prepered="update Session set isactive=? where isactive=?";
            PreparedStatement stmt = con.prepareStatement(prepered);

            stmt.setBoolean(1,false);
            stmt.setBoolean(2,true);
            stmt.execute();

        } catch (SQLException e) {
            LOGGER.info("SessionDAO", e.getMessage());
        }
    }

}
