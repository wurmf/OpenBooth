package org.openbooth.service.impl;

import org.openbooth.dao.BackgroundCategoryDAO;
import org.openbooth.dao.BackgroundDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;
import org.openbooth.service.BackgroundService;
import org.openbooth.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Background Service Implementation
 */
@Service
public class BackgroundServiceImpl implements BackgroundService{
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundServiceImpl.class);

    @Resource
    private BackgroundDAO backgroundDAO;
    @Resource
    private BackgroundCategoryDAO backgroundCategoryDAO;

    public BackgroundServiceImpl(BackgroundDAO backgroundDAO,
                                 BackgroundCategoryDAO backgroundCategoryDAO) {
        this.backgroundDAO = backgroundDAO;
        this.backgroundCategoryDAO = backgroundCategoryDAO;
    }

    @Override
    public Background add(Background background) throws ServiceException {
        try {
            background = backgroundDAO.create(background);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! add background object in service layer has failed.:" + e);
        }
        return background;
    }

    @Override
    public void edit(Background background) throws ServiceException {
        try {
            backgroundDAO.update(background);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! editing background object in service layer has failed.:" + e);
        }
    }

    @Override
    public Background get(int id) throws ServiceException {
        Background background;
        try {
            background = backgroundDAO.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting background object in service layer has failed.:" + e);
        }
        return background;
    }

    @Override
    public List<Background> getAll() throws ServiceException {
        List<Background> backgrounds;
        try {
            backgrounds = backgroundDAO.readAll();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting all background objects in service layer has failed.:" + e);
        }
        return backgrounds;
    }

    @Override
    public List<Background> getAllWithCategory(Background.Category category) throws ServiceException {
        List<Background> backgrounds;
        try {
            backgrounds = backgroundDAO.readAllWithCategory(category);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting all background objects in service layer has failed.:" + e);
        }

        return backgrounds;
    }

    @Override
    public void erase(Background background) throws ServiceException {
        try {
            backgroundDAO.delete(background);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! erasing background object in service layer has failed.:" + e);
        }
    }

    @Override
    public Background.Category addCategory(Background.Category category) throws ServiceException {
        try {
            category = backgroundCategoryDAO.create(category);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! add backgroundCategory object in service layer has failed.:" + e);
        }
        return category;
    }

    @Override
    public void editCategory(Background.Category category) throws ServiceException {
        try {
             backgroundCategoryDAO.update(category);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! editing backgroundCategory object in service layer has failed.:" + e);
        }
    }

    @Override
    public Background.Category getCategory(int id) throws ServiceException {
        Background.Category backgroundCategory;
        try {
            backgroundCategory =  backgroundCategoryDAO.read(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting background object in service layer has failed.:" + e);
        }
        return backgroundCategory;
    }

    @Override
    public List<Background.Category> getAllCategories() throws ServiceException {
        List<Background.Category> backgroundCategories;
        try {
            backgroundCategories = backgroundCategoryDAO.readAll();
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting all backgroundCategory objects in service layer has failed.:" + e);
        }
        return backgroundCategories;
    }

    @Override
    public void eraseCategory(Background.Category category) throws ServiceException {
        try {
            backgroundCategoryDAO.delete(category);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! erasing backgroundCategory object in service layer has failed.:" + e);
        }
    }
    @Override
    public void createPairProfileCategory(int profileID,int categoryID) throws ServiceException{
        try {
            backgroundCategoryDAO.createProfileCategoryRelation(profileID,categoryID);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! createProfileCategoryRelation  object in service layer has failed.:",e);
        }
    }
    @Override
    public void deletePairProfileCategory(int profileID,int categoryID) throws ServiceException{
        try {
            backgroundCategoryDAO.deleteProfileCategoryRelation(profileID,categoryID);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! deleteProfileCategoryRelation  object in service layer has failed.:",e);
        }
    }
}
