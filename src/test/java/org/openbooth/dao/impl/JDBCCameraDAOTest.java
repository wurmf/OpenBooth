package org.openbooth.dao.impl;

import org.junit.Test;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Camera;
import org.openbooth.util.TestDataProvider;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class JDBCCameraDAOTest extends JDBCTestEnvironment{

    private JDBCCameraDAO cameraDAOWithMock;
    private JDBCCameraDAO cameraDAO = getApplicationContext().getBean(JDBCCameraDAO.class);

    private TestDataProvider testDataProvider;



    @SuppressWarnings("unchecked")
    @Override
    public void setUp() throws Exception {
        super.setUp();
        cameraDAOWithMock = new JDBCCameraDAO(mockDBHandler);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        //noinspection MagicConstant
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    }

    @Override
    protected void prepareTestData() {
        testDataProvider = getNewTestDataProvider();
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithSQLException() throws PersistenceException{
        cameraDAOWithMock.create(new Camera(-1, "test", "test","test","test", false));
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteWithSQLException() throws PersistenceException{
        cameraDAOWithMock.delete(1);
    }

    @Test(expected = PersistenceException.class)
    public void testReadWithSQLException() throws PersistenceException{
        cameraDAOWithMock.read(1);
    }

    @Test(expected = PersistenceException.class)
    public void testReadActiveWithSQLException() throws PersistenceException{
        cameraDAOWithMock.readActive();
    }

    @Test(expected = PersistenceException.class)
    public void testGetAllWithSQLException() throws PersistenceException{
        cameraDAOWithMock.getAll();
    }

    @Test
    public void testValidSetActivationStatus() throws PersistenceException{
        Camera storedCamera = testDataProvider.getStoredCameras().get(0);
        cameraDAO.setActivationStatus(storedCamera.getId(), !storedCamera.isActive());
        assertEquals(cameraDAO.read(storedCamera.getId()).isActive(), !storedCamera.isActive());
    }

    @Test(expected = PersistenceException.class)
    public void testSetActivationStatusWithSQLException() throws PersistenceException{
        cameraDAOWithMock.setActivationStatus(1, true);
    }

    @Test(expected = PersistenceException.class)
    public void testSetAllInactiveWithSQLException() throws PersistenceException{
        cameraDAOWithMock.setAllInactive();
    }

    @Test(expected = PersistenceException.class)
    public void testGetCameraIfExistsWithSQLException() throws PersistenceException{
        cameraDAOWithMock.getCameraIfExists(new Camera(1, "test", "test", "test", "test", false));
    }

    @Test(expected = PersistenceException.class)
    public void testEditCameraWithSQLException() throws PersistenceException{
        cameraDAOWithMock.editCamera(new Camera(1, "test", "test", "test", "test", false));
    }

}
