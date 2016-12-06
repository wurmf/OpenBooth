package at.ac.tuwien.sepm.ws16.qse01.dao;

import at.ac.tuwien.sepm.util.dbhandler.impl.H2Handler;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.ImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCImageDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.impl.JDBCShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests if everything reacts so as required.
 */
public class ImageDAOTest {
    private ImageDAO imageDAO;
    private ShootingDAO shootingDAO;
    @Before
    public void before() throws Exception {
        imageDAO = new JDBCImageDAO(H2Handler.getInstance());
        shootingDAO = new JDBCShootingDAO(H2Handler.getInstance());
    }

    /**
     * This test creates a null-image.
     * DAO should throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void createNullImage() throws Throwable  {
        imageDAO.create(null);
    }

    /**
     * This test creates an image with valid data.
     * DAO should save it in database.
     */
    @Test
    public void createValidImage() throws Throwable  {
        Image img = new Image(99,"/images/validImage.jpg",2,new Date());
        img.setAutoDate();

        //There is no image in database with ID = 99
        assertThat(String.valueOf(imageDAO.read(99)),is("null"));

        Image createdImg = imageDAO.create(img);

        //Check if the created image saved.
        assertThat(imageDAO.read(createdImg.getImageID()).getImagepath(),is(img.getImagepath()));
    }

    /**
     * This test reads an existing image from database.
     */
    @Test
    public void readExistingImageFromDatabase() throws Throwable  {
        Image img = new Image(99,"/images/existingImage.jpg",2,new Date());
        img.setAutoDate();

        Image createdImg = imageDAO.create(img);

        //Check if read function returns the saved image.
        assertThat(imageDAO.read(createdImg.getImageID()).getImagepath(),is("/images/existingImage.jpg"));
    }

    /**
     * This test reads the path of last created image from database.
     */
    @Test
    public void getLastCreatedImagePath() throws Throwable  {
        Image img = new Image(99,"/images/lastCreatedImage.jpg",2,new Date());
        img.setAutoDate();

        imageDAO.create(img);

        //Check if the last created imagepath will be returned
        assertThat(imageDAO.getLastImgPath(2),is("/images/lastCreatedImage.jpg"));
    }

    /**
     * This test reads all image paths of an existing shooting in database.
     */
    @Test
    public void getAllImagePaths() throws Throwable  {
        shootingDAO.create(new Shooting(99,"/images/shooting99",true));
        Image img = new Image(99,"/images/lastCreatedImage.jpg",2,new Date());
        img.setAutoDate();

        imageDAO.create(img);
        //TODO: shooting -> create() -> funktioniert nicht!
        //Check if the last created imagepath will be returned
        assertThat(imageDAO.getAllImages(99).size(),is(1));
    }






}
