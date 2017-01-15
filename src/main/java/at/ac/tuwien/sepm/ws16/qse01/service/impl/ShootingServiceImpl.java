package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Shooting service impl
 */
@Service
public class ShootingServiceImpl implements ShootingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingServiceImpl.class);
    private ShootingDAO shootingDAO;

    @Autowired
    public ShootingServiceImpl(ShootingDAO jdbcShootingDAO) {
        shootingDAO = jdbcShootingDAO;
    }

    String getImageStorage(){
        return "";
    }

    @Override
    public void addShooting(Shooting shooting) throws ServiceException {
        try {

            shootingDAO.create(shooting);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Shooting searchIsActive() throws ServiceException {

        try {
            return shootingDAO.searchIsActive();
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void endShooting() throws ServiceException {
        try {
            shootingDAO.endShooting();
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateProfile(Shooting shooting) throws ServiceException{
        try {
            shootingDAO.updateProfile(shooting);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public String createPath() throws ServiceException {
        String path=null;
        Path storagepath = null;

        String resource = System.getProperty("user.home");
        storagepath = Paths.get(resource + "/fotostudio/Studio");
        if (storagepath != null) {
            try {
                Files.createDirectories(storagepath);
            }  catch (IOException e) {
                LOGGER.error("createPath - creating initial folder", e);
                throw new ServiceException("Der Speicherort konnte nicht erstellt werden");
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            Date date = new Date();
            Path shootingstorage = Paths.get(storagepath + "/" + dateFormat.format(date));

            try {
                Files.createDirectories(shootingstorage);
            } catch (FileAlreadyExistsException e) {
                LOGGER.info("shooting folder already exists - " , e);
                throw new ServiceException("Der Speicherort konnte nicht neu angelegt werden, da er bereits vorhanden ist ");
            } catch (IOException e) {
                LOGGER.error("creatin shooting folder file - ", e);
                throw new ServiceException("Der Speicherort konnte nicht erstellt werden");
            }
            path = shootingstorage.toString();
        }
        return path;
    }
}