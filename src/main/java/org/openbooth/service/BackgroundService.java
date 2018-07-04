package org.openbooth.service;

import org.openbooth.entities.Background;
import org.openbooth.service.exceptions.ServiceException;

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

    /**
     * get background stored in persistence store
     * @param id - id of the background to be retrieved
     * @return background object with desired id
     * @throws ServiceException if action fails
     */
    Background get(int id) throws ServiceException;

    /**
     * get all backgrounds stored in persistence store
     * @return list of all backgrounds found in persistence store
     * @throws ServiceException if action fails
     */
    List<Background> getAll() throws ServiceException;

    /**
     * get all backgrounds with category of given category id
     * @param id - id of category object
     * @return list of backgrounds with given category id
     * @throws ServiceException if action fails
     */
    List<Background> getAllWithCategory(int id) throws ServiceException;

    /**
     * erase given background from persistence store
     * @param background to be erased
     * @return true, if erasing has been successful , else false
     * @throws ServiceException if action fails
     */
    boolean erase(Background background) throws ServiceException;

    /**
     * add background category to persistence store
     * @param category - non null background category object
     * @return added background category object
     * @throws ServiceException if action fails
     */
    Background.Category addCategory(Background.Category category) throws ServiceException;

    /**
     * edit background category stored in persistence store
     * @param category - non null background category object
     * @throws ServiceException if action fails
     */
    void editCategory(Background.Category category) throws ServiceException;

    /**
     * get background category stored in persistence store
     * @param id - id of the background category to be retrieved
     * @return background object with desired id
     * @throws ServiceException if action fails
     */
    Background.Category getCategory(int id) throws ServiceException;

    /**
     * get all backgrounds categories stored in persistence store
     * @return list of all backgrounds categories found in persistence store
     * @throws ServiceException if action fails
     */
    List<Background.Category> getAllCategories() throws ServiceException;

    /**
     * erase given background category from persistence store
     * @param category to be erased
     * @throws ServiceException if action fails
     */
    void eraseCategory(Background.Category category) throws ServiceException;


    void createPairProfileCategory(int profileID,int categoryID) throws ServiceException;

    void deletePairProfileCategory(int profileID,int categoryID) throws ServiceException;
}
