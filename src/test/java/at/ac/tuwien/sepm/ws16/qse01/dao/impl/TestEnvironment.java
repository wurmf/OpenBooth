package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.LogoDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.PositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.ProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test Environment
 * provides setup (Database connections and mocks) for all DAO tests
 */

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestEnvironment.class);
    protected LogoDAO logoDAO;
    protected LogoDAO mockLogoDAO;

    protected PositionDAO positionDAO;
    protected PositionDAO mockPositionDAO;

    protected ProfileDAO profileDAO;
    protected ProfileDAO mockProfileDAO;
    protected Connection con;

    @Mock protected H2Handler mockH2Handler;
    @Mock protected Connection mockConnection;
    @Mock protected Statement mockStatement;
    @Mock protected PreparedStatement mockPreparedStatement;
    @Mock protected ResultSet mockResultSet;

    @Before public void setUp() throws Exception
    {
        /* Setup test mocks
        *  Please don't mess with these ones,
        *  if you don't understand completely what implications it has
        */
        when(mockH2Handler.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.prepareStatement(anyString(),anyInt())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE,Boolean.FALSE);

        mockLogoDAO = new JDBCLogoDAO(mockH2Handler);
        mockPositionDAO = new JDBCPositionDAO(mockH2Handler);
        mockProfileDAO = new JDBCProfileDAO(mockH2Handler);

        /* Setup DAOs for all testing
         */
        logoDAO = new JDBCLogoDAO(H2Handler.getInstance());
        positionDAO = new JDBCPositionDAO(H2Handler.getInstance());
        profileDAO = new JDBCProfileDAO(H2Handler.getInstance());
        this.con = H2Handler.getInstance().getConnection();
        try {
            con.setAutoCommit(false);
            LOGGER.debug("Turn off AutoCommit before beginning testing");
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! AutoCommit couldn't be deactivated:" + e);
        }
    }

    @After public void tearDown() throws PersistenceException {
        // Good tests clean up their environment and reset to initial condition
        // Therefore a database session rollback is performed
        try {
            con.rollback();
            LOGGER.debug("Rollback after finished testing");
        }
        catch (SQLException e) {
            throw new PersistenceException("Error! Rollback couldn't be performed:" + e);
        }
    }
}


