package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.geometry.Pos;

import javax.sql.rowset.serial.SerialException;
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
    boolean edit(Profile profile) throws ServiceException;

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
    boolean erase(Profile profile) throws ServiceException;

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
    boolean editPosition(Position position) throws ServiceException;

    /**
     * Get a certain position by its id
     * @param id - position id
     * @return position with id
     * @throws ServiceException if getting is not possible
     */
    Position getPosition(long id) throws ServiceException;

    /**
     * Get all positions
     * @return all positions in a list
     * @throws ServiceException if getting all positions is not possible
     */
    List<Position> getAllPositions() throws ServiceException;

    /**
     * Erase a certain position by its id
     * @param position - Position to be erased
     * @throws ServiceException if erasing is not possible
     */
    boolean erasePosition(Position position) throws ServiceException;

    /**
     * Add a logo
     * @param logo - logo to be added
     * @return added logo with autoassigned id
     * @throws ServiceException if adding logo is not possible
     */
    Position addLogo(Logo logo) throws ServiceException;

    /**
     * Edit a existing logo and update its properties
     * @param logo - logo to be edited
     * @throws ServiceException if editing is not possible
     */
    boolean editLogo(Logo logo) throws ServiceException;

    /**
     * Get a certain logo by its id
     * @param id - logo id
     * @return logo object with given id
     * @throws ServiceException if getting is not possible
     */
    Position getLogo(long id) throws ServiceException;

    /**
     * Get all logos
     * @return all logos in a list
     * @throws ServiceException if getting all logos is not possible
     */
    List<Logo> getAllLogos() throws ServiceException;

    /**
     * Erase a certain logo by its id
     * @param logo - Logo to be erased
     * @throws ServiceException if erasing is not possible
     */
    boolean eraseLogo(Logo logo) throws ServiceException;

    /**
     * Set to be edited profile object
     *
     * @param profile profile that is intended to be edited
     * @return true, if setting is possible, otherwise false
     * @throws ServiceException if setting active profile is not possible
     */
    boolean setEditedProfile(Profile profile) throws ServiceException;

    /**
     * Get profile object that is set currently to be edited
     *
     * @return profile object, maybe also be null, if no profile is set to be edited currently
     * @throws ServiceException if getting currently edited profile is not possible
     */
    Profile getEditedProfile() throws ServiceException;

    /**
     * Set active profile object in currently active shooting
     *
     * @param profile profile that is intended to be set active for active shooting
     * @return true, if setting is possible, otherwise false
     * @throws ServiceException if setting active profile is not possible
     */
    boolean setActiveProfile(Profile profile) throws ServiceException;

    /**
     * Get active profile object that is set by ShootingService
     *
     * @return profile object, maybe also be null, if no active profile is set
     * @throws ServiceException if getting active profile is not possible
     */
    Profile getActiveProfile() throws ServiceException;

    /**
     * Get all positions of active profile
     * @return List<Position> list of positions of active profile
     * in case the active profile is not set, returned list is just empty but not null
     */
    List<Position> getAllPositionsOfActiveProfile() throws ServiceException;

    /**
     * Get all cameras of active profile
     * @return List<Camera> list of cameras of active profile
     * in case the active profile is not set, returned list is just empty but not null
     */
    List<Camera> getAllCamerasOfActiveProfile() throws ServiceException;

    /**
     * Get all relative rectangles(sub areas of image where logos are embedded) of active profile
     * @return List <RelativeRectangle> list of sub aras of image where logos are embedded
     * in case the active profile is not set, returned list is just empty but not null
     */
    List <RelativeRectangle> getAllRelativeRectanglesOfActiveProfile()throws ServiceException;

    /**
     * Get all logos used in active profile
     * @return List <Logo> list of logos used in active profile
     * in case the active profile is not set, returned list will be just empty, but not null value
     */
    List<Logo> getAllLogosOfActiveProfile()throws ServiceException;

    /**
     * Get position of given camera of active profile
     * @param camera - given camera of active profile
     * @return Position - position of the given camera
     * if given camera is not included in active profile, null value will be returned
     */
    Position getPositionOfCameraOfActiveProfile(Camera camera)throws ServiceException;

    /**
     * Get camera of given position of  active profile
     * @param position - given position of active profile
     * @return Camera - camera of the given camera
     * if given camera is not included in active profile, null value will be returned
     */
    Camera getCameraOfPositionOfActiveProfile(Position position)throws ServiceException;

    /**
     * Find out, if this position is greenscreen-ready in this active profile
     * @param position - given position of active profile
     * @return boolean
     */
    boolean isGreenScreenReadyPositionOfActiveProfile(Position position) throws ServiceException;

    /**
     * Get logo of given relative rectangle(sub area of image where logos are embedded) of active profile
     * @param relativeRectangle - given relative rectangle(sub area of image where logos are embedded)
     * @return Logo - logo of the given sub area of image where logos are embedded
     * if given logo is not included in active profile, null value will be returned
     */
    Logo getRelativeRectangleOfLogoOfActiveProfile(RelativeRectangle relativeRectangle)throws ServiceException;

    /**
     * Get relative rectangle(sub area of image where logos are embedded) of given logo of active profile
     * @param logo - given logo of active profile
     * @return RelativeRectangle - relative rectangle(sub area of image where logos are embedded)
     * if given relative rectangle is not included in active profile, null value will be returned
     */
    RelativeRectangle getLogoOfRelativeRectangleOfActiveProfile(Logo logo)throws ServiceException;

    /**
     * Get water mark of active profile
     * @return Logo - watermark of active profile
     * Water mark is a special logo that covers all the image
     * if no water mark is set in active profile, null value will be returned
     */
    Logo getWaterMarkOfActiveProfile()throws ServiceException;

    /**
     * Get name of active profile
     * @return String - name of active profile (max. 50 characters long)
     */
    String getNameOfActiveProfile()throws ServiceException;

    /**
     * Find out, if active profile is print enabled
     * @return boolean
     */
    boolean isPrintEnabledActiveProfile()throws ServiceException;

    /**
     * Find out, if active profile is filter enabled
     * @return boolean
     */
    boolean isFilerEnabledActiveProfile()throws ServiceException;

    /**
     * Find out, if active profile is green screen enabled
     * @return boolean
     */
    boolean isGreenscreenEnabledActiveProfile()throws ServiceException;

}
