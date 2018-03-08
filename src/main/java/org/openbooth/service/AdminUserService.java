package org.openbooth.service;

import org.openbooth.service.exceptions.ServiceException;

/**
 * Used to check the login credentials for an admin account given by the user.
 */
public interface AdminUserService {
    /**
     * Checks if an admin-user is saved with the given login credentials.
     * @param adminNameInput the adminname given as input by the user.
     * @param passwordInput the password given as input by the user.
     * @return true if an adminUser with the specified name and password was found, false otherwise.
     * @throws ServiceException if an error occurs while trying to run the credential checking.
     */
    boolean checkLogin(String adminNameInput,String passwordInput) throws ServiceException;
}
