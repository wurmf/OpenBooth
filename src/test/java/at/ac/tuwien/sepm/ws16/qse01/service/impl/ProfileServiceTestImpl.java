package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileServiceTest;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
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
public class ProfileServiceTestImpl extends ProfileServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceTestImpl.class);
    private ProfileService profileService;
    private Connection con;

    @Before
    public void setUp() throws PersistenceException, ServiceException {
        ProfileServiceImpl profileService = new ProfileServiceImpl(new JDBCProfileDAO(H2Handler.getInstance()));
        //ProfileServiceImpl profileService = new ProfileServiceImpl();
        this.con = H2Handler.getInstance().getConnection();
        setProfileService(profileService);
        try {
            con.setAutoCommit(false);
            LOGGER.debug("Turn off AutoCommit before beginning testing");
        } catch (SQLException e) {
            throw new PersistenceException("Error! AutoCommit couldn't be deactivated:" + e);
        }
    }
    @After
    public void tearDown() throws ServiceException, PersistenceException {
        // Good tests clean up their environment and reset to initial pretest condition therefore a collback is performed
        try {
            con.rollback();
            LOGGER.debug("Rollback after finished testing");
        } catch (SQLException e) {
            throw new PersistenceException("Error! Rollback couldn't be performed:" + e);
        }
    }
}
