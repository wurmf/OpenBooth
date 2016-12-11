package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;

import java.util.List;

/**
 * ProfileDAO
 */
public interface ProfileDAO {

    /**
     * Persists a given profile with or without known id
     * In case id is unknown an unique id will be autoassigned
     * @param profile - profile entity to be persisted
     * @return persisted profile entity with eventuelly autoassigned id
     */
    Profile create(Profile profile) throws PersistenceException;

    /**
     * Persists change of an existing and already persisted profile
     * identification, if profile exists already in persistence store is made by comparing ids
     * if profile with give id doesn't exist in persistence store, nothing will be updated
     * @param profile - profile entity with changed properties to be persisted
     */
    boolean update(Profile profile) throws PersistenceException;

    /**
     * Retrieve a profile entity identified by id from the persistence store
     * @param id - id of the profile that is looked for
     * @return profile that has been looked for, if a profile with given id doesn't
     * exist in prsistence store null will be returned
     */
    Profile read(int id) throws PersistenceException;

    /**
     * Retrieve all profiles entities from persistence store
     * @return all profiles in a List
     */
    List<Profile> readAll() throws PersistenceException;

    /**
     * Remove given profile from persistence store
     * @param profile - profile to be remove is identified by its id
     */
    boolean delete(Profile profile) throws PersistenceException;
}
