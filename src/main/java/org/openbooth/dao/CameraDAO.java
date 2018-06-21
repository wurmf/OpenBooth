package org.openbooth.dao;

import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Camera;

import java.util.List;


public interface CameraDAO {

    /**
     * Persists a given simcam object
     * @param camera - simcam object to be persisted, must not be null
     * @return persisted simcam object with autoassigned id
     * @throws PersistenceException if persistence data store can not be accessed
     */
    Camera create(Camera camera) throws PersistenceException;

    /**
     * Remove given simcam from persistence store
     * @param cameraID - simcam to be removed is identified by its id
     * @throws PersistenceException if persistence data store can not be accessed
     */
    void delete(int cameraID) throws PersistenceException;

    /**
     * Retrieve a Camera entity identified by id from the persistence store (independent from its activity status)
     * @param id - id of the simcam that is looked for
     * @return simcam that has been looked for, if a simcam with given id doesn't
     * exist in persistence store, null will be returned
     */
    Camera read(int id) throws PersistenceException;

    /**
     * Retrueve a list of all active cameras
     * @return list of simcam objects that are set active
     * @throws PersistenceException if persistence data store can not be accessed
     */
    List<Camera> readActive() throws PersistenceException;

    /**
     * Sets the simcam with the given ID to active
     * @param cameraID - ID of the simcam that will be set active
     * @throws PersistenceException if persistence data store can not be accessed
     */
    void setActive(int cameraID) throws PersistenceException;

    /**
     * Sets the simcam with the given ID to inactive
     * @param cameraID - ID of the simcam that will be set inactive
     * @throws PersistenceException if persistence data store can not be accessed
     */
    void setInactive(int cameraID) throws PersistenceException;

    /**
     * Sets all saved Cameras to inactive
     * @throws PersistenceException if persistence data store can not be accessed
     */
    void setAllInactive() throws PersistenceException;

    /**
     * returns all Cameras
     * @return list of all Cameras
     * @throws PersistenceException if persistence data store can not be accessed
     */
    List<Camera> getAll() throws PersistenceException;

    /**
     * Looks a given simcam up and returns if it already exists
     * @param camera - simcam which will be looked up in the persistence
     * @return true if the given simcam is already saved (identified by portnumber and cameramodel)
     * @throws PersistenceException
     */
    Camera exists(Camera camera) throws PersistenceException;

    /**
     * Looks a given simcam up and returns if it already exists
     * @param camera - simcam which will be edited in the persistence
     * @return edited simcam
     * @throws PersistenceException
     */
    Camera editCamera(Camera camera) throws PersistenceException;
}
