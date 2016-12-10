package at.ac.tuwien.sepm.ws16.qse01.service.impl;

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
    private List<Profile> profileList = new ArrayList<>();
    private List<Position> positionList = new ArrayList<>();

    @Autowired
    public ProfileServiceImpl(ProfileDAO profileDAO,PositionDAO positionDAO) throws ServiceException {
    //public ProfileServiceImpl() throws ServiceException {
        this.profileDAO = profileDAO;
        this.positionDAO = positionDAO;
        try {
            profileList.addAll(profileDAO.readAll());
            positionList.addAll(positionDAO.readAll());
        } catch (PersistenceException e) {
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
            throw new ServiceException("Error! Adding in service layer has failed.:" + e);
        }
    }

    public boolean editPosition(Position position) throws ServiceException {
        return false;
    }

    @Override
    public Position getPosition(long id) throws ServiceException {
        return null;
    }

    @Override
    public List<Position> getAllPositions() throws ServiceException {
        LOGGER.debug("Entering getAllPositions method");
        try {
            this.positionList = positionDAO.readAll();
            return this.positionList;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting All in service layer has failed.:" + e);
        }
    }

    @Override
    public boolean erasePosition(Position position) throws ServiceException {
        return false;
    }

    @Override
    public Position addLogo(Logo logo) throws ServiceException {
        return null;
    }

    @Override
    public boolean editLogo(Logo logo) throws ServiceException {
        return false;
    }

    @Override
    public Position getLogo(long id) throws ServiceException {
        return null;
    }

    @Override
    public List<Logo> getAllLogos() throws ServiceException {
        return null;
    }

    @Override
    public boolean eraseLogo(Logo logo) throws ServiceException {
        return false;
    }

    @Override
    public boolean setEditedProfile(Profile profile) throws ServiceException {
        return false;
    }

    @Override
    public Profile getEditedProfile() throws ServiceException {
        return null;
    }

    @Override
    public Profile getActiveProfile() throws ServiceException {
        return null;
    }

    @Override
    public List<Position> getAllPositionsOfActiveProfile() throws ServiceException {
        return null;
    }

    @Override
    public List<Camera> getAllCamerasOfActiveProfile() throws ServiceException {
        return null;
    }

    @Override
    public List<RelativeRectangle> getAllRelativeRectanglesOfActiveProfile() throws ServiceException {
        return null;
    }

    @Override
    public List<Logo> getAllLogosOfActiveProfile() throws ServiceException {
        return null;
    }

    @Override
    public Position getPositionOfCameraOfActiveProfile(Camera camera) throws ServiceException {
        return null;
    }

    @Override
    public Camera getCameraOfPositionOfActiveProfile(Position position) throws ServiceException {
        return null;
    }

    @Override
    public boolean isGreenScreenReadyPositionOfActiveProfile(Position position) throws ServiceException {
        return false;
    }

    @Override
    public Logo getRelativeRectangleOfLogoOfActiveProfile(RelativeRectangle relativeRectangle) throws ServiceException {
        return null;
    }

    @Override
    public RelativeRectangle getLogoOfRelativeRectangleOfActiveProfile(Logo logo) throws ServiceException {
        return null;
    }

    @Override
    public Logo getWaterMarkOfActiveProfile() throws ServiceException {
        return null;
    }

    @Override
    public String getNameOfActiveProfile() throws ServiceException {
        return null;
    }

    @Override
    public boolean isPrintEnabledActiveProfile() throws ServiceException {
        return false;
    }

    @Override
    public boolean isFilerEnabledActiveProfile() throws ServiceException {
        return false;
    }

    @Override
    public boolean isGreenscreenEnabledActiveProfile() throws ServiceException {
        return false;
    }

    @Override
    public boolean setActiveProfile(Profile profile) throws ServiceException {
        return false;
    }

}
