package org.openbooth.service;

import org.openbooth.entities.Camera;
import org.openbooth.service.exceptions.ServiceException;

import java.util.List;


public interface CameraService {

    public Camera createCamera(Camera camera) throws ServiceException;

    public void deleteCamera(int cameraID) throws ServiceException;

    public List<Camera> getActiveCameras() throws ServiceException;

    public Camera cameraExists(Camera camera) throws ServiceException;

    public void setCameraActive(int cameraID) throws ServiceException;

    public void setCameraInactive(int cameraID) throws ServiceException;

    public void setAllCamerasInactive() throws ServiceException;

    public List<Camera> getAllCameras() throws ServiceException;

    public Camera editCamera(Camera camera) throws ServiceException;
}
