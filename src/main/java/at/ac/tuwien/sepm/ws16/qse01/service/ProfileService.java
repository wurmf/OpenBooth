package at.ac.tuwien.sepm.ws16.qse01.service;

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
    Profile get(int id) throws ServiceException;

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
     * Setting active profile(maximal one profile can be active)
     * @param profile profile to be set active
     * @throws ServiceException if setting active profile is not possible
     */
    void setActiveProfile(Profile profile) throws ServiceException;

    /**
     * Getting active profile(maximal one profile can be active)
     * @return profile that is active or null, if no profile is currently active
     * @throws ServiceException if getting active profile is not possible
     */
    Profile getActiveProfile() throws ServiceException;
}
