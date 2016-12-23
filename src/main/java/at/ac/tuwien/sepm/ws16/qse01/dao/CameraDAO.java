package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;

import java.util.List;


public interface CameraDAO {

    /**
     * Persists a given camera object
     * @param camera - camera object to be persisted, must not be null
     * @return persisted camera object with autoassigned id
     * @throws PersistenceException if persistence data store can not be accessed
     */
    public Camera create(Camera camera) throws PersistenceException;

    /**
     * Remove given camera from persistence store
     * @param cameraID - camera to be removed is identified by its id
     * @throws PersistenceException if persistence data store can not be accessed
     */
    public void delete(int cameraID) throws PersistenceException;

    /**
     * Retrieve a Camera entity identified by id from the persistence store (independent from its activity status)
     * @param id - id of the camera that is looked for
     * @return camera that has been looked for, if a camera with given id doesn't
     * exist in persistence store, null will be returned
     */
    Camera read(int id) throws PersistenceException;

    /**
     * Retrueve a list of all active cameras
     * @return list of camera objects that are set active
     * @throws PersistenceException if persistence data store can not be accessed
     */
    public List<Camera> readActive() throws PersistenceException;

    /**
     * Sets the camera with the given ID to active
     * @param cameraID - ID of the camera that will be set active
     * @throws PersistenceException if persistence data store can not be accessed
     */
    public void setActive(int cameraID) throws PersistenceException;

    /**
     * Sets the camera with the given ID to inactive
     * @param cameraID - ID of the camera that will be set inactive
     * @throws PersistenceException if persistence data store can not be accessed
     */
    public void setInactive(int cameraID) throws PersistenceException;

    /**
     * Sets all saved Cameras to inactive
     * @throws PersistenceException if persistence data store can not be accessed
     */
    public void setAllInactive() throws PersistenceException;

    /**
     * Looks a given camera up and returns if it already exists
     * @param camera - camera which will be looked up in the persistence
     * @return true if the given camera is already saved (identified by portnumber and cameramodel)
     * @throws PersistenceException
     */
    public Camera exists(Camera camera) throws PersistenceException;
}
