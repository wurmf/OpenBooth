package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;

import java.util.List;

/**
 * BackgroundDAO
 */
public interface BackgroundDAO {
    /**
     * Persists a given background object, a unique id will be auto-assigned
     * @param background - a non null Background object
     * @return the persisted background object with auto-assigned id
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Background create(Background background) throws PersistenceException;

    /**
     * Persists change of an existing and already persisted Background object
     * identification, if Background object exists already in persistence store is done by comparing ids
     * in case Background object with given id exists and can be updated in the persistence store,
     * true value will be returned
     * if Background.Category object with given id doesn't exist in persistence store,
     * nothing will be updated and false value will be returned
     * @param background - a non null Background object to be updated
     * @throws PersistenceException if persistence data store can not be accessed or no background with the given id could be found
     */
    void update(Background background) throws  PersistenceException;

    /**
     * Retrieve a Background object identified by id from the persistence store
     * independently, if it has been marked as deleted or not
     * @param id - id of the Background object that is looked for
     * @return Background object that has been looked for, if a Background object with given id doesn't
     * exist in persistence store, null value will be returned
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Background read(int id) throws PersistenceException;

    /**
     * Retrieve all Background objects that are not deleted
     * @return List of Background objects
     * @throws PersistenceException if persistence data store can not be accessed
     */
    List<Background> readAll() throws PersistenceException;

    /**
     * Returns empty list if there is no background for this category or this category does not exists
     * @param category
     * @return
     * @throws PersistenceException
     */
    List<Background> readAllWithCategory(Background.Category category) throws PersistenceException;

    /**
     * Remove given Background from persistence store(in fact mark it as deleted in the persistence store
     * @param background - a non null Background object to be deleted
     * @throws PersistenceException if persistence data store can not be accessed or no background with the given id could be found
     */
    void delete(Background background) throws PersistenceException;
}
