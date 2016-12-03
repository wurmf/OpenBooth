package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

/**
 * Created by Aniela on 23.11.2016.
 */
public interface ShootingService {

    /**
     *  calls adding in DAO
     *
     * @param shooting
     */
    public void addShooting(Shooting shooting) throws ServiceException;

    /**
     * calls searchIsActive in DAO
     * returns value to UI
     *
     * @return Shooting
     */
    public Shooting searchIsActive() throws ServiceException;

    /**
     * calls endShooting
     */
    public void endShooting() throws ServiceException;

}

