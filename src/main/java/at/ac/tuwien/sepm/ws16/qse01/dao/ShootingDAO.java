package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;

import java.util.List;

/**
 * Shooting Dao
 */
public interface ShootingDAO {
    /**
     *
     * adds new shooting to database
     *
     * @throws PersistenceException caused by using sql
     * @param shooting returnes the shooting thats created
     *
     */
     Shooting create(Shooting shooting) throws PersistenceException;

    /**
     *
     * search for active sessions and returns them
     * if null default with inactife gets passed
     *
     * @throws PersistenceException caused by using sql
     * @return Shooting that is active, if there is no active shooting available it returnes an defaulte false shooting
     */
     Shooting searchIsActive() throws PersistenceException;

    /**
     * closes activ session
     * by setting the session to false
     *
     * @throws PersistenceException caused by using sql
     */
     void endShooting() throws PersistenceException;

    /**
     * Will update data saved for this shooting in the persistence.
     * @param shooting Shooting-object with the same ID as the entry in the persistence that shall be updated.
     * @throws PersistenceException if an error occurs while persisting the object.
     */
     void update(Shooting shooting) throws PersistenceException;

    /**
     * Adds the userdefined backgrounds to the given list.
     * @param bgList the list to which the backgrounds shall be added.
     * @throws PersistenceException if a problem occurs while reading the images.
     */
     void getUserBackgrounds(List<Background> bgList) throws PersistenceException;
}
