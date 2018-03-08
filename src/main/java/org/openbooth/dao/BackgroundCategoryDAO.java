package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;

import java.util.List;

/**
 * BackgroundCategoryDAO
 */
public interface BackgroundCategoryDAO {
    /**
     * Persists a given Background.Category object with or without known id
     * (case, if id = Integer.MIN_VALUE)
     * In case id is unknown an unique id will be auto-assigned
     * @param backgroundCategory - a non null Background.Category object
     * @return persisted persisted Background.Category object with possible auto-assigned id(in case, if id = Integer.MIN_VALUE)
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Background.Category create(Background.Category backgroundCategory) throws PersistenceException;

    /**
     * Persists change of an existing and already persisted Background.Category object
     * identification, if Background.Category object exists already in persistence store is done by comparing ids
     * in case Background.Category object with given id exists and can be updated in the persistence store, true value will be returned
     * if Background.Category object with given id doesn't exist in persistence store, nothing will be updated and false value will be returned
     * @param backgroundCategory - a non null Background.Category object to be updated
     * @return true, if given object is updated in persistence store successfully, false otherwise
     * @throws PersistenceException if persistence data store can not be accessed
     */
    boolean update(Background.Category backgroundCategory) throws PersistenceException;

    /**
     * Retrieve a Background.Category object identified by id from the persistence store
     * independently, if it has been marked as deleted or not
     * @param id - id of the Background.Category object that is looked for
     * @return Background.Category object that has been looked for, if a Background.Category object with given id doesn't
     * exist in persistence store, null value will be returned
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Background.Category read(int id) throws PersistenceException;

    /**
     * Retrieve all Background.Category objects from persistence store(that have not been deleted)
     * @return all Background.Category objects in a List(that have not been deleted). This list may contain also no objects
     * @throws PersistenceException if persistence data store can not be accessed
     */
    List<Background.Category> readAll() throws PersistenceException;

    /**
     * Remove given Background.Category from persistence store(in fact mark it as deleted in the persistence store
     * @param backgroundCategory - a non null Background.Category object to be delete
     * @return true if given Background.Category object is deleted in persistence store successfully, false otherwise
     * @throws PersistenceException if persistence data store can not be accessed
     */
    boolean delete(Background.Category backgroundCategory) throws PersistenceException;
    void createPairProfileCategory(int profileID,int categoryID) throws PersistenceException;
    void deletePairProfileCategory(int profileID,int categoryID) throws PersistenceException;
    List<Background.Category> readAllOfProfile(int profileID) throws PersistenceException;
}
