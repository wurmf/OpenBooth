package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCCameraDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CameraDAOTest {

    private CameraDAO cameraDAO;
    @Before
    public void before() throws Exception {
        cameraDAO = new JDBCCameraDAO(H2Handler.getInstance());
    }

    /**
     * This test creates a null-camera.
     * DAO should throw IllegalArgumentException.
     */
    /*
    @Test(expected = IllegalArgumentException.class)
    public void createNullCamera() throws Throwable  {
        cameraDAO.create(null);
    }
    */
    /**
     * This test creates an image with valid data.
     * DAO should save it in database.
     */
    /*
    @Test
    public void createValidCamera() throws Throwable  {
        Camera camera = new Camera(-1, "", "", "", "");

        camera = cameraDAO.create(camera);

        assertThat(cameraDAO.exists(camera).getId()==camera.getId(), is(true));

    }
    */

    /**
     * This test sets an existing camera to active.
     */
    /*
    @Test
    public void setExistingCameraActive() throws Throwable  {

        Camera camera = new Camera(-1, "", "", "", "");

        camera = cameraDAO.create(camera);
        cameraDAO.setActive(camera.getId());
        int i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==camera.getId())
            {
                i++;
            }
        }
        assertThat(i, is(1));
    }
    */

    /**
     * This test sets an not existing camera to active.
     */
    /*
    @Test
    public void setNotExistingCameraActive() throws Throwable  {


        cameraDAO.setActive(-1);

        for (Camera camera :cameraDAO.readActive())
        {
            assertThat(camera.getId()==-1, is(false));
        }
    }
    */

    /**
     * This test deletes an existing camera  to active.
     */
    /*
    @Test
    public void deleteExistingCamera() throws Throwable  {


        Camera camera = new Camera(-1, "", "", "", "");

        camera = cameraDAO.create(camera);
        cameraDAO.setActive(camera.getId());
        int i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==camera.getId())
            {
                i++;
            }
        }
        assertThat(i, is(1));

        cameraDAO.delete(camera.getId());
        i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==camera.getId())
            {
                i++;
            }
        }
        assertThat(i, is(0));
    }
    */

    /**
     * This test reads all active cameras.
     */
    /*
    @Test
    public void readAllActive() throws Throwable  {


        Camera camera1 = new Camera(-1, "", "", "", "");

        camera1 = cameraDAO.create(camera1);
        cameraDAO.setActive(camera1.getId());

        int i=0;
        for (Camera camera3 :cameraDAO.readActive())
        {
            if(camera3.getId()==camera1.getId())
            {
                i++;
            }
        }
        assertThat(i, is(1));

        Camera camera2 = new Camera(-2, "", "", "", "");
        camera2 = cameraDAO.create(camera2);
        i=0;
        for (Camera camera4 :cameraDAO.readActive())
        {
            if(camera4.getId()==camera2.getId())
            {
                i++;
            }
        }
        assertThat(i, is(0));
    }
    */

    /**
     * This test sets an existing camera to inactive.
     */
    /*
    @Test
    public void setExistingCameraInactive() throws Throwable  {

        Camera camera = new Camera(-1, "", "", "", "");

        camera = cameraDAO.create(camera);
        cameraDAO.setActive(camera.getId());
        int i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==camera.getId())
            {
                i++;
            }
        }
        assertThat(i, is(1));
        cameraDAO.setInactive(camera.getId());
        i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==camera.getId())
            {
                i++;
            }
        }
        assertThat(i, is(0));
    }
    */
}
