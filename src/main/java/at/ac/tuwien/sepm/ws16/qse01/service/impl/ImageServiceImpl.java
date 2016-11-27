package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.ImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class includes services for images.
 */
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    private static ImageDAO dao;

    public ImageServiceImpl() throws Exception {
        dao = new JDBCImageDAO();
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

}
