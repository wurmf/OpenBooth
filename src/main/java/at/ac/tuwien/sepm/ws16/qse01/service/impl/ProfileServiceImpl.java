package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.ProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCProfileDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
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
    private List<Profile> profileList = new ArrayList<>();
    private Profile activeProfile = null;

    @Autowired
    public ProfileServiceImpl(ProfileDAO profileDAO) throws ServiceException {
    //public ProfileServiceImpl() throws ServiceException {
        this.profileDAO = profileDAO;
        try {
            //profileDAO = new JDBCProfileDAO();
            profileList.addAll(profileDAO.readAll());
            activeProfile = this.getActiveProfile();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Initializing service layer has failed.:" + e);
        }
    }

    @Override
    public Profile add(Profile profile) throws ServiceException {
        LOGGER.debug("Entering add method with parameters " + profile);
        try {
            Profile p = profileDAO.create(profile);
            profileList.add(p);
            return p;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Adding in service layer has failed.:" + e);
        }
    }

    @Override
    public void edit(Profile profile) throws ServiceException {
        LOGGER.debug("Entering edit method with parameters " + profile);
        try {
            profileDAO.update(profile);
            this.profileList = profileDAO.readAll();
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
    public void erase(Profile profile) throws ServiceException {
        LOGGER.debug("Entering erase method with parameters " + profile);
        try {
            profileDAO.delete(profile);
            this.profileList = profileDAO.readAll();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Erasing in service layer has failed.:" + e);
        }
    }

    @Override
    public void setActiveProfile(Profile profile) throws ServiceException{
        LOGGER.debug("Entering setActiveProfile method with parameters " + profile);
        if (profile == null) {throw new IllegalArgumentException("Error! SetActiveProfile methode has been called with a null Argument");}
        try {
            if (this.activeProfile != null){
                this.activeProfile.setIsActive(false);
                profileDAO.update(this.activeProfile);
            }
            profile.setIsActive(true);
            profileDAO.update(profile);
            this.profileList = profileDAO.readAll();
            this.activeProfile = this.getActiveProfile();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Setting active profile in service layer has failed.:" + e);
        }
    }

    @Override
    public Profile getActiveProfile() throws ServiceException{
        LOGGER.debug("Entering getActiveProfile method");
        try {
            this.profileList = profileDAO.readAll();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Getting active profile in service layer has failed.:" + e);
        }
        this.activeProfile = null;
        for (Profile profile:profileList){
            if(profile.isActive()){
                this.activeProfile = profile;
            }
        }
        return this.activeProfile;
    }
}
