package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.util.List;

/**
 * ProfileServie
 */
public interface ProfileService {
    /**
     * Add a profile
     * @param profile - profile to be added
     * @return added profile with autoassigned id
     * @throws ServiceException if adding profile is not possible
     */
    Profile add(Profile profile) throws ServiceException;

    /**
     * Edit a existing profile and update its properties
     * @param profile - profile to be edited
     * @throws ServiceException if editing is not possible
     */
    void edit(Profile profile) throws ServiceException;

    /**
     * Get a certain profile by its id
     * @param id - profile id
     * @return profile with id
     * @throws ServiceException if getting is not possible
     */
    Profile get(long id) throws ServiceException;

    /**
     * Get all profiles
     * @return all profiles
     * @throws ServiceException if getting all profiles is not possible
     */
    List<Profile> getAllProfiles() throws ServiceException;

    /**
     * Erase a certain profile by its id
     * @param profile - profile to be erased
     * @throws ServiceException if erasing is not possible
     */
    void erase(Profile profile) throws ServiceException;

    /**
     * Add a position
     * @param position - position to be added
     * @return added position with autoassigned id
     * @throws ServiceException if adding position is not possible
     */
    Position addPosition(Position position) throws ServiceException;

    /**
     * Edit a existing position and update its properties
     * @param position - position to be edited
     * @throws ServiceException if editing is not possible
     */
    void editPosition(Position position) throws ServiceException;

    /**
     * Get a certain position by its id
     * @param id - position id
     * @return position with id
     * @throws ServiceException if getting is not possible
     */
    Position getPosition(int id) throws ServiceException;

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
    void erasePosition(Position position) throws ServiceException;
}
