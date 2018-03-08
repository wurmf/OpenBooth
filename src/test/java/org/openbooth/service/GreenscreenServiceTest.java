package org.openbooth.service;

import org.openbooth.util.ImageHandler;
import org.openbooth.util.OpenCVLoader;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.util.exceptions.LibraryLoadingException;
import org.openbooth.entities.Background;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.impl.GreenscreenServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertTrue;

/**
 * Created by fabian on 04.01.17.
 */
public class GreenscreenServiceTest {

    private GreenscreenService greenscreenService;
    private ImageHandler imageHandler;

    private String srcImgPath;
    private BufferedImage srcImg;
    private String backgroundImgPath;

    private Background background;


    @Before
    public void setUp() throws ServiceException, LibraryLoadingException, ImageHandlingException {
        OpenCVLoader openCVLoader = new OpenCVLoader();
        imageHandler = new ImageHandler(openCVLoader);
        greenscreenService = new GreenscreenServiceImpl(openCVLoader, imageHandler);

        srcImgPath = this.getClass().getResource("/greenscreen/images/greenscreen_ufo.jpeg").getPath();
        backgroundImgPath = this.getClass().getResource("/greenscreen/background/test_background2.png").getPath();

        srcImg = imageHandler.openImage(srcImgPath);

        Background.Category category = new Background.Category(1, "testcategory", false);
        background = new Background(1, "testbackground", backgroundImgPath, category, false);
    }

    @Test
    @Ignore
    public void applyGreenscreenWithValidParameters() throws ServiceException{

        BufferedImage result = greenscreenService.applyGreenscreen(srcImg, background);

        assertTrue(result != null);

    }

    @Test
    @Ignore
    public void applyGreenscreenWithDifferentBackgroundSize() throws ServiceException{

        String newBackgroundPath = this.getClass().getResource("/greenscreen/background/test_background1.jpg").getPath();

        background.setPath(newBackgroundPath);

        BufferedImage result = greenscreenService.applyGreenscreen(srcImg, background);

        assertTrue(result != null);
    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void applyGreenscreenWithNullImage() throws ServiceException{
        greenscreenService.applyGreenscreen(null, background);
    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void applyGreenscreenWithNullBackground() throws ServiceException{
        greenscreenService.applyGreenscreen(srcImg, null);
    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void applyGreenscreenWithInvalidBackgroundPath() throws ServiceException{
        background.setPath("/this/is/no/valid/path.jpg");
        greenscreenService.applyGreenscreen(srcImg, background);
    }



}
