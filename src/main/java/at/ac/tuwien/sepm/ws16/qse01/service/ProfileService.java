package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.geometry.Pos;

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
    Position getPosition(int id) throws ServiceException;

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
    Logo getLogo(int id) throws ServiceException;

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
     * Get a certain camera by its id
     * @param id - camera id
     * @return camera object with given id
     * @throws ServiceException if getting is not possible
     */
    Camera getCamera(int id) throws ServiceException;

    /**
     * Get all positions of given profile
     * @param profile - given profile
     * @return List<Position> list of positions of given profile
     * if no positions included, list will be just empty, but not null value
     */
    List<Position> getAllPositionsOfProfile(Profile profile) throws ServiceException;

    /**
     * Get all positions of active profile
     * @return List<Position> list of positions of active profile
     * if no positions included, list will be just empty, but not null value
     */
    List<Position> getAllPositionsOfProfile() throws ServiceException;

    /**
     * Get all cameras of given profile
     * @param profile - given profile
     * @return List<Camera> list of cameras of active profile
     * if no camera included, list will be just empty, but not null value
     */
    List<Camera> getAllCamerasOfProfile(Profile profile) throws ServiceException;

    /**
     * Get all cameras of active profile
     * @return List<Camera> list of cameras of active profile
     * if no camera included, list will be just empty, but not null value
     */
    List<Camera> getAllCamerasOfProfile() throws ServiceException;

    /**
     * Get all relative rectangles(sub areas of image where logos are embedded) of given profile
     * @param profile - given profile
     * @return List <RelativeRectangle> list of sub aras of image where logos are embedded
     * if no relative rectangles included, list will be just empty, but not null value
     */
    List <RelativeRectangle> getAllRelativeRectanglesOfProfile(Profile profile)throws ServiceException;

    /**
     * Get all relative rectangles(sub areas of image where logos are embedded) of active profile
     * @return List <RelativeRectangle> list of sub aras of image where logos are embedded
     * if no relative rectangles included, list will be just empty, but not null value
     */
    List <RelativeRectangle> getAllRelativeRectanglesOfProfile()throws ServiceException;

    /**
     * Get all logos used in given profile
     * @param profile - given profile
     * @return List <Logo> list of logos used in given profile
     * if no logos included, list will be just empty, but not null value
     */
    List<Logo> getAllLogosOfProfile(Profile profile)throws ServiceException;

    /**
     * Get all logos used in active profile
     * @return List <Logo> list of logos used in active profile
     * if no logos included, list will be just empty, but not null value
     */
    List<Logo> getAllLogosOfProfile()throws ServiceException;

    /**
     * Get position of given camera of given profile
     * @param profile - given profile
     * @param camera - given camera
     * @return Position - position of the given camera
     * if given camera is not included in given profile, null value will be returned
     */
    Position getPositionOfCameraOfProfile(Profile profile,Camera camera)throws ServiceException;

    /**
     * Get all logos used in active profile
     * @return List <Logo> list of logos used in active profile
     * if no logos included, list will be just empty, but not null value
     */
    Position getPositionOfCameraOfProfile(Camera camera)throws ServiceException;

    /**
     * Get camera of given position of given profile
     * @param profile - given profile
     * @param position - given position
     * @return Camera - camera of the given position
     * if given camera is not included in given profile, null value will be returned
     */
    Camera getCameraOfPositionOfProfile(Profile profile,Position position)throws ServiceException;


    /**
     * Get camera of given position of active profile
     * @param position - given position
     * @return Camera - camera of the given position
     * if given camera is not included in active profile, null value will be returned
     */
    Camera getCameraOfPositionOfProfile(Position position)throws ServiceException;

    /**
     * Find out, if given position in given profile is greenscreen-ready
     * @param profile - given profile
     * @param position - given position of given profile
     * @return boolean true, if greenscreen-ready
     */
    boolean isPositionGreenScreenReadyPositionOfProfile(Profile profile,Position position) throws ServiceException;

    /**
     * Find out, if given position in active profile is greenscreen-ready
     * @param position - given position of active profile
     * @return boolean true, if greenscreen-ready
     */
    boolean isPositionGreenScreenReadyPositionOfProfile(Position position) throws ServiceException;

    /**
     * Get logo of given relative rectangle(sub area of image where logos are embedded) of given profile
     * @param profile - given profile
     * @param relativeRectangle - given relative rectangle(sub area of image where logos are embedded)
     * @return Logo - logo of the given sub area of image where logos are embedded
     * if given logo is not included in given profile, null value will be returned
     */
    Logo getLogoOfRelativeRectangleOfProfile(Profile profile,RelativeRectangle relativeRectangle)throws ServiceException;

    /**
     * Get logo of given relative rectangle(sub area of image where logos are embedded) of active profile
     * @param relativeRectangle - given relative rectangle(sub area of image where logos are embedded)
     * @return Logo - logo of the given sub area of image where logos are embedded
     * if given logo is not included in active profile, null value will be returned
     */
    Logo getLogoOfRelativeRectangleOfProfile(RelativeRectangle relativeRectangle)throws ServiceException;

    /**
     * Get relative rectangle(sub area of image where logos are embedded) of given logo of given profile
     * @param profile - given profile
     * @param logo - given logo of given profile
     * @return RelativeRectangle - relative rectangle(sub area of image where logos are embedded)
     * if given relative rectangle is not included in given profile, null value will be returned
     */
    RelativeRectangle getRelativeRectangleOfLogoOfProfile(Profile profile,Logo logo)throws ServiceException;

    /**
     * Get relative rectangle(sub area of image where logos are embedded) of given logo of active profile
     * @param logo - given logo of given profile
     * @return RelativeRectangle - relative rectangle(sub area of image where logos are embedded)
     * if given relative rectangle is not included in active profile, null value will be returned
     */
    RelativeRectangle getRelativeRectangleOfLogoOfProfile(Logo logo)throws ServiceException;

    /**
     * Get water mark of given profile
     * @param profile - given profile
     * @return Logo - watermark of active profile
     * Water mark is a special logo that covers all the image
     * if no water mark is set in given profile, null value will be returned
     */
    Logo getProfileWaterMark(Profile profile)throws ServiceException;

    /**
     * Get water mark of active profile
     * @return Logo - watermark of active profile
     * Water mark is a special logo that covers all the image
     * if no water mark is set in given profile, null value will be returned
     */
    Logo getProfileWaterMark()throws ServiceException;

    /**
     * Get name of given profile
     * @param profile - given profile
     * @return String - name of given profile (max. 50 characters long)
     */
    String getProfileName(Profile profile)throws ServiceException;

    /**
     * Get name of active profile
     * @return String - name of active profile (max. 50 characters long)
     */
    String getProfileName()throws ServiceException;

    /**
     * Find out, if given profile is print enabled
     * @param profile - given profile
     * @return boolean - true, if enabled
     */
    boolean isProfilePrintEnabled(Profile profile)throws ServiceException;

    /**
     * Find out, if active profile is print enabled
     * @return boolean - true, if enabled
     */
    boolean isProfilePrintEnabled()throws ServiceException;

    /**
     * Find out, if given profile is filter enabled
     * @param profile - given profile
     * @return boolean - true, if enabled
     */
    boolean isProfileFilerEnabled(Profile profile)throws ServiceException;

    /**
     * Find out, if active profile is filter enabled
     * @return boolean - true, if enabled
     */
    boolean isProfileFilerEnabled()throws ServiceException;

    /**
     * Find out, if given profile is green screen enabled
     * @param profile - given profile
     * @return boolean - true, if enabled
     */
    boolean isProfileGreenscreenEnabled(Profile profile)throws ServiceException;

    /**
     * Find out, if active profile is green screen enabled
     * @return boolean - true, if enabled
     */
    boolean isProfileGreenscreenEnabled()throws ServiceException;

    /**
     * Get active Profile from ShootingService
     * @return Profile - active Profile
     */
    Profile getActiveProfile() throws ServiceException;

    void setActiveProfile(int id) throws ServiceException;


    /**
     * Get all Camera-position pairs of given profile
     * @param profileId - id of given profile
     * @return List<Profile.PairCameraPosition> - List of all Camera-position pairs of given profile
     * may return an empty list, but never a null
     */
    List<Profile.PairCameraPosition> getAllPairCameraPositionOfProfile(int profileId) throws ServiceException;

    /**
     * Get all Camera-position pairs of currently active profile
     * @return List<Profile.PairCameraPosition> - List of all Camera-position pairs of currently profile
     * may return an empty list, but never a null
     */
    List<Profile.PairCameraPosition> getAllPairCameraPositionOfProfile() throws ServiceException;

    /**
     * Add a combination of camera and position to a given profile(and also tell, if it is greeenscreenready
     * @param profileId - id of a profile where to add this combination
     * @param cameraId - id of camera that has to be part of this combination
     * @param positionId - id of position that has to be paer of this combination
     * @param isGreenScreenReady - true, if this combination has to be greenscreenready
     * @return Profile.PairCameraPosition object that has been generated and added to Profile
     */
    Profile.PairCameraPosition addPairCameraPosition(int profileId, int cameraId, int positionId, boolean isGreenScreenReady)
            throws ServiceException;

    /**
     * Add a combination of camera and position of currently active profile(and also tell, if it is greeenscreenready
     * @param cameraId - id of camera that has to be part of this combination
     * @param positionId - id of position that has to be paer of this combination
     * @param isGreenScreenReady - true, if this combination has to be greenscreenready
     * @return Profile.PairCameraPosition object that has been generated and added to Profile
     */
    Profile.PairCameraPosition addPairCameraPosition(int cameraId, int positionId, boolean isGreenScreenReady)
            throws ServiceException;

    /**
     * Edit given pairCameraPosition object
     * (Note that ProfileId is encapsuled in the object and does not need to be separatily taken care of)
     * @param pairCameraPosition - PairCameraPosition object to be edited
     * @param newCameraId - id of Camera object to replace current one
     *        if id is negative or zero, previous already assigned object will be kept,
     *        if positive id does not exist, previous already assigned object will be kept
     * @param newPositionId - id of position object to replace current one
     *        if id is negative or zero, previous already assigned object will be kept,
     *        if positive id does not exist, previous already assigned object will be kept
     * @param invertIsGreenScreenReadySwitch - true, if you want to invert current greenscreenready setting
     *        false, if you want to keep current greenscreenready setting unchanged
     * @return true, if editing of given pairCameraPosition object has been successful
     */
    boolean editPairCameraPosition(Profile.PairCameraPosition pairCameraPosition, int newCameraId, int newPositionId, boolean invertIsGreenScreenReadySwitch)
            throws ServiceException;

    /**
     * Edit given pairCameraPosition object
     * (Note that ProfileId is encapsuled in the object and does not need to be separatily taken care of)
     * @param pairCameraPosition - PairCameraPosition object to exchange current object
     * @return true, if editing of given pairCameraPosition object has been successful
     */
    boolean editPairCameraPosition(Profile.PairCameraPosition pairCameraPosition)
            throws ServiceException;

    /**
     * Erase given paraCameraPostion
     * @param pairCameraPosition - given PairCameraPosition object
     * @return true, if erasing has been successful
     */
    boolean erasePairCameraPosition(Profile.PairCameraPosition pairCameraPosition)
            throws ServiceException;

    /**
     * Get all logo-relative rectangle pairs of given profile
     * @param profileId - id of given profile
     * @return List<Profile.PairLogoRelativeRectangle> - List of all logo-relative rectangle pairs of given profile
     * may return an empty list, but never a null
     */
    List<Profile.PairLogoRelativeRectangle> getAllPairLogoRelativeRectangle(int profileId)
            throws ServiceException;

    /**
     * Get all logo-relative rectangle pairs of currently active profile
     * @return List<Profile.PairLogoRelativeRectangle> - List of all logo-relative rectangle pairs of given profile
     * may return an empty list, but never a null
     */
    List<Profile.PairLogoRelativeRectangle> getAllPairLogoRelativeRectangle()
            throws ServiceException;

    /**
     * Add a combination of logo and its (relative rectangle) position to a given profile
     * @param logoId - id of logo that has to be part of this combination
     * @param relativeRectangle RelativeRectangle object that has to be part of this combination
     * @return Profile.PairLogoRelativeRectangle object that has been generated and added to Profile
     */
    Profile.PairLogoRelativeRectangle addPairLogoRelativeRectangle(int profileId, int logoId, RelativeRectangle relativeRectangle)
            throws ServiceException;

    /**
     * Add a combination of logo and its (relative rectangle) position to currently active profile
     * @param logoId - id of logo that has to be part of this combination
     * @param relativeRectangle RelativeRectangle object that has to be part of this combination
     * @return Profile.PairLogoRelativeRectangle object that has been generated and added to Profile
     */
    Profile.PairLogoRelativeRectangle addPairLogoRelativeRectangle(int logoId, RelativeRectangle relativeRectangle)
            throws ServiceException;

    /**
     * Edit given pairLogoRelativeRectangle object
     * (Note that ProfileId is encapsuled in the object and does not need to be separatily taken care of)
     * @param pairLogoRelativeRectangle - PairLogoRelativeRectangle object to be edited
     * @param newLogoId - id of Logo object to replace current one
     *        if id is negative or zero, previous already assigned object will be kept,
     *        if positive id does not exist, previous already assigned object will be kept
     * @param newRelativeRectangle - RelativeRectangle object to set new position of the paired logo object
     *        if an attribute of the newRelativeRectangle object is set to a negative value, the current value
     *        will stay unchanged
     *        if an attribute of the newRelativeRectangle object is set to a zero or positive value, the current value
     *        will be replaced by it
     * @return true, if editing of given pairLogoRelativeRectangle object has been successful
     */
    boolean editPairLogoRelativeRectangle(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle, int newLogoId, RelativeRectangle newRelativeRectangle)
            throws ServiceException;

    /**
     * Edit given pairLogoRelativeRectangle object
     * (Note that ProfileId is encapsuled in the object and does not need to be separatily taken care of)
     * @param pairLogoRelativeRectangle - PairLogoRelativeRectangle object to exchange current object
     * @return true, if editing of given pairLogoRelativeRectangle object has been successful
     */
    boolean editPairLogoRelativeRectangle(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle)
            throws ServiceException;

    /**
     * Erase given pairLogoRelativeRectangle
     * @param pairLogoRelativeRectangle - given PairLogoRelativeRectangle object
     * @return true, if erasing has been successful
     */
    boolean erasePairLogoRelativeRectangle(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle)
            throws ServiceException;

    /**
     * Add background category to given profile
     * @param profileId - id to identify given profile
     * @param backgroundCategory - Background category to be added to profile
     * @return added background category
     * @throws ServiceException if this action can't be completed
     */
    Background.Category addBackgroundCategoryToProfile(int profileId, Background.Category backgroundCategory) throws ServiceException;

    /**
     * Erase background category from given profile
     * @param profileId - id to identify given profile
     * @param backgroundCategory - Background category to be erased from profile
     * @return true, if action has been successful
     * @throws ServiceException if this action can't be completed
     */
    boolean eraseBackgroundCategoryFromProfile(int profileId, Background.Category backgroundCategory) throws ServiceException;

    /**
     * Add background category to active profile
     * @param backgroundCategory - Background category to be added active profile
     * @return added background category
     * @throws ServiceException if this action can't be completed
     */
    Background.Category addBackgroundCategoryToProfile(Background.Category backgroundCategory) throws ServiceException;

    /**
     * Erase background category from active profile
     * @param backgroundCategory - Background category to be erased from given profile
     * @return true, if action has been successful
     * @throws ServiceException if this action can't be completed
     */
    boolean eraseBackgroundCategoryFromProfile(Background.Category backgroundCategory) throws ServiceException;

    /**
     * Get pairCameraPosition object that contains given camera object in active profile
     * @param camera - given camera object
     * @return pairCameraPosition object that contains given camera
     * @throws ServiceException if this action can't be completed
     */
    Profile.PairCameraPosition getPairCameraPosition(Camera camera)throws ServiceException;

    /**
     * Get all background categories added to given profile
     * @param profileId - id of given profile
     * @return list of all added categories
     */
    List<Background.Category> getAllCategoryOfProfile(int profileId) throws ServiceException;

    /**
     * Get all background categories added to active profile
     * @return list of all added categories
     */
    List<Background.Category> getAllCategoryOfProfile() throws ServiceException;

    /**
     * Get all backgrounds available of given profile
     * @param profileId - id of given profile
     * @return list of all available backgrounds
     */
    List<Background> getAllBackgroundOfProfile(int profileId) throws ServiceException;

    /**
     * Get all backgrounds available of active profile
     * @return list of all available backgrounds
     */
    List<Background> getAllBackgroundOfProfile() throws ServiceException;
}
