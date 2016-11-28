package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shouting;

/**
 * Created by Moatzgeile Sau on 23.11.2016.
 */
public interface ShoutingDAO {
    /**
     *
     * adds new shouting to database
     *
     * @param shouting
     * @autor Aniela
     */
    public void add_session(Shouting shouting) throws PersistenceException;

    /**
     *
     * search for active sessions and returns them
     * if null default with inactife gets passed
     *
     * @return Shouting
     * @autor Aniela
     */
    public Shouting search_isactive() throws PersistenceException;

    /**
     * closes activ session
     *
     * @autor Aniela
     */
    public void end_session();
}
