package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.ProfileDAOTest;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBCProfileDAOTest class to extend all abstract test classes
 * is used to set up and at end to tear down concrete H2-DB test environment
 */
public class JDBCProfileDAOTest extends ProfileDAOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCProfileDAOTest.class);
    private ProfileDAO profileDAO;
    private Connection con;

    @Before
    public void setUp() throws PersistenceException {
        JDBCProfileDAO profileDAO = new JDBCProfileDAO();
        this.con = H2Handler.getInstance().getConnection();
        setProfileDAO(profileDAO);
        try {
            con.setAutoCommit(false);
            LOGGER.debug("Turn off AutoCommit before beginning testing");
        } catch (SQLException e) {
            throw new PersistenceException("Error! AutoCommit couldn't be deactivated:" + e);
        }
    }
    @After
    public void tearDown() throws PersistenceException {
        // Good tests clean up their environment and reset to initial pretest condition therefore a collback is performed
        try {
            con.rollback();
            LOGGER.debug("Rollback after finished testing");
        } catch (SQLException e) {
            throw new PersistenceException("Error! Rollback couldn't be performed:" + e);
        }
    }
}
