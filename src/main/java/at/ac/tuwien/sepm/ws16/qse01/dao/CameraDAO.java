package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;

import java.util.List;


public interface CameraDAO {
    public Camera create(Camera camera) throws PersistenceException;

    public void delete(int cameraID) throws PersistenceException;

    public List<Camera> getActive() throws PersistenceException;

    public void setActive(int cameraID) throws PersistenceException;

    public void setInactive(int cameraID) throws PersistenceException;

    public void setAllInactive() throws PersistenceException;

    public Camera exists(Camera camera) throws PersistenceException;
}
