package org.openbooth.service.impl;

import org.openbooth.dao.AdminUserDAO;
import org.openbooth.entities.AdminUser;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
