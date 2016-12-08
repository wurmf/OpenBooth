package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;


import java.util.List;

/**
 * Interface allows users to create/read an image
 */
public interface ImageDAO {

    /**
     * Creates a new image in database.
     *
     * @param f the name of image object
     * @return Image which successfully created.
     */
    Image create(Image f) throws PersistenceException;

    /**
     * Reads a image by id.
     *
     * @param id the ID of image object
     * @return Image which successfully founded.
     */
     Image read(int id) throws PersistenceException;

    /**
     * Returns the path for last image by shootingid
     *
     * @param shootingid the ID of shooting
     * @return Imagepath of last image as String
     */
    String getLastImgPath(int shootingid) throws PersistenceException;


    /**
     * Returns the next imageID
     *
     *
     * @return int  next image ID
     *
     * */
    int getNextImageID() throws PersistenceException;

    /**
     * deletes given image in database
     *
     * @param imageID ID of image to delete
     * @throws PersistenceException if an error occurs then it throws a PersistenceException
     */
    void delete(int imageID) throws PersistenceException;


    /**
     * gets all images by shooting id
     *
     * @param shootingid the ID of shooting
     *
     * @return List<Image> list of all images belonging to given shooting
     * @throws PersistenceException  if an error occurs then it throws a PersistenceException
     */
    List<Image> getAllImages(int shootingid) throws PersistenceException;
}
