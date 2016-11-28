package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.service.shoutingservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aniela on 23.11.2016.
 */
public class ShootingServiceImpl implements shoutingservice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingServiceImpl.class);
    ShootingDAO sessionDAO;
    public ShootingServiceImpl() throws Exception {
        sessionDAO = new JDBCShootingDAO();
    }
    String getImageStorage(){
        //DAO.getImageStorage();
        String imagePath ="";
        return imagePath;
    }


    public void add_session(Shooting shouting) throws ServiceException {
        try {
            sessionDAO.add_session(shouting);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Shooting search_isactive() throws ServiceException {

        try {
            return sessionDAO.search_isactive();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public void end_session() {
        sessionDAO.end_session();
    }
}