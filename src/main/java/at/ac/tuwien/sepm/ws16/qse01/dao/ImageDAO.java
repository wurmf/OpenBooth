package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.util.List;

/**
 * Interface allows users to create/read an image
 */
public interface ImageDAO {

    /**
     * Creates a new image in database.
     *
     * @param f
     *            the name of image object
     *
     * @return Image which successfully created.
     *
     * */
    public Image create(Image f);

    /**
     * Reads a image by id.
     *
     * @param id
     *            the ID of image object
     *
     * @return Image which successfully founded.
     *
     * */
    public Image read(int id);

    /**
     * Returns the path for last image by shootingid
     *
     * @param shootingid
     *            the ID of shooting
     *
     * @return Imagepath of last image as String
     *
     * */
    public String getLastImgPath(int shootingid);

    /**
     * Returns a string list including all image paths by shootingid
     *
     * @param shootingid
     *            the ID of shooting
     *
     * @return List<String> list of all imagepaths from given shooting
     *
     * */
    public List<String> getAllImagePaths(int shootingid);

    /**
     * Returns the next imageID
     *
     *
     * @return int  next image ID
     *
     * */
    public int getNextImageID();

    /**
     * deltes image given as parameter
     *
     * @param image
     * @throws ServiceException
     */
    public void delet(Image image) throws PersistenceException;


    /**
     * gets all images by shooting id
     *
     * @param shootingid
     * @return
     * @throws ServiceException
     */
    public List<Image> getAllImages(int shootingid) throws PersistenceException;
}
