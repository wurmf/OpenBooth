package org.openbooth.service;

import org.openbooth.util.dbhandler.impl.H2EmbeddedHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.dao.impl.JDBCAdminUserDAO;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.impl.AdminUserServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Test for implementations of the AdminUserService-Interface
 */
public class AdminUserServiceTest {
    static final Logger LOGGER = LoggerFactory.getLogger(AdminUserServiceTest.class);

    @Autowired
    protected AdminUserService adminUserService;

    @Before
    public void setUp() throws DatabaseException, PersistenceException {
        H2EmbeddedHandler.getInstance().getTestConnection();
        adminUserService = new AdminUserServiceImpl(new JDBCAdminUserDAO(H2EmbeddedHandler.getInstance()));
    }

    /**
     * Checks the return calue of an existing entry.
     */
    @Ignore
    @Test
    public void checkExistentUser(){
        try {
            assertTrue(adminUserService.checkLogin("admin","martin"));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * Checks the return of a user that is not in the database
     */
    @Test
    public void checkNonExistentUser(){
        try {
            assertTrue(!adminUserService.checkLogin("nonExistentUser-NoOneIsNamedLikeThis","martin"));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * Checks the return for existing user and wrong password
     */
    @Test
    public void checkWrongPassword(){
        try {
            assertTrue(!adminUserService.checkLogin("admin","martin1"));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * Checks the return for existing user and empty and null-password
     */
    @Test
    public void checkEmptyPassword(){
        try {
            assertFalse(adminUserService.checkLogin("admin",""));
            assertFalse(adminUserService.checkLogin("admin",null));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * Checks the return for empty or null username
     */
    @Test
    public void checkEmptyAdminName(){
        try {
            assertFalse(adminUserService.checkLogin("","martin"));
            assertFalse(adminUserService.checkLogin(null,"martin"));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }

}
