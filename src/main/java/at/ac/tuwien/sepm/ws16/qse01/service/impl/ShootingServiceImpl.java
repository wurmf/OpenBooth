package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShootingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aniela on 23.11.2016.
 */
public class ShootingServiceImpl implements ShootingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingServiceImpl.class);
    ShootingDAO sessionDAO;
    public ShootingServiceImpl(JDBCShootingDAO jdbcShootingDAO) throws Exception {
        sessionDAO = jdbcShootingDAO;
    }
    String getImageStorage(){
        //DAO.getImageStorage();
        String imagePath ="";
        return imagePath;
    }

    public void addShooting(Shooting shouting) throws ServiceException {
        try {

            sessionDAO.create(shouting);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public Shooting searchIsActive() throws ServiceException {

        try {
            return sessionDAO.searchIsActive();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public void endShooting() throws ServiceException {
        try {
            sessionDAO.endShooting();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}