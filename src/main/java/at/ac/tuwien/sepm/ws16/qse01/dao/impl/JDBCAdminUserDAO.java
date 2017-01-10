package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.AdminUserDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.AdminUser;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of the AdminUserDAO-Interface using H2-syntax to manipulate the the adminuser table in an H2-database.
 */
@Repository
public class JDBCAdminUserDAO implements AdminUserDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCAdminUserDAO.class);

    private Connection con;

    /**
     * Constructor with a DBHandler specified, which will be used to retrieve a database connection.
     * @param handler DBHandler from which the connection will be taken.
     * @throws PersistenceException if an error occurs while retrieving a connection from the DBHandler.
     */
    @Autowired
    public JDBCAdminUserDAO(DBHandler handler) throws PersistenceException{
        con=handler.getConnection();
    }

    @Override
    public AdminUser read(String adminName) throws PersistenceException{
        String loggerFuncName="read";
        String query="SELECT * FROM adminusers WHERE adminname=?";
        PreparedStatement stmt=null;
        ResultSet rs=null;
        try {
            stmt=con.prepareStatement(query);
            stmt.setString(1,adminName);
            rs=stmt.executeQuery();
            if(rs.next()){
                String newAdminName=rs.getString("adminname");
                byte[] password=rs.getBytes("password");
                return new AdminUser(newAdminName, password);
            } else{
                return null;
            }
        } catch (SQLException e) {
            LOGGER.error(loggerFuncName+" - "+e);
            throw new PersistenceException(e);
        } finally{
            closeResource(stmt,loggerFuncName);
            closeResource(rs,loggerFuncName);
        }
    }

    /**
     * This method closes any resource that is given to it.
     * @param c an AutoCloseable that shall be closed.
     * @param loggerFuncName the name of the function that called this function, used for the logger statement if an exception occurs.
     * @throws PersistenceException if the resource cannot be closed.
     */
    private void closeResource(AutoCloseable c, String loggerFuncName) throws PersistenceException{
        if(c!=null){
            try{
                c.close();
            } catch(Exception e){
                LOGGER.error(loggerFuncName+" - "+e);
                throw new PersistenceException(e);
            }
        }
    }
}
