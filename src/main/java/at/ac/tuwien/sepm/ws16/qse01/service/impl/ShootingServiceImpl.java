package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Aniela on 23.11.2016.
 */
@Service
public class ShootingServiceImpl implements ShootingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingServiceImpl.class);
    ShootingDAO shootingDAO;

    @Autowired
    public ShootingServiceImpl(ShootingDAO jdbcShootingDAO) throws Exception {
        shootingDAO = jdbcShootingDAO;
    }
    String getImageStorage(){
        //DAO.getImageStorage();
        String imagePath ="";
        return imagePath;
    }

    public void addShooting(Shooting shooting) throws ServiceException {
        try {

            shootingDAO.create(shooting);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public Shooting searchIsActive() throws ServiceException {

        try {
            return shootingDAO.searchIsActive();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public void endShooting() throws ServiceException {
        try {
            shootingDAO.endShooting();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}