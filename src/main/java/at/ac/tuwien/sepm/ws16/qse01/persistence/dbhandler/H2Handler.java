package at.ac.tuwien.sepm.ws16.qse01.persistence.dbhandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This Singleton-class returns a connection to an H2-database called "fotostudio".
 * The class will always return the same connection-object as long as {@link #closeConnection()} is not called. If it is called a new connection will be opened when calling {@link #getConnection()}.
 */
public class H2Handler  implements DBHandler{
    //private final Logger logger= new Logger();

    private static H2Handler ourInstance = new H2Handler();
    private Connection connection;

    public static H2Handler getInstance() {
        return ourInstance;
    }

    private H2Handler() {
        this.connection=null;
    }

    /*
    Precondition: The H2-DB-application is running with a database called "fotostudio" and a user "sa" with an empty password.
     */
    /**
     * Opens a new connection to an H2-database of the name "fotostudio" with username "sa" and empty password
     * @throws Exception if either the driver-class is not found or the DriverManager can't establish a connection to the specified database with the given login credentials.
     */
    private void openConnection() throws Exception{
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/fotostudio", "sa","");
        } catch(SQLException e){
            //logger.error("establishConnection - "+e.getMessage());
            throw e;
        }
    }

    @Override
    public Connection getConnection() throws Exception {
        if(connection==null){
            openConnection();
        }
        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            //logger.error("closeConnection - "+e.getMessage());
        } finally {
            connection=null;
        }
    }
}
