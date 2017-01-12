package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.BackgroundCategoryDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.BackgroundDAO;
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
        return null;
    }

    @Override
    public boolean edit(Background background) throws ServiceException {
        return false;
    }

    @Override
    public Background get(int id) throws ServiceException {
        return null;
    }

    @Override
    public List<Background> getAll() throws ServiceException {
        return null;
    }

    @Override
    public List<Background> getAllWithCategory(int id) throws ServiceException {
        return null;
    }

    @Override
    public List<Background> getAllforCurrentShootingAvailable() throws ServiceException {
        return null;
    }

    @Override
    public boolean erase(Background background) throws ServiceException {
        return false;
    }

    @Override
    public Background.Category addCategory(Background.Category category) throws ServiceException {
        return null;
    }

    @Override
    public boolean editCategory(Background.Category category) throws ServiceException {
        return false;
    }

    @Override
    public List<Background.Category> getCategory(int id) throws ServiceException {
        return null;
    }

    @Override
    public List<Background.Category> getAllCategories() throws ServiceException {
        return null;
    }

    @Override
    public boolean eraseCategory(int id) throws ServiceException {
        return false;
    }
}
