package org.openbooth.dao;

import org.openbooth.dao.impl.TestEnvironment;
import org.openbooth.entities.Image;
import org.openbooth.entities.Shooting;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests possible cases for ImageDAO
 */
public class ImageDAOTest extends TestEnvironment {
    private Image img = new Image(99,"/images/validImage.jpg",2,new Date());
    private int id = 1;

    @Before public void setUp() throws Exception {
        super.setUp();
    }

    @After public void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * This test creates a null-image.
     * DAO should throw IllegalArgumentException.
     */

    @Test(expected = IllegalArgumentException.class)
    public void createNullImage() throws Throwable  {
        mockImageDAO.create(null);
    }


    /**
     * This test creates an image with valid data.
     * DAO should save it in database.
     */

    @Test
    public void createValidImage() throws Throwable  {
        Image createdImg = imageDAO.create(img);

        assertTrue(createdImg.getImageID()>0);
        assertTrue(createdImg.getImagepath().equals(img.getImagepath()));
        assertTrue(createdImg.getShootingid()==img.getShootingid());
        assertTrue(createdImg.getDate() == img.getDate());
    }

    @Test
    public void mockCreateValidImage() throws Throwable{

        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(1)).thenReturn(id);
        Image returnvalue = mockImageDAO.create(img);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(returnvalue.getImageID() == 1);
        assertTrue(returnvalue.getImagepath() == "/images/validImage.jpg");
        assertTrue(returnvalue.getShootingid() == 2);
    }

    /**
     * This test reads an existing image from database.
     */

    @Test
    public void readExistingImageFromDatabase() throws Throwable  {
        Image img = new Image(99,"/images/existingImage.jpg",2,new Date());

        Image createdImg = imageDAO.create(img);

        //Check if read function returns the saved image.
        assertTrue(createdImg.getImageID()>0);
        assertTrue(createdImg.getImagepath().equals(img.getImagepath()));
        assertTrue(createdImg.getShootingid()==img.getShootingid());
        assertTrue(createdImg.getDate() == img.getDate());
    }



    /**
     * This test reads all images of an existing shooting in database.
     */

    @Test
    public void getAllImagesByExistingShootingID() throws Throwable  {
        Shooting shooting = shootingDAO.create(new Shooting(9999,1,"/asdf/","/asdf2/",true));
        Image img = new Image(99,"/images/lastCreatedImage.jpg",shooting.getId(),new Date());

        imageDAO.create(img);

        assertTrue(imageDAO.getAllImages(shooting.getId()).size()==1);
    }

    /**
     * This test reads the path of last created image from database.
     */
    @Test
    public void getLastCreatedImagePath() throws Throwable  {

        imageDAO.create(img);

        //Check if the last created imagepath will be returned
        assertTrue(imageDAO.getLastImgPath(img.getShootingid()) == img.getImagepath());
    }

    /**
     * This test reads the next possible image ID
     */

    @Test
    public void getNextImageID() throws Throwable  {
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.getInt(1)).thenReturn(id);

        int nextid = mockImageDAO.getNextImageID();
        verify(mockPreparedStatement).executeQuery();
        assertTrue(nextid==id);
    }




}
