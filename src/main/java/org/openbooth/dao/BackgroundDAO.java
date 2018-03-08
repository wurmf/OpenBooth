package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;

import java.util.List;

/**
 * BackgroundDAO
 */
public interface BackgroundDAO {
    /**
     * Persists a given Background object with or without known id
     * (case, if id = Integer.MIN_VALUE)
     * In case id is unknown an unique id will be auto-assigned
     * @param background - a non null Background object
     * @return persisted persisted Background object with possible auto-assigned id
     * (in case, if id = Integer.MIN_VALUE)
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
     * @return true, if given object is updated in persistence store successfully, false otherwise
     * @throws PersistenceException if persistence data store can not be accessed
     */
    boolean update(Background background) throws  PersistenceException;

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

    List<Background> readAllWithCategory(int id) throws PersistenceException;

    /**
     * Remove given Background from persistence store(in fact mark it as deleted in the persistence store
     * @param background - a non null Background object to be deleted
     * @return true if given Background object is deleted in persistence store successfully, false otherwise
     * @throws PersistenceException if persistence data store can not be accessed
     */
    boolean delete(Background background) throws PersistenceException;
}
