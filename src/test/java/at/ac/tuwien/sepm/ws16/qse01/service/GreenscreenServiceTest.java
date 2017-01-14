package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.exceptions.ImageHandlingException;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by fabian on 04.01.17.
 */
public abstract class GreenscreenServiceTest {

    private GreenscreenService greenscreenService;
    private ImageHandler imageHandler;

    private String srcImgPath;
    private String backgroundImgPath;

    private Background background;

    protected void setGreenscreenService(GreenscreenService greenscreenService){
        this.greenscreenService = greenscreenService;
    }

    protected void setImageHandler(ImageHandler imageHandler){
        this.imageHandler = imageHandler;
    }

    @Before
    public void setUp() throws ServiceException, LibraryLoadingException{
        srcImgPath = this.getClass().getResource("/greenscreen/images/greenscreen_ufo.jpeg").getPath();
        backgroundImgPath = this.getClass().getResource("/greenscreen/background/test_background3.jpg").getPath();

        Background.Category category = new Background.Category(1, "testcategory", false);
        background = new Background(1, "testbackground", backgroundImgPath, category, false);
    }

    @Test
    public void getPreviewWithValidParameters() throws ServiceException, ImageHandlingException{

        BufferedImage srcImg = imageHandler.openImage(srcImgPath);

        BufferedImage result = greenscreenService.applyGreenscreen(srcImg, background);

        imageHandler.saveImage(result, "/home/fabian/greenscreen_test_result3.jpg");


    }

}
