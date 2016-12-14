package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.dao.CameraDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraServiceImpl implements CameraService {

    CameraDAO cameraDAO;
    static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);

    @Autowired
    public CameraServiceImpl(CameraDAO cameraDAO) {
        this.cameraDAO=cameraDAO;
    }

    @Override
    public Camera createCamera(Camera camera) throws ServiceException {
        try
        {
            return cameraDAO.create(camera);
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Camera could not be created");
            throw new ServiceException(ex);
        }
    }

    @Override
    public void deleteCamera(int cameraID) throws ServiceException {
        try
        {
            cameraDAO.delete(cameraID);
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Camera could not be deleted");
            throw new ServiceException(ex);
        }
    }

    @Override
    public List<Camera> getActiveCameras() throws ServiceException {
        try
        {
            return cameraDAO.getActive();
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Failure at searching active Cameras");
            throw new ServiceException(ex);
        }
    }

    @Override
    public Camera cameraExists(Camera camera) throws ServiceException
    {
        try
        {
            return cameraDAO.exists(camera);
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("cameraExists - Failure at searching given camera");
            throw new ServiceException(ex);
        }
    }

    @Override
    public void setCameraActive(int cameraID) throws ServiceException {
        try
        {
            cameraDAO.setActive(cameraID);
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Camera could not be set active");
            throw new ServiceException(ex);
        }
    }

    @Override
    public void setCameraInactive(int cameraID) throws ServiceException {
        try
        {
            cameraDAO.setInactive(cameraID);
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Camera could not be set inactive");
            throw new ServiceException(ex);
        }
    }

    @Override
    public void setAllCamerasInactive() throws ServiceException {
        try
        {
            cameraDAO.setAllInactive();
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Cameras could not be set inactive");
            throw new ServiceException(ex);
        }
    }
}
