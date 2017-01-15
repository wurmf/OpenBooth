package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Shooting Dao
 */
public interface ShootingDAO {
    /**
     *
     * adds new shooting to database
     *
     * @throws PersistenceException caused by using sql
     * @param shooting returnes the shooting thats created
     *
     */
     Shooting create(Shooting shooting) throws PersistenceException;

    /**
     *
     * search for active sessions and returns them
     * if null default with inactife gets passed
     *
     * @throws PersistenceException caused by using sql
     * @return Shooting that is active, if there is no active shooting available it returnes an defaulte false shooting
     */
     Shooting searchIsActive() throws PersistenceException;

    /**
     * closes activ session
     * by setting the session to false
     *
     * @throws PersistenceException caused by using sql
     */
     void endShooting() throws PersistenceException;

    /**
     * Will update data saved for this shooting in the persistence.
     * @param shooting Shooting-object with the same ID as the entry in the persistence that shall be updated.
     * @throws PersistenceException if an error occurs while persisting the object.
     */
     void update(Shooting shooting) throws PersistenceException;

    /**
     * Returns a map that contains all userdefined backgrounds.
     * The keys in the returned map are the paths to the image-files, the values are the corresponding images as BufferedImages
     * @return the map containing the userdefined backgrounds or null if there is no folder defined for backgrounds, the defined folder is empty or no active shooting is available.
     * @throws PersistenceException if an error occurs while reading the pictures.
     */
     Map<String, BufferedImage> getUserBackgrounds() throws PersistenceException;
}
