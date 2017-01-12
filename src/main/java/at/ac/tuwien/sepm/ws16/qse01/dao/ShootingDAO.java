package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;

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
     * updates the shooting profile
     *
     * @param shooting shooting to be updated to
     * @throws PersistenceException
     */
     void updateProfile(Shooting shooting) throws PersistenceException;

}
