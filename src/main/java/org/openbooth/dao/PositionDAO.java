package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Position;

import java.util.List;

/**
 * Position DAO
 */
public interface PositionDAO {
    /**
     * Persists a given position with or without known id(in case, if id = Integer.MIN_VALUE)
     * In case id is unknown an unique id will be autoassigned
     * @param position - position entity to be persisted
     * @return persisted position entity with possibly autoassigned id(in case, if id = Integer.MIN_VALUE)
     */
    Position create(Position position) throws PersistenceException;

    /**
     * Persists change of an existing and already persisted position
     * identification, if position exists already in persistence store is made by comparing ids
     * if position with give id doesn't exist in persistence store, nothing will be updated and false will be returned
     * @param position - position entity with changed properties to be persisted
     * @return true if given position object could be updated in persistence store, false in all other cases
     */
    boolean update(Position position) throws PersistenceException;

    /**
     * Retrieve a position entity identified by id from the persistence store
     * @param id - id of the position that is looked for
     * @return position that has been looked for, if a position with given id doesn't
     * exist in persistence store, null will be returned
     */
    Position read(int id) throws PersistenceException;

    /**
     * Retrieve all position entities from persistence store(that have not been deleted)
     * @return all positions in a List(that have not been deleted)
     */
    List<Position> readAll() throws PersistenceException;

    /**
     * Remove given position from persistence store(in fact mark it as deleted in the persistence store
     * @param position - position to be removed is identified by its id
     * @return true if given position object could be deleted in persistence store, false in all other cases
     */
    boolean delete(Position position) throws PersistenceException;
}
