package at.ac.tuwien.sepm.ws16.qse01.dao.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.AbstractAdminUserDAOTest;
import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import org.junit.After;
import org.junit.Before;

/**
 * Implementation of a test for the JDBCAdminUserDAO-class.
 */
public class JDBCAdminUserDAOTest extends AbstractAdminUserDAOTest{
    @Before
    public void setUp() throws PersistenceException {
        setAdminUserDAO(new JDBCAdminUserDAO(H2Handler.getInstance()));
    }
    @After
    public void restore(){

    }
}
