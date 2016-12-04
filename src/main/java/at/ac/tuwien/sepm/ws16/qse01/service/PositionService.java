package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.util.List;

/**
 * PositionService
 */
public interface PositionService {
    /**
     * Add a position
     * @param position - position to be added
     * @return added position with autoassigned id
     * @throws ServiceException if adding position is not possible
     */
    Position add(Position position) throws ServiceException;

    /**
     * Edit a existing position and update its properties
     * @param position - position to be edited
     * @throws ServiceException if editing is not possible
     */
    void edit(Position position) throws ServiceException;

    /**
     * Get a certain position by its id
     * @param id - position id
     * @return position with id
     * @throws ServiceException if getting is not possible
     */
    Position get(int id) throws ServiceException;

    /**
     * Get all positions
     * @return all positions
     * @throws ServiceException if getting all positions is not possible
     */
    List<Position> getAllPositions() throws ServiceException;

    /**
     * Erase a certain position by its id
     * @param position - Position to be erased
     * @throws ServiceException if erasing is not possible
     */
    void erase(Position position) throws ServiceException;
}
