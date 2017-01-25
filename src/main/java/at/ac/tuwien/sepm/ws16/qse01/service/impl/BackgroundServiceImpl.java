package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.BackgroundCategoryDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.BackgroundDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.service.BackgroundService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
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
                                 BackgroundCategoryDAO backgroundCategoryDAO)
        throws ServiceException {
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
    public boolean edit(Background background) throws ServiceException {
        boolean returnValue;
        try {
            returnValue = backgroundDAO.update(background);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! editing background object in service layer has failed.:" + e);
        }
        return returnValue;
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
    public List<Background> getAllWithCategory(int id) throws ServiceException {
        List<Background> backgrounds;
        try {
            backgrounds = backgroundDAO.readAllWithCategory(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! getting all background objects in service layer has failed.:" + e);
        }

        return backgrounds;
    }

    @Override
    public boolean erase(Background background) throws ServiceException {
        boolean returnValue;
        try {
            returnValue = backgroundDAO.delete(background);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! erasing background object in service layer has failed.:" + e);
        }
        return returnValue;
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
    public boolean editCategory(Background.Category category) throws ServiceException {
        boolean returnValue;
        try {
            returnValue = backgroundCategoryDAO.update(category);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! editing backgroundCategory object in service layer has failed.:" + e);
        }
        return returnValue;
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
    public boolean eraseCategory(Background.Category category) throws ServiceException {
        boolean returnValue;
        try {
            returnValue = backgroundCategoryDAO.delete(category);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! erasing backgroundCategory object in service layer has failed.:" + e);
        }
        return returnValue;
    }
    @Override
    public void createPairProfileCategory(int profileID,int categoryID) throws ServiceException{
        try {
            backgroundCategoryDAO.createPairProfileCategory(profileID,categoryID);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! createPairProfileCategory  object in service layer has failed.:",e);
        }
    }
    @Override
    public void deletePairProfileCategory(int profileID,int categoryID) throws ServiceException{
        try {
            backgroundCategoryDAO.deletePairProfileCategory(profileID,categoryID);
        } catch (PersistenceException e) {
            throw new ServiceException("Error! deletePairProfileCategory  object in service layer has failed.:",e);
        }
    }
}
