package org.openbooth.util.dbhandler;

import org.openbooth.util.dbhandler.impl.H2EmbeddedHandler;
import org.openbooth.util.dbhandler.prep.DataPrepper;
import org.openbooth.util.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Primary
@Component
public class TestH2EmbeddedHandler extends H2EmbeddedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestH2EmbeddedHandler.class);

    /**
     * Returns a connection to the test-database. If there is no open connection, the method calls {@link #openConnection(String dbName)} to start the database-server and create a connection.
     * @return a connection to the test-database.
     * @throws DatabaseException if an error occured while opening a new connection or if there is an open connection to the default database.
     */
    @Override
    public Connection getConnection() throws DatabaseException{
        if(connection==null){
            try {
                openConnection("openboothTest");
            } catch (SQLException |ClassNotFoundException e) {
                throw new DatabaseException(e);
            }
        }
        return connection;
    }

    @Override
    protected void onFirstStartup(Connection connection) throws DatabaseException{
        DataPrepper.createTables(connection);
    }


}
