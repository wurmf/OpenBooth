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
    List<Background> getAllAssociatedWithProfile(int id) throws ServiceException;
    List<Background> getAllAssociatedWithEvent(int id) throws ServiceException;
    List<Background> getAllAssociatedGlobal(int id) throws ServiceException;
    List<Background> getAllforCurrentShootingAvailable() throws ServiceException;
    boolean erase(Background background) throws ServiceException;

    Background add(Background.Event event) throws ServiceException;
    boolean edit(Background.Event event) throws ServiceException;
    List<Background.Event> getEvent(int id) throws ServiceException;
    List<Background.Event> getAllEvents() throws ServiceException;
    boolean eraseEvent(int id) throws ServiceException;
}
