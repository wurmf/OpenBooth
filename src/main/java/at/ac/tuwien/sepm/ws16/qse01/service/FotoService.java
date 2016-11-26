package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Foto;

/**
 * Interface to implement services for images.
 */
public interface FotoService {
    /**
     * Creates a new foto in database.
     *
     * @param f
     *            the name of foto object
     *
     * @return Foto image which successfully created.
     *
     * */
    public Foto create(Foto f);

    /**
     * Reads a foto by id.
     *
     * @param id
     *            the ID of foto object
     *
     * @return Foto image which successfully founded.
     *
     * */
    public Foto read(int id);

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
}
