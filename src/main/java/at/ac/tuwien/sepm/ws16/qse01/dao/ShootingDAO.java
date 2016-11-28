package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;

/**
 * Created by Moatzgeile Sau on 23.11.2016.
 */
public interface ShootingDAO {
    /**
     *
     * adds new shouting to database
     *
     * @param shouting
     * @autor Aniela
     */
    public void add_session(Shooting shouting) throws PersistenceException;

    /**
     *
     * search for active sessions and returns them
     * if null default with inactife gets passed
     *
     * @return Shooting
     * @autor Aniela
     */
    public Shooting search_isactive() throws PersistenceException;

    /**
     * closes activ session
     *
     * @autor Aniela
     */
    public void end_session();
}
