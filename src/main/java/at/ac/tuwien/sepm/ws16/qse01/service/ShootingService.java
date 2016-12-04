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
     * @param shouting
     */
    public void add_session(Shooting shouting) throws ServiceException;

    /**
     * calls search_isactive in DAO
     * returns value to UI
     *
     * @return session
     */
    public Shooting search_isactive() throws ServiceException;

    /**
     * calls end_session
     */
    public void end_session();

}

