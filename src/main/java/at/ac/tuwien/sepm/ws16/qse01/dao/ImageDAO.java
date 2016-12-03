package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;

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
     * Returns the next imageID
     *
     *
     * @return int  next image ID
     *
     * */
    public int getNextImageID();
}
