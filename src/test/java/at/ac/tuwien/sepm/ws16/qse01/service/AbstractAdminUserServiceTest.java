package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Martin Schroeder on 27.11.16.
 */
public abstract class AbstractAdminUserServiceTest {
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdminUserServiceTest.class);

    protected AdminUserService adminUserService;

    protected void setAdminUserService(AdminUserService adminUserService){
        this.adminUserService=adminUserService;
    }
    @Test
    public void checkExistentUser(){
        try {
            assertTrue(adminUserService.checkLogin("admin","martin"));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }
    @Test
    public void checkNonExistentUser(){
        try {
            assertTrue(!adminUserService.checkLogin("nonExistentUser-NoOneIsNamedLikeThis","martin"));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }
    @Test
    public void checkWrongPassword(){
        try {
            assertTrue(!adminUserService.checkLogin("admin","martin1"));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }
    @Test
    public void checkEmptyPassword(){
        try {
            assertFalse(adminUserService.checkLogin("admin",""));
            assertFalse(adminUserService.checkLogin("admin",null));
        } catch (ServiceException e) {
            LOGGER.error(e.toString());
        }
    }
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
