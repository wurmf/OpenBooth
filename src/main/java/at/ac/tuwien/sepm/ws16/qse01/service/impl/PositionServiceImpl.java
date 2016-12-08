package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.PositionDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.service.PositionService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * PositionService Implementation
 */
@Service
public class PositionServiceImpl implements PositionService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);
    @Resource
    private PositionDAO positionDAO;
    private List<Position> positionList = new ArrayList<>();

    @Autowired
    public PositionServiceImpl(PositionDAO positionDAO) throws ServiceException {
        this.positionDAO = positionDAO;
        try {
            positionList.addAll(positionDAO.readAll());
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Initializing service layer has failed.:" + e);
        }
    }

    @Override
    public Position add(Position position) throws ServiceException {
        LOGGER.debug("Entering add method with parameters " + position);
        try {
            Position p = positionDAO.create(position);
            positionList.add(p);
            return p;
        } catch (PersistenceException e) {
            throw new ServiceException("Error! Adding in service layer has failed.:" + e);
        }
    }

    @Override
    public void edit(Position position) throws ServiceException {

    }

    @Override
    public Position get(int id) throws ServiceException {
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
    public void erase(Position position) throws ServiceException {

    }
}
