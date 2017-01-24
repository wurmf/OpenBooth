package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.*;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Profile Service Implementation
 */
@Service
public class ProfileServiceImpl implements ProfileService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);
    @Resource
    private ProfileDAO profileDAO;
    @Resource
    private PositionDAO positionDAO;
    @Resource
    private LogoDAO logoDAO;
    @Resource
    private CameraDAO cameraDAO;
    @Resource
    private ShootingService shootingService;
    @Resource
    private BackgroundDAO backgroundDAO;
    @Resource
    private BackgroundCategoryDAO backgroundCategoryDAO;
    @Resource
    private BackgroundService backgroundService;
    @Resource
    private CameraService cameraService;

    private List<Profile> profileList = new ArrayList<>();
    private List<Position> positionList = new ArrayList<>();
    private List<Logo> logoList = new ArrayList<>();
    private List<Camera> cameraList = new ArrayList<>();
    private Profile activeProfile;
    @Autowired
    public ProfileServiceImpl(ProfileDAO profileDAO,
                              PositionDAO positionDAO,
                              LogoDAO logoDAO,
                              CameraDAO cameraDAO,
                              ShootingService shootingService,
                              CameraService cameraService
    ) throws ServiceException {
        this.profileDAO = profileDAO;
        this.positionDAO = positionDAO;
        this.logoDAO = logoDAO;
        this.cameraDAO = cameraDAO;
        this.shootingService = shootingService;
        this.cameraService = cameraService;
        this.setActiveProfile(1);

        try {
            profileList.addAll(profileDAO.readAll());
            positionList.addAll(positionDAO.readAll());
            logoList.addAll(logoDAO.readAll());
            cameraList.addAll(cameraDAO.readActive());
        }
        catch (PersistenceException e) {
            throw new ServiceException("Error! Initializing service layer has failed.:" + e);
        }
    }

    @Override
    public Profile add(Profile profile) throws ServiceException {
        LOGGER.debug("Entering addPosition method with parameters " + profile);
        try {
            Profile p = profileDAO.create(profile);
            profileList.add(p);
            return p;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Adding in service layer has failed.:" + e);
        }
    }

    @Override
    public boolean edit(Profile profile) throws ServiceException {
        LOGGER.debug("Entering editPosition method with parameters " + profile);
        try {
            boolean returnvalue = profileDAO.update(profile);
            this.profileList = profileDAO.readAll();
            return returnvalue;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Editing in service layer has failed.:" + e);
        }
    }

    @Override
    public Profile get(int id) throws ServiceException {
        LOGGER.debug("Entering get method with parameter id = " + id);
        try {
            return profileDAO.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting in service layer has failed.:" + e);
        }
    }

    @Override
    public List<Profile> getAllProfiles() throws ServiceException {
        LOGGER.debug("Entering getAllProfiles method");
        try {
            this.profileList = profileDAO.readAll();
            return this.profileList;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting All in service layer has failed.:" + e);
        }
    }

    @Override
    public boolean erase(Profile profile) throws ServiceException {
        LOGGER.debug("Entering erase method with parameters " + profile);
        try {
            boolean returnvalue = profileDAO.delete(profile);
            this.profileList = profileDAO.readAll();
            return returnvalue;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Erasing in service layer has failed.:" + e);
        }
    }

    @Override
    public Position addPosition(Position position) throws ServiceException {
        LOGGER.debug("Entering addPosition method with parameters " + position);
        try {
            Position p = positionDAO.create(position);
            positionList.add(p);
            return p;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Adding object in service layer has failed.:" + e);
        }
    }

    @Override
    public boolean editPosition(Position position) throws ServiceException {
        LOGGER.debug("Entering editPosition method with parameters " + position);
        try {
            boolean returnvalue = positionDAO.update(position);
            this.positionList = positionDAO.readAll();
            return returnvalue;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Editing object in service layer has failed.:" + e);
        }
    }

    @Override
    public Position getPosition(int id) throws ServiceException {
        LOGGER.debug("Entering get method with parameter id = " + id);
        try {
            return positionDAO.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting object in service layer has failed.:" + e);
        }
    }

    @Override
    public List<Position> getAllPositions() throws ServiceException {
        LOGGER.debug("Entering getAllPositions method");
        try {
            this.positionList = positionDAO.readAll();
            return this.positionList;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting all objects in service layer has failed.:" + e);
        }
    }

    @Override
    public boolean erasePosition(Position position) throws ServiceException {
        LOGGER.debug("Entering erase method with parameters " + position);
        try {
            boolean returnvalue = positionDAO.delete(position);
            this.positionList = positionDAO.readAll();
            return returnvalue;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Erasing object in service layer has failed.:" + e);
        }
    }

    @Override
    public Logo addLogo(Logo logo) throws ServiceException {
        LOGGER.debug("Entering addLogo method with parameters " + logo);
        try {
            Logo returnvalue = logoDAO.create(logo);
            logoList.add(returnvalue);
            return returnvalue;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Adding object in service layer has failed.:" + e);
        }
    }

    @Override
    public boolean editLogo(Logo logo) throws ServiceException {
        LOGGER.debug("Entering editLogo method with parameters " + logo);
        try {
            boolean returnvalue = logoDAO.update(logo);
            this.logoList = logoDAO.readAll();
            return returnvalue;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Editing object in service layer has failed.:" + e);
        }
    }

    @Override
    public Logo getLogo(int id) throws ServiceException {
        LOGGER.debug("Entering get method with parameter id = " + id);
        try {
            return logoDAO.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting object in service layer has failed.:" + e);
        }
    }

    @Override
    public List<Logo> getAllLogos() throws ServiceException {
        LOGGER.debug("Entering getAllLogos method");
        try {
            this.logoList = logoDAO.readAll();
            return this.logoList;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting all objects in service layer has failed.:" + e);
        }
    }

    @Override
    public boolean eraseLogo(Logo logo) throws ServiceException {
        LOGGER.debug("Entering erase method with parameters " + logo);
        try {
            boolean returnvalue = logoDAO.delete(logo);
            this.logoList = logoDAO.readAll();
            return returnvalue;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Erasing object in service layer has failed.:" + e);
        }
    }

    @Override
    public Camera getCamera(int id) throws ServiceException {
        LOGGER.debug("Entering get method with parameter id = " + id);
        try {
            return cameraDAO.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting object in service layer has failed.:" + e);
        }
    }

    @Override
    public List<Position> getAllPositionsOfProfile(Profile profile) throws ServiceException {
        List<Position> returnvalue = new ArrayList<>();
        for(Profile.PairCameraPosition pairCameraPosition : profile.getPairCameraPositions()) {
            returnvalue.add(pairCameraPosition.getPosition());
        }
        return returnvalue;
    }

    @Override
    public List<Camera> getAllCamerasOfProfile(Profile profile) throws ServiceException {
        List<Camera> returnvalue = new ArrayList<>();
        for(Profile.PairCameraPosition pairCameraPosition : profile.getPairCameraPositions()) {
            returnvalue.add(pairCameraPosition.getCamera());
        }
        return returnvalue;
    }

    @Override
    public List<RelativeRectangle> getAllRelativeRectanglesOfProfile(Profile profile) throws ServiceException {
        List<RelativeRectangle> returnvalue = new ArrayList<>();
        for(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : profile.getPairLogoRelativeRectangles()) {
            returnvalue.add(pairLogoRelativeRectangle.getRelativeRectangle());
        }
        return returnvalue;
    }

    @Override
    public List<Logo> getAllLogosOfProfile(Profile profile) throws ServiceException {
        List<Logo> returnvalue = new ArrayList<>();
        for(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : profile.getPairLogoRelativeRectangles()) {
            returnvalue.add(pairLogoRelativeRectangle.getLogo());
        }
        return returnvalue;
    }

    @Override
    public Position getPositionOfCameraOfProfile(Profile profile, Camera camera) throws ServiceException {
        for(Profile.PairCameraPosition pairCameraPosition : profile.getPairCameraPositions()) {
            if(pairCameraPosition.getCamera().getId()==camera.getId())
                return pairCameraPosition.getPosition();
        }
        return null;
    }

    @Override
    public Camera getCameraOfPositionOfProfile(Profile profile, Position position) throws ServiceException {
        for(Profile.PairCameraPosition pairCameraPosition : profile.getPairCameraPositions()) {
            if(pairCameraPosition.getPosition().getId()==position.getId())
                return pairCameraPosition.getCamera();
        }
        return null;
    }

    @Override
    public boolean isPositionGreenScreenReadyPositionOfProfile(Profile profile, Position position) throws ServiceException {
        for(Profile.PairCameraPosition pairCameraPosition : profile.getPairCameraPositions()) {
            if(pairCameraPosition.getPosition().getId()==position.getId())
                return pairCameraPosition.isGreenScreenReady();
        }
        return false;
    }

    @Override
    public Logo getLogoOfRelativeRectangleOfProfile(Profile profile, RelativeRectangle relativeRectangle) throws ServiceException {
        for(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : profile.getPairLogoRelativeRectangles()) {
            if(pairLogoRelativeRectangle.getRelativeRectangle().equals(relativeRectangle))
                return pairLogoRelativeRectangle.getLogo();
        }
        return null;
    }

    @Override
    public RelativeRectangle getRelativeRectangleOfLogoOfProfile(Profile profile, Logo logo) throws ServiceException {
        for(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : profile.getPairLogoRelativeRectangles()) {
            if(pairLogoRelativeRectangle.getLogo().getId()==logo.getId())
                return pairLogoRelativeRectangle.getRelativeRectangle();
        }
        return null;
    }

    @Override
    public Logo getProfileWaterMark(Profile profile) throws ServiceException {
        for(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : profile.getPairLogoRelativeRectangles()) {
            RelativeRectangle relativeRectangle = pairLogoRelativeRectangle.getRelativeRectangle();
            if (relativeRectangle.getX() == -1
                    && relativeRectangle.getY() == -1
                    && relativeRectangle.getWidth() == -1
                    && relativeRectangle.getHeight() == -1
                    )
                return pairLogoRelativeRectangle.getLogo();
        }
        return null;
    }

    @Override
    public String getProfileName(Profile profile) throws ServiceException {
        return profile.getName();
    }

    @Override
    public boolean isProfilePrintEnabled(Profile profile) throws ServiceException {
        return profile.isPrintEnabled();
    }

    @Override
    public boolean isProfileFilerEnabled(Profile profile) throws ServiceException {
        return profile.isFilerEnabled();
    }

    @Override
    public boolean isProfileGreenscreenEnabled(Profile profile) throws ServiceException {
        return profile.isGreenscreenEnabled();
    }

    @Override
    public Profile getActiveProfile() throws ServiceException {
        Shooting activeShooting = this.shootingService.searchIsActive();

        if (activeShooting != null && this.activeProfile != null)
            {
                if(activeShooting.getProfileid() == this.activeProfile.getId())
                    {return this.activeProfile;}
                else
                    {return this.get(activeShooting.getProfileid());}
            }
        else if (activeShooting != null && this.activeProfile == null)
            {return this.get(activeShooting.getProfileid());}
        else
            {return this.activeProfile;}
    }

    @Override
    public void setActiveProfile(int id) throws ServiceException {
        Shooting activeShooting = this.shootingService.searchIsActive();
        if(activeShooting!=null)
            {this.activeProfile = get(activeShooting.getProfileid());}
        else
            {this.activeProfile = this.get(id);}
    }

    @Override
    public List<Position> getAllPositionsOfProfile() throws ServiceException {
        return getAllPositionsOfProfile(this.getActiveProfile());
    }

    @Override
    public List<Camera> getAllCamerasOfProfile() throws ServiceException {
        return getAllCamerasOfProfile(this.getActiveProfile());
    }

    @Override
    public List<RelativeRectangle> getAllRelativeRectanglesOfProfile() throws ServiceException {
        return getAllRelativeRectanglesOfProfile(this.getActiveProfile());
    }

    @Override
    public List<Logo> getAllLogosOfProfile() throws ServiceException {
        return getAllLogosOfProfile(this.getActiveProfile());
    }

    @Override
    public Position getPositionOfCameraOfProfile(Camera camera) throws ServiceException {
        return getPositionOfCameraOfProfile(this.getActiveProfile(),camera);
    }

    @Override
    public Camera getCameraOfPositionOfProfile(Position position) throws ServiceException {
        return getCameraOfPositionOfProfile(this.getActiveProfile(),position);
    }

    @Override
    public boolean isPositionGreenScreenReadyPositionOfProfile(Position position) throws ServiceException {
        return isPositionGreenScreenReadyPositionOfProfile(this.getActiveProfile(),position);
    }

    @Override
    public Logo getLogoOfRelativeRectangleOfProfile(RelativeRectangle relativeRectangle) throws ServiceException {
        return getLogoOfRelativeRectangleOfProfile(this.getActiveProfile(),relativeRectangle);
    }

    @Override
    public RelativeRectangle getRelativeRectangleOfLogoOfProfile(Logo logo) throws ServiceException {
        return getRelativeRectangleOfLogoOfProfile(this.getActiveProfile(),logo);
    }

    @Override
    public Logo getProfileWaterMark() throws ServiceException {
        return getProfileWaterMark(this.getActiveProfile());
    }

    @Override
    public String getProfileName() throws ServiceException {
        return getProfileName(this.getActiveProfile());
    }

    @Override
    public boolean isProfilePrintEnabled() throws ServiceException {
        return isProfilePrintEnabled(this.getActiveProfile());
    }

    @Override
    public boolean isProfileFilerEnabled() throws ServiceException {
        return isProfileFilerEnabled(this.getActiveProfile());
    }

    @Override
    public boolean isProfileGreenscreenEnabled() throws ServiceException {
        return isProfileGreenscreenEnabled(this.getActiveProfile());
    }

    @Override
    public List<Profile.PairCameraPosition> getAllPairCameraPositionOfProfile(int profileId) throws ServiceException {
        try {
            return profileDAO.read(profileId).getPairCameraPositions();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting objects in service layer has failed.:" + e);
        }
    }

    @Override
    public List<Profile.PairCameraPosition> getAllPairCameraPositionOfProfile() throws ServiceException {
        return getAllPairCameraPositionOfProfile(this.getActiveProfile().getId());
    }
    @Override
    public List<Profile.PairCameraPosition> getAllPairCamerasWithPositionByProfile(int profileID) throws ServiceException {
        List<Profile.PairCameraPosition> allCameras = getAllPairCameraPositionOfProfile(profileID);
        for(Camera c: cameraService.getAllCameras()) {
            boolean exist = false;
            for (Profile.PairCameraPosition p: allCameras){
                if(c.getId()==p.getCamera().getId())
                    exist = true;
            }
            if(!exist)
                allCameras.add(new Profile.PairCameraPosition(profileID,c,null,false));

        }
        return allCameras;
    }

    @Override
    public Profile.PairCameraPosition addPairCameraPosition(int profileId, int cameraId, int positionId, boolean isGreenScreenReady) throws ServiceException {
        Profile.PairCameraPosition pairCameraPosition
                = new Profile.PairCameraPosition(
                this.getCamera(cameraId),
                this.getPosition(positionId),
                isGreenScreenReady);
        Profile profile = this.get(profileId);
        List<Profile.PairCameraPosition> pairCameraPositions = profile.getPairCameraPositions();
        pairCameraPositions.add(pairCameraPosition);
        profile.setPairCameraPositions(pairCameraPositions);
        this.edit(profile);
        return pairCameraPosition;
    }

    @Override
    public Profile.PairCameraPosition addPairCameraPosition(int cameraId, int positionId, boolean isGreenScreenReady) throws ServiceException {
        return addPairCameraPosition(this.getActiveProfile().getId(),cameraId,positionId, isGreenScreenReady);
    }

    @Override
    public boolean editPairCameraPosition(Profile.PairCameraPosition pairCameraPosition, int newCameraId, int newPositionId, boolean invertIsGreenScreenReadySwitch) throws ServiceException {
        if (newCameraId > 0)
            {pairCameraPosition.setCamera(getCamera(newCameraId));}
        if (newPositionId > 0)
            {pairCameraPosition.setPosition(getPosition(newPositionId));}
        Profile profile = this.get(pairCameraPosition.getProfileId());
        List<Profile.PairCameraPosition> pairCameraPositions = profile.getPairCameraPositions();
        boolean b = true;
        for(Profile.PairCameraPosition auxPairCameraPosition:pairCameraPositions)
        {
            if(auxPairCameraPosition.getId()==pairCameraPosition.getId())
            {
                b = auxPairCameraPosition.isGreenScreenReady();
            }
        }
        if (invertIsGreenScreenReadySwitch) {
            if (b)
                {pairCameraPosition.setGreenScreenReady(false);}
            else {pairCameraPosition.setGreenScreenReady(true);}
        }
        return editPairCameraPosition(pairCameraPosition);
    }

    @Override
    public boolean editPairCameraPosition(Profile.PairCameraPosition pairCameraPosition) throws ServiceException{
        Profile profile = this.get(pairCameraPosition.getProfileId());
        List<Profile.PairCameraPosition> pairCameraPositions = profile.getPairCameraPositions();
        for(Profile.PairCameraPosition auxPairCameraPosition:pairCameraPositions)
        {
            if(auxPairCameraPosition.getId()==pairCameraPosition.getId())
            {
                auxPairCameraPosition.setCamera(pairCameraPosition.getCamera());
                auxPairCameraPosition.setPosition(pairCameraPosition.getPosition());
                auxPairCameraPosition.setGreenScreenReady(pairCameraPosition.isGreenScreenReady());
            }
        }
        profile.setPairCameraPositions(pairCameraPositions);
        return this.edit(profile);
    }

    @Override
    public boolean erasePairCameraPosition(Profile.PairCameraPosition pairCameraPosition) throws ServiceException {
        Profile profile = this.get(pairCameraPosition.getProfileId());
        List<Profile.PairCameraPosition> pairCameraPositions = profile.getPairCameraPositions();

        for(Iterator<Profile.PairCameraPosition> it = pairCameraPositions.iterator();it.hasNext();)
        {
            Profile.PairCameraPosition auxPairCameraPosition = it.next();
            if(auxPairCameraPosition.getId()== pairCameraPosition.getId())
            {
                it.remove(); //pairCameraPositions.remove(auxPairCameraPosition);
            }
        }


        profile.setPairCameraPositions(pairCameraPositions);
        return this.edit(profile);
    }

    @Override
    public List<Profile.PairLogoRelativeRectangle> getAllPairLogoRelativeRectangle(int profileId) throws ServiceException {
        try {
            return profileDAO.read(profileId).getPairLogoRelativeRectangles();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting objects in service layer has failed.:" + e);
        }
    }

    @Override
    public List<Profile.PairLogoRelativeRectangle> getAllPairLogoRelativeRectangle() throws ServiceException {
        return getAllPairLogoRelativeRectangle(this.getActiveProfile().getId());
    }

    @Override
    public Profile.PairLogoRelativeRectangle addPairLogoRelativeRectangle(int profileId, int logoId, RelativeRectangle relativeRectangle) throws ServiceException {
        Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle
                = new Profile.PairLogoRelativeRectangle(
                this.getLogo(logoId),
                relativeRectangle);
        Profile profile = this.get(profileId);
        List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles = profile.getPairLogoRelativeRectangles();
        pairLogoRelativeRectangles.add(pairLogoRelativeRectangle);
        profile.setPairLogoRelativeRectangles(pairLogoRelativeRectangles);
        this.edit(profile);
        return pairLogoRelativeRectangle;
    }

    @Override
    public Profile.PairLogoRelativeRectangle addPairLogoRelativeRectangle(int logoId, RelativeRectangle relativeRectangle) throws ServiceException {
        return addPairLogoRelativeRectangle(this.getActiveProfile().getId(),logoId,relativeRectangle);
    }

    @Override
    public boolean editPairLogoRelativeRectangle(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle, int newLogoId, RelativeRectangle newRelativeRectangle) throws ServiceException {
        if (newLogoId > 0)
            {pairLogoRelativeRectangle.setLogo(getLogo(newLogoId));}
        if (newRelativeRectangle != null)
            {pairLogoRelativeRectangle.setRelativeRectangle(newRelativeRectangle);}
        return editPairLogoRelativeRectangle(pairLogoRelativeRectangle);
    }

    @Override
    public boolean editPairLogoRelativeRectangle(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws ServiceException {
        LOGGER.debug("Entering editPairLogoRelativeRectangle with ->",pairLogoRelativeRectangle.getId());
        Profile profile = this.get(pairLogoRelativeRectangle.getProfileId());
        List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles = profile.getPairLogoRelativeRectangles();
        for(Profile.PairLogoRelativeRectangle auxPairLogoRelativeRectangle:pairLogoRelativeRectangles)
        {
            if(auxPairLogoRelativeRectangle.getId()==pairLogoRelativeRectangle.getId()) {
                auxPairLogoRelativeRectangle.setLogo(pairLogoRelativeRectangle.getLogo());
                auxPairLogoRelativeRectangle.setRelativeRectangle(pairLogoRelativeRectangle.getRelativeRectangle());
            }
        }
        profile.setPairLogoRelativeRectangles(pairLogoRelativeRectangles);
        return this.edit(profile);
    }

    @Override
    public boolean erasePairLogoRelativeRectangle(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle) throws ServiceException {
        Profile profile = this.get(pairLogoRelativeRectangle.getProfileId());
        List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles = profile.getPairLogoRelativeRectangles();
        for(Iterator<Profile.PairLogoRelativeRectangle> it = pairLogoRelativeRectangles.iterator();it.hasNext();)
        {
            Profile.PairLogoRelativeRectangle auxPairLogoRelativeRectangle = it.next();
            if(auxPairLogoRelativeRectangle.getId()==pairLogoRelativeRectangle.getId())
            {
                it.remove(); //pairLogoRelativeRectangles.remove(auxPairLogoRelativeRectangle);
            }
        }
        profile.setPairLogoRelativeRectangles(pairLogoRelativeRectangles);
        return this.edit(profile);
    }

    @Override
    public Background.Category addBackgroundCategoryToProfile(int profileId, Background.Category backgroundCategory) throws ServiceException {
        if(backgroundCategory.getId()==Integer.MIN_VALUE)
            {
                try {
                    backgroundCategory = backgroundCategoryDAO.create(backgroundCategory);
                    Profile profile = this.get(profileId);
                    List<Background.Category> backgroundCategories = profile.getBackgroundCategories();
                    backgroundCategories.add(backgroundCategory);
                    profile.setBackgroundCategories(backgroundCategories);
                    this.edit(profile);
                } catch (PersistenceException e) {
                    throw new ServiceException("Error! add background object to profile object in service layer has failed.:" + e);
                }
            }
        return backgroundCategory;
    }

    @Override
    public boolean eraseBackgroundCategoryFromProfile(int profileId, Background.Category backgroundCategory) throws ServiceException {
        Profile profile = this.get(profileId);
        List<Background.Category> backgroundCategories = profile.getBackgroundCategories();
        if(backgroundCategories.contains(backgroundCategory))
            {
                backgroundCategories.remove(backgroundCategory);
                profile.setBackgroundCategories(backgroundCategories);
                return this.edit(profile);
            }
        else
            {return false;}
    }

    @Override
    public Background.Category addBackgroundCategoryToProfile(Background.Category backgroundCategory) throws ServiceException {
        return this.addBackgroundCategoryToProfile(this.activeProfile.getId(),backgroundCategory);
    }

    @Override
    public boolean eraseBackgroundCategoryFromProfile(Background.Category backgroundCategory) throws ServiceException {
        return this.eraseBackgroundCategoryFromProfile(this.activeProfile.getId(),backgroundCategory);
    }

    @Override
    public Profile.PairCameraPosition getPairCameraPosition(Camera camera) throws ServiceException {

        Profile profile = this.getActiveProfile();
        List<Profile.PairCameraPosition> pairCameraPositions = profile.getPairCameraPositions();
        for(Profile.PairCameraPosition auxPairCameraPosition:pairCameraPositions)
        {
            if(auxPairCameraPosition.getCamera().equals(camera))
            {
               return auxPairCameraPosition;
            }
        }
        return null;
    }

    @Override
    public List<Background.Category> getAllCategoryOfProfile(int profileId) throws ServiceException{
        try {
            return backgroundCategoryDAO.readAllOfProfile(profileId); //this.get(profileId).getBackgroundCategories();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting background categories from profile object in service layer has failed.:" + e);
        }
    }

    @Override
    public List<Background.Category> getAllCategoryOfProfile() throws ServiceException{
        return this.getAllCategoryOfProfile(this.activeProfile.getId());
    }

    @Override
    public List<Background> getAllBackgroundOfProfile(int profileId) throws ServiceException{
        List<Background> backgrounds = new ArrayList<>();
        Profile profile = this.get(profileId);
        for (Background.Category backgroundCategory:profile.getBackgroundCategories())
            {
                backgrounds.addAll(backgroundService.getAllWithCategory(backgroundCategory.getId()));
            }
        return backgrounds;
    }

    @Override
    public List<Background> getAllBackgroundOfProfile() throws ServiceException{
        return this.getAllBackgroundOfProfile(this.activeProfile.getId());
    }

    public int getNumberOfUsing(int logoID) throws ServiceException {
        int countsOfLogoUsing = 0;
        for(Profile profile: getAllProfiles()) {
            for (Profile.PairLogoRelativeRectangle p : getAllPairLogoRelativeRectangle(profile.getId())) {
                if (p.getLogo() != null && logoID == p.getLogo().getId()) {
                    countsOfLogoUsing++;
                }
            }
        }
        return countsOfLogoUsing;
    }
    public int getNumberOfUsingByProfile(int logoID,int profileID) throws ServiceException {
        int countsOfLogoUsingByProfile = 0;

        for (Profile.PairLogoRelativeRectangle p : getAllPairLogoRelativeRectangle(profileID)) {
            if (p.getLogo() != null && logoID == p.getLogo().getId()) {
                countsOfLogoUsingByProfile++;
            }
        }

        return countsOfLogoUsingByProfile;
    }

    @Override
    public void resetActiveProfileNonPersistentAttributes() throws ServiceException{
        Profile profile = this.getActiveProfile();
        List<Profile.PairCameraPosition> pairCameraPositions = profile.getPairCameraPositions();
        for(Profile.PairCameraPosition auxPairCameraPosition:pairCameraPositions)
        {
            auxPairCameraPosition.setBackground(null);
            auxPairCameraPosition.setFilterName("");
            auxPairCameraPosition.setShotType(Profile.PairCameraPosition.SHOT_TYPE_SINGLE);
        }
        activeProfile.setPairCameraPositions(pairCameraPositions);
    }
}