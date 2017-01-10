package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2EmbeddedHandler;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCAdminUserDAO;
import at.ac.tuwien.sepm.ws16.qse01.service.AbstractAdminUserServiceTest;
import org.junit.After;
import org.junit.Before;

/**
 * Implementation of a test for the AdminUserServiceImpl
 */
public class AdminUserServiceTestImpl extends AbstractAdminUserServiceTest{
    @Before
    public void setUp() throws PersistenceException{
        H2EmbeddedHandler.getInstance().getTestConnection();
        setAdminUserService(new AdminUserServiceImpl(new JDBCAdminUserDAO(H2EmbeddedHandler.getInstance())));
    }

    @After
    public void restore(){

    }
}
