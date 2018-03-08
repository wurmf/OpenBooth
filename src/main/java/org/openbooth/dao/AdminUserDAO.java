package org.openbooth.dao;

import org.openbooth.entities.AdminUser;
import org.openbooth.dao.exceptions.PersistenceException;

/**
 * Interface that allows to get login information for admins from an unspecified datasource.
 */
public interface AdminUserDAO {
    /**
     * Browses the datasource for an entry with the specified admin-name and returns the AdminUser-object with the correspnding password if an entry was found.
     * @param adminName name of the admin user that shall be searched for.
     * @return an AdminUser-object with the specified name and corresponding password if an entry for this name was found, null otherwise.
     * @throws PersistenceException if an error occurs upon trying to read from the datasource.
     */
    AdminUser read(String adminName) throws PersistenceException;
}
