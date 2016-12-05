package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.ImageDAO;
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

    private static ImageDAO dao;

    @Autowired
    public ImageServiceImpl(ImageDAO imageDAO) throws ServiceException {
        this.dao = imageDAO;
    }

    @Override
    public Image create(Image f) {
        LOGGER.debug("Entering create method in Service with parameters {}"+f);

        return dao.create(f);
    }

    @Override
    public Image read(int id) {
        LOGGER.debug("Entering read method in Service with image id = "+id);
        return dao.read(id);
    }

    @Override
    public String getLastImgPath(int shootingid) {
        LOGGER.debug("Entering getLastImgPath method in Service with shootingid = "+shootingid);
        return dao.getLastImgPath(shootingid);
    }
    @Override
    public List<String> getAllImagePaths(int shootingid) {
        LOGGER.debug("Entering getAllImagePaths method in Service with shootingid = "+shootingid);
        return dao.getAllImagePaths(shootingid);
    }

    @Override
    public int getNextImageID() {
        LOGGER.debug("Entering getNextImageID method in Service ");
        return dao.getNextImageID();
    }

}
