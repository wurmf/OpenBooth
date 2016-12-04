package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;

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
}
