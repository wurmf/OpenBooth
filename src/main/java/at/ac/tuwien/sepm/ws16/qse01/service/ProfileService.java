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
    Logo addLogo(Logo logo) throws ServiceException;

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
    Logo getLogo(long id) throws ServiceException;

    /**
     * Get all logos
     * @return all logos in a list
     * @throws ServiceException if getting all logos is not possible
     */
    List<Logo> getAllLogos() throws ServiceException;

    /**
     * Erase a given logo by its id
     * @param logo - given logo to be erased
     * @throws ServiceException if erasing is not possible
     */
    boolean eraseLogo(Logo logo) throws ServiceException;

    /**
     * Get all positions of given profile
     * @param profile - given profile
     * @return List<Position> list of positions of given profile
     * if no positions included, list will be just empty, but not null value
     */
    List<Position> getAllPositionsOfProfile(Profile profile) throws ServiceException;

    /**
     * Get all cameras of given profile
     * @param profile - given profile
     * @return List<Camera> list of cameras of active profile
     * if no camera included, list will be just empty, but not null value
     */
    List<Camera> getAllCamerasOfProfile(Profile profile) throws ServiceException;

    /**
     * Get all relative rectangles(sub areas of image where logos are embedded) of given profile
     * @param profile - given profile
     * @return List <RelativeRectangle> list of sub aras of image where logos are embedded
     * if no relative rectangles included, list will be just empty, but not null value
     */
    List <RelativeRectangle> getAllRelativeRectanglesOfProfile(Profile profile)throws ServiceException;

    /**
     * Get all logos used in given profile
     * @param profile - given profile
     * @return List <Logo> list of logos used in given profile
     * if no logos included, list will be just empty, but not null value
     */
    List<Logo> getAllLogosOfProfile(Profile profile)throws ServiceException;

    /**
     * Get position of given camera of given profile
     * @param profile - given profile
     * @param camera - given camera
     * @return Position - position of the given camera
     * if given camera is not included in given profile, null value will be returned
     */
    Position getPositionOfCameraOfProfile(Profile profile,Camera camera)throws ServiceException;

    /**
     * Get camera of given position of given profile
     * @param profile - given profile
     * @param position - given position
     * @return Camera - camera of the given camera
     * if given camera is not included in given profile, null value will be returned
     */
    Camera getCameraOfPositionOfProfile(Profile profile,Position position)throws ServiceException;

    /**
     * Find out, if given position in given profile is greenscreen-ready
     * @param profile - given profile
     * @param position - given position of given profile
     * @return boolean true, if greenscreen-ready
     */
    boolean isPositionGreenScreenReadyPositionOfProfile(Profile profile,Position position) throws ServiceException;

    /**
     * Get logo of given relative rectangle(sub area of image where logos are embedded) of given profile
     * @param relativeRectangle - given relative rectangle(sub area of image where logos are embedded)
     * @return Logo - logo of the given sub area of image where logos are embedded
     * if given logo is not included in given profile, null value will be returned
     */
    Logo getLogoOfRelativeRectangleOfProfile(Profile profile,RelativeRectangle relativeRectangle)throws ServiceException;

    /**
     * Get relative rectangle(sub area of image where logos are embedded) of given logo of given profile
     * @param profile - given profile
     * @param logo - given logo of given profile
     * @return RelativeRectangle - relative rectangle(sub area of image where logos are embedded)
     * if given relative rectangle is not included in given profile, null value will be returned
     */
    RelativeRectangle getRelativeRectangleOfLogoOfProfile(Profile profile,Logo logo)throws ServiceException;

    /**
     * Get water mark of given profile
     * @return Logo - watermark of active profile
     * Water mark is a special logo that covers all the image
     * if no water mark is set in given profile, null value will be returned
     */
    Logo getProfileWaterMark(Profile profile)throws ServiceException;

    /**
     * Get name of given profile
     * @param profile - given profile
     * @return String - name of given profile (max. 50 characters long)
     */
    String getProfileName(Profile profile)throws ServiceException;

    /**
     * Find out, if given profile is print enabled
     * @param profile - given profile
     * @return boolean - true, if enabled
     */
    boolean isProfilePrintEnabled(Profile profile)throws ServiceException;

    /**
     * Find out, if given profile is filter enabled
     * @param profile - given profile
     * @return boolean - true, if enabled
     */
    boolean isProfileFilerEnabled(Profile profile)throws ServiceException;

    /**
     * Find out, if given profile is green screen enabled
     * @param profile - given profile
     * @return boolean - true, if enabled
     */
    boolean isProfileGreenscreenEnabled(Profile profile)throws ServiceException;
}
