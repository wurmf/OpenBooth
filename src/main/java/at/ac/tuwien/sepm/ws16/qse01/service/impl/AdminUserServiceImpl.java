package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.AdminUserDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.AdminUser;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Implementation of
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    static final Logger LOGGER = LoggerFactory.getLogger(AdminUserServiceImpl.class);
    private AdminUserDAO adminUserDAO;
    @Autowired
    public AdminUserServiceImpl(AdminUserDAO adminUserDAO){
        this.adminUserDAO=adminUserDAO;
    }
    @Override
    public boolean checkLogin(String adminNameInput, String passwordInput) throws ServiceException {
        try {
            if(adminNameInput==null||passwordInput==null){
                return false;
            }
            AdminUser user=adminUserDAO.read(adminNameInput);
            if(user==null){
                return false;
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passwordInput.getBytes("UTF-8"));
            byte[] inputPass = md.digest();
            return Arrays.equals(inputPass,user.getPassword());
        } catch (PersistenceException|NoSuchAlgorithmException|UnsupportedEncodingException e) {
            LOGGER.error("checkLogin - ",e);
            throw new ServiceException(e);
        }
    }
}
