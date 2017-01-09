package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.util.List;

/**
 * BackgroundService
 */
public interface BackgroundService {
    Background add(Background background) throws ServiceException;
    boolean edit(Background background) throws ServiceException;
    Background get(int id) throws ServiceException;
    List<Background> getAll() throws ServiceException;
    List<Background> getAllWithCategory(int id) throws ServiceException;
    List<Background> getAllforCurrentShootingAvailable() throws ServiceException;
    boolean erase(Background background) throws ServiceException;

    Background.Category addCategory(Background.Category category) throws ServiceException;
    boolean editCategory(Background.Category category) throws ServiceException;
    List<Background.Category> getCategory(int id) throws ServiceException;
    List<Background.Category> getAllCategories() throws ServiceException;
    boolean eraseCategory(int id) throws ServiceException;
}
