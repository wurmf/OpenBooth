package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.entities.Foto;

/**
 * Created by macdnz on 24.11.16.
 */
public interface FotoDAO {

    /**
     * Creates a new foto in database.
     *
     * @param f
     *            the name of foto object
     *
     * @return Foto which successfully created.
     *
     * */
    public Foto create(Foto f);

    /**
     * Reads a foto by id.
     *
     * @param id
     *            the ID of foto object
     *
     * @return Foto which successfully founded.
     *
     * */
    public Foto read(int id);

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
}
