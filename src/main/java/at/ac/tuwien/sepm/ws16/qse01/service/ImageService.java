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
     * @param f the name of foto object
     * @return Image image which successfully created.
     * @throws ServiceException if an error occurs then it throws a ServiceException
     * */
    Image create(Image f) throws ServiceException;

    /**
     * Reads a foto by id.
     *
     * @param id the ID of foto object
     * @return Image image which successfully founded.
     * @throws ServiceException if an error occurs then it throws a ServiceException
     * */
    Image read(int id) throws  ServiceException;


    /**
     * Persists change of an existing and already persisted image object
     *
     * @param img - image object with changed properties to be persisted
     * @throws ServiceException if persistence data store can not be accessed
     */
    void update(Image img) throws ServiceException;

    /**
     * Returns the path for last image by shootingid
     *
     * @param shootingid the ID of shooting
     * @return String imagepath of last image
     * @throws ServiceException if an error occurs then it throws a ServiceException
     * */
    String getLastImgPath(int shootingid) throws ServiceException;

    /**
     * Returns the next imageID
     *
     * @return int  next image ID
     * @throws ServiceException if an error occurs then it throws a ServiceException
     *
     * */
    int getNextImageID() throws ServiceException;

    /**
     * deletes given image in database
     *
     * @param imageID ID of image to delete
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    void delete(int imageID) throws ServiceException;


    /**
     * gets all images by shooting id
     *
     * @param shootingid the ID of shooting
     * @return List<Image> list of all images belonging to given shooting
     * @throws ServiceException if an error occurs then it throws a ServiceException
     */
    List<Image> getAllImages(int shootingid) throws ServiceException;




}
