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
     * @param shooting
     *
     */
    public Shooting create(Shooting shooting) throws PersistenceException;

    /**
     *
     * search for active sessions and returns them
     * if null default with inactife gets passed
     *
     * @return Shooting
     *
     */
    public Shooting searchIsActive() throws PersistenceException;

    /**
     * closes activ session
     *
     */
    public void endShooting() throws PersistenceException;
}
