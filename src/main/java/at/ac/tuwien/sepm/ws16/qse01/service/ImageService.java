package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.util.List;

/**
 * Interface to implement services for images.
 */
public interface ImageService {
    /**
     * Creates a new foto in database.
     *
     * @param f
     *            the name of foto object
     *
     * @return Image image which successfully created.
     *
     * */
    public Image create(Image f);

    /**
     * Reads a foto by id.
     *
     * @param id
     *            the ID of foto object
     *
     * @return Image image which successfully founded.
     *
     * */
    public Image read(int id);

    /**
     * Returns the path for last image by shootingid
     *
     * @param shootingid
     *            the ID of shooting
     *
     * @return String imagepath of last image
     *
     * */
    public String getLastImgPath(int shootingid);

    /**
     * Returns the next imageID
     *
     *
     * @return int  next image ID
     *
     * */
    public int getNextImageID();

    /**
     * deletes given image in database
     *
     * @param imageID ID of image to delete
     * @throws ServiceException
     */
    public void delete(int imageID) throws ServiceException;


    /**
     * gets all images by shooting id
     *
     * @param shootingid the ID of shooting
     *
     * @return List<Image> list of all images belonging to given shooting
     * @throws ServiceException
     */
    public List<Image> getAllImages(int shootingid) throws ServiceException;


}
