package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.AbstractImageDAOTest;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import org.junit.After;
import org.junit.Before;

/**
 * Implementation of a test for the JDBCAdminUserDAO-class.
 */
public class JDBCImageDAOTest extends AbstractImageDAOTest {
    @Before
    public void setUp() throws Exception {
        setImageDAO(new JDBCImageDAO(H2Handler.getInstance()));
        setShootingDAO(new JDBCShootingDAO(H2Handler.getInstance()));
    }
    @After
    public void restore(){

    }
}
