package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Session;

/**
 * Created by Moatzgeile Sau on 23.11.2016.
 */
public interface SessionDAO {
    /**
     *
     * adds new session to database
     *
     * @param session
     * @autor Aniela
     */
    public void add_session(at.ac.tuwien.sepm.ws16.qse01.entities.Session session) throws PersistenceException;

    /**
     *
     * search for active sessions and returns them
     * if null default with inactife gets passed
     *
     * @return Session
     * @autor Aniela
     */
    public Session search_isactive() throws PersistenceException;

    /**
     * closes activ session
     *
     * @autor Aniela
     */
    public void end_session();
}
