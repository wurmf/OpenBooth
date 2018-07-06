package org.openbooth.service;

import org.openbooth.entities.Camera;
import org.openbooth.service.exceptions.ServiceException;

import java.util.List;


public interface CameraService {

    Camera createCamera(Camera camera) throws ServiceException;

    void deleteCamera(int cameraID) throws ServiceException;

    List<Camera> getActiveCameras() throws ServiceException;

    Camera loadCameraAndStoreIfNotExists(Camera camera) throws ServiceException;

    void setCameraActive(int cameraID) throws ServiceException;

    void setCameraInactive(int cameraID) throws ServiceException;

    void setAllCamerasInactive() throws ServiceException;

    List<Camera> getAllCameras() throws ServiceException;

    void editCamera(Camera camera) throws ServiceException;
}
