package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

/**
 * Shooting Service
 */
public interface ShootingService {

     void updateProfile();
    /**
     *  calls adding in DAO and gets parameter from Controller
     *
     * @throws ServiceException when ever an Persistent exeption occurs
     * @param shooting new shooting created in the Controller
     */
     void addShooting(Shooting shooting) throws ServiceException;

    /**
     * calls searchIsActive in DAO
     * returns value to UI
     *
     * @throws ServiceException when ever an Persistent exeption occurs
     * @return Shooting returns shooting that is activ from dao
     */
     Shooting searchIsActive() throws ServiceException;

    /**
     * calls endShooting in DAO
     *
     * @throws ServiceException when ever an Persistent exeption occurs
     */
     void endShooting() throws ServiceException;

    /**
     * creates default storage Place in HomeOrdner named Forostudio
     * in this it creats an ordner for mobile or studio and in there a new ordner named with time and date of the started shooting
     * @return the default saving Path
     * @throws ServiceException when ever an IOExeption or an FolderAlreadyExists Exeption ocures to inforem the usere
     */
     String createPath() throws ServiceException;
}

