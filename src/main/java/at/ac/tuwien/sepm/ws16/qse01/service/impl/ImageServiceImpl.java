package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class includes services for images.
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    private ImageDAO dao;

    public ImageServiceImpl() throws ServiceException{
        try {
            this.dao = new JDBCImageDAO(H2Handler.getInstance());
        } catch (PersistenceException e) {
            throw new ServiceException("Error: "+e.getMessage());
        }
    }
    @Autowired
    public ImageServiceImpl(ImageDAO imageDAO) throws ServiceException {
        this.dao = imageDAO;
    }

    @Override
    public Image create(Image f) throws ServiceException {
        try{
            LOGGER.debug("Entering create method in Service with parameters {}"+f);
            return dao.create(f);
         } catch (PersistenceException e) {
            throw new ServiceException("Error! Creating in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public Image read(int id) throws ServiceException {
        try {
            LOGGER.debug("Entering read method in Service with image id = "+id);
            return dao.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Reading in service layer has failed.:" + e.getMessage());
        }

    }
    @Override
    public void delete(int imageID) throws ServiceException {
        try {
            LOGGER.debug("Entering delete method in Service with imageID = " + imageID);
            dao.delete(imageID);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Deleting in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public String getLastImgPath(int shootingid) throws ServiceException  {
        try {
            LOGGER.debug("Entering getLastImgPath method in Service with shootingid = " + shootingid);
            return dao.getLastImgPath(shootingid);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getLastImgPath in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public List<Image> getAllImages(int shootingid) throws ServiceException {
        try {
            LOGGER.debug("Entering getAllImages method in Service with shootingid = " + shootingid);
            return dao.getAllImages(shootingid);
        }catch(PersistenceException e){
            throw new ServiceException("Error! Showing all images in service layer has failed.:" + e.getMessage());
        }
    }

    @Override
    public int getNextImageID() throws ServiceException {
        try {
            LOGGER.debug("Entering getNextImageID method in Service ");
            return dao.getNextImageID();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getNextImageId in service layer has failed.:" + e.getMessage());
        }
    }



}
