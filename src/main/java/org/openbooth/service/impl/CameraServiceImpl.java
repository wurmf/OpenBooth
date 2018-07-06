package org.openbooth.service.impl;

import org.openbooth.dao.CameraDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Camera;
import org.openbooth.service.CameraService;
import org.openbooth.service.exceptions.ServiceException;
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
            return cameraDAO.readActive();
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Failure at searching active Cameras");
            throw new ServiceException(ex);
        }
    }

    @Override
    public Camera loadCameraAndStoreIfNotExists(Camera camera) throws ServiceException
    {
        try {
            Camera storedCamera = cameraDAO.getCameraIfExists(camera);

            if(storedCamera == null) storedCamera = cameraDAO.create(camera);

            return storedCamera;
        }
        catch (PersistenceException ex) {
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

    @Override
    public List<Camera> getAllCameras() throws ServiceException {
        try
        {
            return cameraDAO.getAll();
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Cameras could not be set inactive");
            throw new ServiceException(ex);
        }
    }

    @Override
    public void editCamera(Camera camera) throws ServiceException {
        try
        {
            cameraDAO.editCamera(camera);
        }
        catch (PersistenceException ex)
        {
            LOGGER.error("Cameras could not be set inactive");
            throw new ServiceException(ex);
        }
    }
}
