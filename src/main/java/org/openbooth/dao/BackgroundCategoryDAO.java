package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;

import java.util.List;

/**
 * BackgroundCategoryDAO
 */
public interface BackgroundCategoryDAO {
    /**
     * Persists a given Background.Category object.
     * A unique id will be auto-assigned, the original value will be ignored.
     * @param backgroundCategory - a non null Background.Category object
     * @return the persisted Background.Category object with auto-assigned id
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Background.Category create(Background.Category backgroundCategory) throws PersistenceException;

    /**
     * Persists change of an existing and already persisted Background.Category object
     * identification, if Background.Category object exists already in persistence store is done by comparing ids
     * in case Background.Category object with given id exists and can be updated in the persistence store, true value will be returned
     * if Background.Category object with given id doesn't exist in persistence store, nothing will be updated and a PersistenceException will be thrown
     * @param backgroundCategory - a non null Background.Category object to be updated
     * @throws PersistenceException if persistence data store can not be accessed or no background category with the given id exists
     */
    void update(Background.Category backgroundCategory) throws PersistenceException;

    /**
     * Retrieve a Background.Category object identified by id from the persistence store
     * independently, if it has been marked as deleted or not
     * @param id - id of the Background.Category object that is looked for
     * @return Background.Category object that has been looked for, if a Background.Category object with given id doesn't
     * exist in persistence store, a PersistenceException will be thrown
     * @throws PersistenceException if persistence data store can not be accessed or no object with the requested id exists in data store
     */
    Background.Category read(int id) throws PersistenceException;

    /**
     * Retrieve all Background.Category objects from persistence store(that have not been deleted)
     * @return all Background.Category objects in a list (that have not been deleted). This list may contain also no objects
     * @throws PersistenceException if persistence data store can not be accessed
     */
    List<Background.Category> readAll() throws PersistenceException;

    /**
     * Remove given Background.Category from persistence store(in fact mark it as deleted in the persistence store
     * @param backgroundCategory - a non null Background.Category object to be deleted.
     * @throws PersistenceException if persistence data store can not be accessed or the given object does not exists in persistence
     */
    void delete(Background.Category backgroundCategory) throws PersistenceException;

    void createProfileCategoryRelation(int profileID, int categoryID) throws PersistenceException;
    void deleteProfileCategoryRelation(int profileID, int categoryID) throws PersistenceException;
    List<Background.Category> getAllCategoriesForProfile(int profileID) throws PersistenceException;
}
