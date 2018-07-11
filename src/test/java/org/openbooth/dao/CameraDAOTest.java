package org.openbooth.dao;

import org.junit.Test;
import org.openbooth.TestEnvironment;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Camera;
import org.openbooth.util.TestDataProvider;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class CameraDAOTest extends TestEnvironment {

    private CameraDAO cameraDAO = getApplicationContext().getBean(CameraDAO.class);

    private TestDataProvider testDataProvider;

    private Camera storedCamera;
    private Camera newCamera;
    private List<Camera> storedCameras;


    @Override
    protected void prepareTestData(){
        testDataProvider = getNewTestDataProvider();
        storedCameras = testDataProvider.getStoredCameras();
        storedCamera = storedCameras.get(0);
        newCamera = testDataProvider.getNewCamera();
    }

    /**
     * TESTING METHOD: Camera create(Camera camera)
     */

    @Test
    public void testValidCreateAndRead() throws PersistenceException{
        newCamera.setId(cameraDAO.create(newCamera).getId());
        assertEquals(newCamera, cameraDAO.read(newCamera.getId()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull() throws PersistenceException{
        cameraDAO.create(null);
    }

    /**
     * TESTING METHOD: void delete(int cameraID)
     */

    @Test
    public void testValidDeleteAndReadAll() throws PersistenceException{
        cameraDAO.delete(storedCamera.getId());
        assertTrue(!cameraDAO.getAll().contains(storedCamera));
    }

    /**
     * TESTING METHOD: Camera read(int id)
     */

    @Test
    public void testValidRead() throws PersistenceException{
        assertEquals(storedCamera, cameraDAO.read(storedCamera.getId()));
    }

    /**
     * TESTING METHOD: List<Camera> readActive()
     */

    @Test
    public void testValidReadActive() throws PersistenceException{
        List<Camera> activeCameras = storedCameras.stream().filter(Camera::isActive).collect(Collectors.toList());
        List<Camera> readCameras = cameraDAO.readActive();
        assertTrue(activeCameras.containsAll(readCameras));
        assertTrue(readCameras.containsAll(activeCameras));
    }

    /**
     * TESTING METHOD: List<Camera> getAll()
     */

    @Test
    public void testValidGetAll() throws PersistenceException{
        List<Camera> readCameras = cameraDAO.getAll();
        assertTrue(storedCameras.containsAll(readCameras));
        assertTrue(readCameras.containsAll(storedCameras));
    }

    /**
     * TESTING METHOD: void setActive(int cameraID)
     */

    @Test
    public void testValidSetActiveAndReadActive() throws PersistenceException{
        List<Camera> inActiveCameras = storedCameras.stream().filter(c -> !c.isActive()).collect(Collectors.toList());
        for(Camera camera : inActiveCameras){
            cameraDAO.setActive(camera.getId());
            camera.setActive(true);
        }
        assertTrue(cameraDAO.readActive().containsAll(inActiveCameras));

    }

    /**
     * TESTING METHOD: void setAllInactive()
     */

    @Test
    public void testValidSetAllInActiveAndReadAll() throws PersistenceException{
        cameraDAO.setAllInactive();
        for(Camera camera : cameraDAO.getAll()){
            assertTrue(!camera.isActive());
        }
    }

    /**
     * TESTING METHOD: Camera getCameraIfExists(Camera camera)
     */

    @Test
    public void testGetCameraIfExistsWithExistingCamera() throws PersistenceException{
        assertEquals(storedCamera,cameraDAO.getCameraIfExists(storedCamera));
    }

    @Test
    public void testGetCameraIfExistsWithNonExistingCamera() throws PersistenceException{
        assertNull(cameraDAO.getCameraIfExists(testDataProvider.getNewCamera()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCameraIfExistsWithNull() throws PersistenceException{
        cameraDAO.getCameraIfExists(null);
    }

    /**
     * TESTING METHOD: void editCamera(Camera camera)
     */

    @Test
    public void testValidEditCameraAndRead() throws PersistenceException{
        storedCamera.setLable("NewLabel");
        storedCamera.setModel("NewModel");
        storedCamera.setPort("NewPort");
        storedCamera.setSerialnumber("NewSerialnumber");
        cameraDAO.editCamera(storedCamera);
        assertEquals(storedCamera, cameraDAO.read(storedCamera.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditCameraWithNull() throws PersistenceException{
        cameraDAO.editCamera(null);
    }









}
