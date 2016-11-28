package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Shouting;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

/**
 * Created by Aniela on 23.11.2016.
 */
public interface ShoutingService {

    /**
     *  calls adding in DAO
     *
     * @param shouting
     * @autor Aniela
     */
    public void add_session(Shouting shouting) throws ServiceException;

    /**
     * calls search_isactive in DAO
     * returns value to UI
     *
     * @return session
     * @autor Aniela
     */
    public Shouting search_isactive() throws ServiceException;

    /**
     * calls end_session
     * @autor Aniela
     */
    public void end_session();


}

