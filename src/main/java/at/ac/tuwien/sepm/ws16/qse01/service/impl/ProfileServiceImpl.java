package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.LogoDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.PositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.ProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private List<Profile> profileList = new ArrayList<>();
    private List<Position> positionList = new ArrayList<>();
    private List<Logo> logoList = new ArrayList<>();
    @Autowired
    public ProfileServiceImpl(ProfileDAO profileDAO,
                              PositionDAO positionDAO,
                              LogoDAO logoDAO
    ) throws ServiceException {
    //public ProfileServiceImpl() throws ServiceException {
        this.profileDAO = profileDAO;
        this.positionDAO = positionDAO;
        this.logoDAO = logoDAO;

        try {
            profileList.addAll(profileDAO.readAll());
            positionList.addAll(positionDAO.readAll());
            logoList.addAll(logoDAO.readAll());
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
    public Profile get(long id) throws ServiceException {
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
    public Position getPosition(long id) throws ServiceException {
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
    public Logo getLogo(long id) throws ServiceException {
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
    public List<Position> getAllPositionsOfProfile(Profile profile) throws ServiceException {
        List<Position> returnvalue = new ArrayList<>();
        for(Profile.PairCameraPosition pairCameraPosition : profile.getCameraPositions()) {
            returnvalue.add(pairCameraPosition.getPosition());
        }
        return returnvalue;
    }

    @Override
    public List<Camera> getAllCamerasOfProfile(Profile profile) throws ServiceException {
        List<Camera> returnvalue = new ArrayList<>();
        for(Profile.PairCameraPosition pairCameraPosition : profile.getCameraPositions()) {
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
        for(Profile.PairCameraPosition pairCameraPosition : profile.getCameraPositions()) {
            if(pairCameraPosition.getCamera().getId()==camera.getId())
                return pairCameraPosition.getPosition();
        }
        return null;
    }

    @Override
    public Camera getCameraOfPositionOfProfile(Profile profile, Position position) throws ServiceException {
        for(Profile.PairCameraPosition pairCameraPosition : profile.getCameraPositions()) {
            if(pairCameraPosition.getPosition().getId()==position.getId())
                return pairCameraPosition.getCamera();
        }
        return null;
    }

    @Override
    public boolean isPositionGreenScreenReadyPositionOfProfile(Profile profile, Position position) throws ServiceException {
        for(Profile.PairCameraPosition pairCameraPosition : profile.getCameraPositions()) {
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
}
