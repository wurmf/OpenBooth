package org.openbooth.service.impl;

import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.dao.ImageDAO;
import org.openbooth.dao.ShootingDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Image;
import org.openbooth.entities.Shooting;
import org.openbooth.service.FilterService;
import org.openbooth.service.ImageService;
import org.openbooth.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Class includes services for images.
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    private ImageDAO dao;
    private ShootingDAO shootingDAO;
    private String storageDir;
    private Shooting activeShooting;
    private ImageHandler imageHandler;
    private FilterService filterService;

    @Autowired
    public ImageServiceImpl(ImageDAO imageDAO, ShootingDAO shootingDAO, ImageHandler imageHandler, FilterService filterService) throws ServiceException {
        this.dao = imageDAO;
        this.shootingDAO = shootingDAO;
        this.imageHandler = imageHandler;
        this.filterService = filterService;


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
    public void update(Image img) throws ServiceException {
        try {
            LOGGER.debug("Entering update method in Service with image = "+img.toString());
            dao.update(img);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Updating in service layer has failed.:" + e.getMessage());
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

    @Override
    public Image crop(Image original,String filterName, int x1, int x2, int y1, int y2, int maxX, int maxY) throws ServiceException {
        try {
            BufferedImage bufOriginal = filterService.filter(filterName, original.getImagepath());

            int width = bufOriginal.getWidth();
            int height = bufOriginal.getHeight();
            double ratioX = width / (double) maxX;
            double ratioY = height / (double) maxY;
            int newX1 = (int) (x1 * ratioX);
            int newX2 = (int) (x2 * ratioX);

            int newY1 = (int) (y1 * ratioY);
            int newY2 = (int) (y2 * ratioY);


            BufferedImage bufCropped = bufOriginal.getSubimage(newX1, newY1, newX2 - newX1, newY2 - newY1);
            Image img = new Image(-1, original.getImagepath().substring(0, original.getImagepath().length() - 4) + "_crop.jpg", original.getShootingid(), original.getDate());
            Image image = dao.create(img); // bufCropped);
            imageHandler.saveImage(bufCropped, img.getImagepath().replaceAll("//", "/"));
            return image;
        } catch (ImageHandlingException e) {
            LOGGER.error("crop->saveImage: ", e);
            throw new ServiceException(e);
        } catch (PersistenceException e) {
            LOGGER.error("crop->saveImage in DB: ", e);
            throw new ServiceException(e);
        }
    }

}
