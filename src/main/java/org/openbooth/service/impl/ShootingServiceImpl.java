package org.openbooth.service.impl;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;
import org.openbooth.entities.Shooting;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.dao.ShootingDAO;
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
    public void addUserDefinedBackgrounds(List<Background> bgList) throws ServiceException{
        try {
            shootingDAO.getUserBackgrounds(bgList);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Shooting shooting) throws ServiceException{
        try {
            shootingDAO.update(shooting);
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
                LOGGER.error("createPath - creating initial folder - ", e);
                throw new ServiceException("Der Speicherort konnte nicht erstellt werden", e);
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            Date date = new Date();
            Path shootingstorage = Paths.get(storagepath + "/" + dateFormat.format(date));

            try {
                Files.createDirectories(shootingstorage);
            } catch (FileAlreadyExistsException e) {
                LOGGER.error("shooting folder already exists - " , e);
                throw new ServiceException("Der Speicherort konnte nicht neu angelegt werden, da er bereits vorhanden ist.",e);
            } catch (IOException e) {
                LOGGER.error("creatin shooting folder file - ", e);
                throw new ServiceException("Der Speicherort konnte nicht erstellt werden",e);
            }
            path = shootingstorage.toString();
        }
        return path;
    }
}