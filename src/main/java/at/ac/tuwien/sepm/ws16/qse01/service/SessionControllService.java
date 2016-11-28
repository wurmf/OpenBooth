package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.ServiceExeption;
import at.ac.tuwien.sepm.ws16.qse01.entities.Session;

import java.util.List;

/**
 * Created by Aniela on 23.11.2016.
 */
public interface SessionControllService {

    /**
     *  calls adding in DAO
     *
     * @param session
     * @autor Aniela
     */
    public void add_session(Session session) throws ServiceExeption;

    /**
     * calls search_isactive in DAO
     * returns value to UI
     *
     * @return session
     * @autor Aniela
     */
    public Session search_isactive() throws ServiceExeption;

    /**
     * calls end_session
     * @autor Aniela
     */
    public void end_session();


}

