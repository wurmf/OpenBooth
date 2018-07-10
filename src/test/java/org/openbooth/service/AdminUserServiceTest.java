package org.openbooth.service;

import org.openbooth.TestEnvironment;
import org.openbooth.service.exceptions.ServiceException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Test for implementations of the AdminUserService-Interface
 */
public class AdminUserServiceTest extends TestEnvironment {
    static final Logger LOGGER = LoggerFactory.getLogger(AdminUserServiceTest.class);

    private AdminUserService adminUserService = getApplicationContext().getBean(AdminUserService.class);

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
