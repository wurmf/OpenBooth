package org.openbooth.dao;

import org.openbooth.util.dbhandler.impl.H2Handler;
import org.openbooth.dao.impl.JDBCCameraDAO;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CameraDAOTest {

    private CameraDAO cameraDAO;
    @Before
    public void before() throws Exception {
        cameraDAO = new JDBCCameraDAO(H2Handler.getInstance());
    }

    /**
     * This test creates a null-simcam.
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
        Camera simcam = new Camera(-1, "", "", "", "");

        simcam = cameraDAO.create(simcam);

        assertThat(cameraDAO.exists(simcam).getId()==simcam.getId(), is(true));

    }
    */

    /**
     * This test sets an existing simcam to active.
     */
    /*
    @Test
    public void setExistingCameraActive() throws Throwable  {

        Camera simcam = new Camera(-1, "", "", "", "");

        simcam = cameraDAO.create(simcam);
        cameraDAO.setActive(simcam.getId());
        int i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==simcam.getId())
            {
                i++;
            }
        }
        assertThat(i, is(1));
    }
    */

    /**
     * This test sets an not existing simcam to active.
     */
    /*
    @Test
    public void setNotExistingCameraActive() throws Throwable  {


        cameraDAO.setActive(-1);

        for (Camera simcam :cameraDAO.readActive())
        {
            assertThat(simcam.getId()==-1, is(false));
        }
    }
    */

    /**
     * This test deletes an existing simcam  to active.
     */
    /*
    @Test
    public void deleteExistingCamera() throws Throwable  {


        Camera simcam = new Camera(-1, "", "", "", "");

        simcam = cameraDAO.create(simcam);
        cameraDAO.setActive(simcam.getId());
        int i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==simcam.getId())
            {
                i++;
            }
        }
        assertThat(i, is(1));

        cameraDAO.delete(simcam.getId());
        i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==simcam.getId())
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
     * This test sets an existing simcam to inactive.
     */
    /*
    @Test
    public void setExistingCameraInactive() throws Throwable  {

        Camera simcam = new Camera(-1, "", "", "", "");

        simcam = cameraDAO.create(simcam);
        cameraDAO.setActive(simcam.getId());
        int i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==simcam.getId())
            {
                i++;
            }
        }
        assertThat(i, is(1));
        cameraDAO.setInactive(simcam.getId());
        i=0;
        for (Camera camera1 :cameraDAO.readActive())
        {
            if(camera1.getId()==simcam.getId())
            {
                i++;
            }
        }
        assertThat(i, is(0));
    }
    */
}
