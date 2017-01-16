package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.util.List;

/**
 * BackgroundService
 */
public interface BackgroundService {

    /**
     * add background to persistence store
     * @param background - non null background object
     * @return added background object
     * @throws ServiceException if action fails
     */
    Background add(Background background) throws ServiceException;

    /**
     * edit background stored in persistence store
     * @param background - non null background object
     * @return true, if edit has been successful, else false
     * @throws ServiceException if action fails
     */
    boolean edit(Background background) throws ServiceException;

    Background get(int id) throws ServiceException;
    List<Background> getAll() throws ServiceException;
    List<Background> getAllWithCategory(int id) throws ServiceException;
    List<Background> getAllforCurrentShootingAvailable() throws ServiceException;
    boolean erase(Background background) throws ServiceException;

    Background.Category addCategory(Background.Category category) throws ServiceException;
    boolean editCategory(Background.Category category) throws ServiceException;
    Background.Category getCategory(int id) throws ServiceException;
    List<Background.Category> getAllCategories() throws ServiceException;
    boolean eraseCategory(Background.Category category) throws ServiceException;
    void createPairProfileCategory(int profileID,int categoryID) throws ServiceException;
    void deletePairProfileCategory(int profileID,int categoryID) throws ServiceException;
}
