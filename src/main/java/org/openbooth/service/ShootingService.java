package org.openbooth.service;

import org.openbooth.entities.Background;
import org.openbooth.entities.Shooting;
import org.openbooth.service.exceptions.ServiceException;

import java.util.List;

/**
 * Shooting Service
 */
public interface ShootingService {

    /**
     * Will tell persistence to update information contained in the entry for the shooting with the same ID as is contained in this Shooting-object.
     * @param shooting a Shooting-object with the same ID as the entry in the persistence-layer that shall be updated.
     * @throws ServiceException when ever a PersistenceException occurs.
     */
     void update(Shooting shooting)throws ServiceException;
    /**
     *  calls adding in DAO and gets parameter from Controller
     *
     * @throws ServiceException when ever a PersistenceException occurs
     * @param shooting new shooting created in the Controller
     */
     void addShooting(Shooting shooting) throws ServiceException;

    /**
     * calls searchIsActive in DAO
     * returns value to UI
     *
     * @throws ServiceException when ever a PersistenceException occurs
     * @return Shooting returns shooting that is activ from dao
     */
     Shooting searchIsActive() throws ServiceException;

    /**
     * calls endShooting in DAO
     *
     * @throws ServiceException when ever a PersistenceException occurs
     */
     void endShooting() throws ServiceException;

    /**
     * Adds the userdefined backgrounds to the given list.
     * @param bgList the list to which the backgrounds shall be added.
     * @throws ServiceException if a problem occurs while reading the images.
     */
     void addUserDefinedBackgrounds(List<Background> bgList) throws ServiceException;

    /**
     * creates default storage Place inside HomeFolder named openbooth
     * in this it creats an ordner for mobile or studio and in there a new ordner named with time and date of the started shooting
     * @return the default saving Path
     * @throws ServiceException when ever an IOException or a FolderAlreadyExists Exception occurs.
     */
     String createPath() throws ServiceException;

}

