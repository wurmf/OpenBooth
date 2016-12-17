package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Logo;

import java.util.List;

/**
 * Logo DAO
 */
public interface LogoDAO {
    /**
     * Persists a given logo object with or without known id(in case, if id = Integer.MIN_VALUE)
     * In case id is unknown an unique id will be autoassigned
     * @param logo - logo object to be persisted, must not be null
     * @return persisted logo object with possible autoassigned id(in case, if id = Integer.MIN_VALUE)
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Logo create(Logo logo) throws PersistenceException;

    /**
     * Persists change of an existing and already persisted logo object
     * identification, if logo object exists already in persistence store is done by comparing ids
     * in case logo object with given id exists and can be updated in the persistence store, true value will be returned
     * if logo object with given id doesn't exist in persistence store, nothing will be updated and false value will be returned
     * @param logo - logo object with changed properties to be persisted
     * @return true, if given logo object is deleted in persistence store successfully, false otherwise
     * @throws PersistenceException if persistence data store can not be accessed
     */
    boolean update(Logo logo) throws PersistenceException;

    /**
     * Retrieve a logo object identified by id from the persistence store
     * independenently if it has been marked as deleted or not
     * @param id - id of the logo object that is looked for
     * @return logo object that has been looked for, if a logo object with given id doesn't
     * exist in persistence store, null value will be returned
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Logo read(int id) throws PersistenceException;

    /**
     * Retrieve all logo objects from persistence store(that have not been deleted)
     * @return all logo objects in a List(that have not been deleted). This list may contain also no objects
     * @throws PersistenceException if persistence data store can not be accessed
     */
    List<Logo> readAll() throws PersistenceException;

    /**
     * Remove given logo from persistence store(in fact mark it as deleted in the persistence store
     * @param logo - Logo to be removed is identified by its id
     * @return true if given logo object is deleted in persistence store successfully, false otherwise
     * @throws PersistenceException if persistence data store can not be accessed
     */
    boolean delete(Logo logo) throws PersistenceException;
}
