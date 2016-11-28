package at.ac.tuwien.sepm.ws16.qse01.dao;

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
    public void add_session(at.ac.tuwien.sepm.ws16.qse01.entities.Session session);

    /**
     *
     * search for active sessions and returns them
     * if null default with inactife gets passed
     *
     * @return Session
     * @autor Aniela
     */
    public at.ac.tuwien.sepm.ws16.qse01.entities.Session search_isactive();

    /**
     * closes activ session
     *
     * @autor Aniela
     */
    public void end_session();
}
